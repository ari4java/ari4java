package ch.loway.oss.ari4java.tools.http;

import ch.loway.oss.ari4java.tools.HttpResponse;
import ch.loway.oss.ari4java.tools.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.logging.ByteBufFormat;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.util.internal.SystemPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * HTTP and WebSocket client implementation based on netty.io.
 * <p>
 * Threading is handled by MultiThreadIoEventLoopGroup, which selects on multiple
 * sockets and provides threads to handle the events on the sockets.
 * <p>
 * Requires netty
 *
 * @author mwalton
 */
public class NettyHttpClient implements HttpClient, WsClient {

    public static final int CONNECTION_TIMEOUT_SEC = 10;
    public static final int READ_TIMEOUT_SEC = 30;
    public static final int MAX_HTTP_REQUEST = 16 * 1024 * 1024; // 16MB
    public static final int MAX_HTTP_BIN_REQUEST = 150 * 1024 * 1024; // 150MB

    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static final String HTTP_CODEC = "http-codec";
    private static final String HTTP_AGGREGATOR = "http-aggregator";
    private static final String HTTP_HANDLER = "http-handler";

    private final Logger logger = LoggerFactory.getLogger(NettyHttpClient.class);

    protected Bootstrap httpBootstrap;
    protected URI baseUri;
    private EventLoopGroup group;
    private EventLoopGroup shutDownGroup;
    protected String auth;

    protected HttpResponseHandler wsCallback;
    private String wsEventsUrl;
    private List<HttpParam> wsEventsParamQuery;
    private WsClientConnection wsClientConnection;
    private final AtomicInteger reconnectCount = new AtomicInteger(0);
    private int maxReconnectCount = 10; // -1 = infinite reconnect attempts
    private ChannelFuture wsChannelFuture;
    private ScheduledFuture<?> wsPingTimer = null;
    private ScheduledFuture<?> wsConnectionTimeout = null;
    protected NettyWSClientHandler wsHandler;
    protected ChannelFutureListener wsFuture;
    private static SslContext sslContext;

    private int pongFailureCount = 0;
    private long lastPong = 0;
    private static boolean autoReconnect = true;
    protected int pingPeriod = 5;
    protected TimeUnit pingTimeUnit = TimeUnit.MINUTES;
    protected ChannelPool pool;
    private final int threadCount;

    public NettyHttpClient() {
        // use at least 3 threads
        threadCount = Math.min(3, SystemPropertyUtil.getInt(
                "io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
        logger.debug("Starting NioEventLoopGroup with {} threads", threadCount);
        group = new MultiThreadIoEventLoopGroup(threadCount, NioIoHandler.newFactory());
        shutDownGroup = new MultiThreadIoEventLoopGroup(1, NioIoHandler.newFactory());
    }

    public void initialize(String baseUrl, String username, String password) throws URISyntaxException {
        if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }
        logger.debug("initialize url: {}, user: {}", baseUrl, username);
        baseUri = new URI(baseUrl);
        String protocol = baseUri.getScheme();
        if (!HTTP.equalsIgnoreCase(protocol) && !HTTPS.equalsIgnoreCase(protocol)) {
            logger.warn("Not http(s), protocol: {}", protocol);
            throw new IllegalArgumentException("Unsupported protocol: " + protocol);
        }
        this.auth = ARIEncoder.encodeCreds(username, password);
        initHttpBootstrap();
    }

    protected void initHttpBootstrap() {
        if (httpBootstrap == null) {
            // Bootstrap is the factory for HTTP connections
            logger.debug("""
                            Bootstrap with
                             connection timeout: {},
                             read timeout: {},
                             aggregator max-length: {}""",
                    CONNECTION_TIMEOUT_SEC,
                    READ_TIMEOUT_SEC,
                    MAX_HTTP_REQUEST);
            httpBootstrap = new Bootstrap();
            httpBootstrap.remoteAddress(baseUri.getHost(), getPort());
            httpBootstrap.group(group);
            bootstrapOptions(httpBootstrap);
            pool = new FixedChannelPool(httpBootstrap, new ChannelPoolHandler() {
                @Override
                public void channelCreated(Channel ch) throws Exception {
                    logger.trace("Channel Pool connection created: {}", ch);
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addFirst("logger", new LoggingHandler(HTTPLogger.class, LogLevel.TRACE, ByteBufFormat.SIMPLE));
                    addSSLIfRequired(pipeline, baseUri);
                    pipeline.addLast("read-timeout", new ReadTimeoutHandler(READ_TIMEOUT_SEC));
                    pipeline.addLast(HTTP_CODEC, new HttpClientCodec());
                    pipeline.addLast(HTTP_AGGREGATOR, new HttpObjectAggregator(MAX_HTTP_REQUEST));
                    pipeline.addLast(HTTP_HANDLER, new NettyHttpClientHandler());
                    logger.debug("pipeline names: {}", pipeline.names());
                    ch.closeFuture().addListener(future -> logger.debug("Channel closed, {}", ch));
                }

                @Override
                public void channelAcquired(Channel ch) {
                    logger.trace("Channel Pool connection acquired: {}", ch);
                }

                @Override
                public void channelReleased(Channel ch) {
                    logger.trace("Channel Pool connection released: {}", ch);
                }
            }, threadCount);
        }
    }

    private void bootstrapOptions(Bootstrap bootStrap) {
        bootStrap.channel(NioSocketChannel.class);
        bootStrap.option(ChannelOption.TCP_NODELAY, true);
        bootStrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootStrap.option(ChannelOption.SO_REUSEADDR, false);
        bootStrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECTION_TIMEOUT_SEC * 1000);
    }

    private static synchronized void addSSLIfRequired(ChannelPipeline pipeline, URI baseUri) throws SSLException {
        if (HTTPS.equalsIgnoreCase(baseUri.getScheme())) {
            if (sslContext == null) {
                sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            }
            pipeline.addLast("ssl", sslContext.newHandler(pipeline.channel().alloc()));
        }
    }

    private int getPort() {
        int port = baseUri.getPort();
        if (port == -1) {
            if (HTTP.equalsIgnoreCase(baseUri.getScheme())) {
                port = 80;
            } else if (HTTPS.equalsIgnoreCase(baseUri.getScheme())) {
                port = 443;
            }
        }
        return port;
    }

    protected Future<Channel> poolAcquire() {
        return pool.acquire();
    }

    protected void poolRelease(Channel ch) {
        pool.release(ch);
    }

    @Override
    public void destroy() {
        logger.debug("destroy...");
        // use a different event group to execute the shutdown to avoid deadlocks
        shutDownGroup.execute(() -> {
            logger.debug("running shutdown...");
            if (wsPingTimer != null) {
                logger.debug("cancel ping...");
                wsPingTimer.cancel(true);
                wsPingTimer = null;
            }
            if (wsConnectionTimeout != null) {
                logger.debug("cancel wsConnectionTimeout...");
                wsConnectionTimeout.cancel(true);
                wsConnectionTimeout = null;
            }
            if (wsClientConnection != null) {
                try {
                    logger.debug("there is a web socket, disconnect...");
                    wsClientConnection.disconnect();
                    wsClientConnection = null;
                } catch (RestException e) {
                    // not bubbling exception up, just ignoring
                }
            }
            if (group != null && !group.isShuttingDown()) {
                logger.debug("wsGroup shutdownGracefully");
                group.shutdownGracefully(0, 1, TimeUnit.SECONDS).syncUninterruptibly();
                group = null;
                logger.debug("wsGroup shutdown complete");
            }
            if (group != null && !group.isShuttingDown()) {
                logger.debug("httpGroup shutdownGracefully");
                group.shutdownGracefully(0, 1, TimeUnit.SECONDS).syncUninterruptibly();
                group = null;
                logger.debug("httpGroup shutdown complete");
            }
        });
        shutDownGroup.shutdownGracefully(0, 5, TimeUnit.SECONDS).syncUninterruptibly();
        shutDownGroup = null;
        logger.debug("... destroyed");
    }

    protected String buildURL(String path, List<HttpParam> parametersQuery, boolean withAddress) {
        StringBuilder uriBuilder = new StringBuilder();
        if (withAddress) {
            uriBuilder.append(baseUri);
        } else {
            uriBuilder.append(baseUri.getPath());
        }
        uriBuilder.append("ari");
        uriBuilder.append(path);
        boolean first = true;
        if (parametersQuery != null) {
            for (HttpParam hp : parametersQuery) {
                if (hp.getValue() != null && !hp.getValue().isEmpty()) {
                    if (first) {
                        uriBuilder.append("?");
                        first = false;
                    } else {
                        uriBuilder.append("&");
                    }
                    uriBuilder.append(hp.getName());
                    uriBuilder.append("=");
                    uriBuilder.append(ARIEncoder.encodeUrl(hp.getValue()));
                }
            }
        }
        return uriBuilder.toString();
    }

    // Factory for WS handshakes
    protected WebSocketClientHandshaker getWsHandshake(String path, List<HttpParam> parametersQuery) throws URISyntaxException {
        String url = buildURL(path, parametersQuery, true);
        if (url.regionMatches(true, 0, HTTP, 0, 4)) {
            // http(s):// -> ws(s)://
            url = "ws" + url.substring(4);
        }
        URI uri = new URI(url);
        HttpHeaders headers = new DefaultHttpHeaders();
        headers.set(HttpHeaderNames.AUTHORIZATION, this.auth);
        return WebSocketClientHandshakerFactory.newHandshaker(
                uri, WebSocketVersion.V13, null, false, headers);
    }

    // Build the HTTP request based on the given parameters
    private HttpRequest buildRequest(String path, String method, List<HttpParam> parametersQuery, String body) {
        String url = buildURL(path, parametersQuery, false);
        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.valueOf(method), url);
        if (body != null && !body.isEmpty()) {
            ByteBuf bbuf = Unpooled.copiedBuffer(body, ARIEncoder.ENCODING);
            request.headers().add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, bbuf.readableBytes());
            request.content().clear().writeBytes(bbuf);
        }
        request.headers().set(HttpHeaderNames.HOST, baseUri.getHost());
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        request.headers().set(HttpHeaderNames.AUTHORIZATION, this.auth);
        HTTPLogger.traceRequest(request, body);
        return request;
    }

    private RestException makeException(HttpResponseStatus status, String response, List<HttpResponse> errors) {

        if (status == null && response == null) {
            return new RestException("No HTTP Status and Response, timeout or client shutdown");
        } else if (status == null) {
            return new RestException("Error: " + response);
        }

        if (errors != null) {
            for (HttpResponse hr : errors) {
                if (hr.getCode() == status.code()) {
                    return new RestException(hr.getDescription(), response, status.code());
                }
            }
        }

        return new RestException(response, status.code());
    }

    // Synchronous HTTP action
    @Override
    public String httpActionSync(String uri, String method, List<HttpParam> parametersQuery,
                                 String body, List<HttpResponse> errors) throws RestException {
        NettyHttpClientHandler handler = httpActionSyncHandler(uri, method, parametersQuery, body, errors);
        return handler != null ? handler.getResponseText() : null;
    }

    // Synchronous HTTP action
    @Override
    public byte[] httpActionSyncAsBytes(String uri, String method, List<HttpParam> parametersQuery,
                                        String body, List<HttpResponse> errors) throws RestException {
        NettyHttpClientHandler handler = httpActionSyncHandler(uri, method, parametersQuery, body, errors, true);
        return handler != null ? handler.getResponseBytes() : null;
    }

    private NettyHttpClientHandler httpActionSyncHandler(String uri, String method, List<HttpParam> parametersQuery,
                                                         String body, List<HttpResponse> errors) throws RestException {
        return httpActionSyncHandler(uri, method, parametersQuery, body, errors, false);
    }

    private NettyHttpClientHandler httpActionSyncHandler(String uri, String method, List<HttpParam> parametersQuery,
                                                         String body, List<HttpResponse> errors, boolean binary) throws RestException {
        HttpRequest request = buildRequest(uri, method, parametersQuery, body);
        logger.debug("Sync Action - {} to {}", request.method(), request.uri());
        Channel ch = poolAcquire().syncUninterruptibly().getNow();
        try {
            replaceAggregator(binary, ch);
            NettyHttpClientHandler handler = (NettyHttpClientHandler) ch.pipeline().get(HTTP_HANDLER);
            if (handler == null) return null;
            handler.reset();
            ch.writeAndFlush(request);
            logger.debug("Wait for response...");
            handler.waitForResponse(READ_TIMEOUT_SEC);
            if (handler.getException() != null) {
                logger.debug("got an error: {}", handler.getException().toString());
                throw new RestException(handler.getException());
            } else if (httpResponseOkay(handler.getResponseStatus())) {
                logger.debug("got OK response");
                return handler;
            } else {
                logger.debug("...done waiting");
                throw makeException(handler.getResponseStatus(), handler.getResponseText(), errors);
            }
        } finally {
            poolRelease(ch);
        }
    }

    private void replaceAggregator(boolean binary, Channel ch) {
        HttpObjectAggregator aggregator = (HttpObjectAggregator) ch.pipeline().get(HTTP_AGGREGATOR);
        if (aggregator != null) {
            if (binary && aggregator.maxContentLength() != MAX_HTTP_BIN_REQUEST) {
                logger.debug("Replace http-aggregator with larger content length...");
                ch.pipeline().replace(
                        HTTP_AGGREGATOR, HTTP_AGGREGATOR, new HttpObjectAggregator(MAX_HTTP_BIN_REQUEST));
            } else if (!binary && aggregator.maxContentLength() != MAX_HTTP_REQUEST) {
                logger.debug("Replace http-aggregator with smaller content length...");
                ch.pipeline().replace(
                        HTTP_AGGREGATOR, HTTP_AGGREGATOR, new HttpObjectAggregator(MAX_HTTP_REQUEST));
            }
        }
    }

    // Asynchronous HTTP action, response is passed to HttpResponseHandler
    @Override
    public void httpActionAsync(String uri, String method, List<HttpParam> parametersQuery,
                                String body, final List<HttpResponse> errors,
                                final HttpResponseHandler responseHandler, boolean binary) {

        final HttpRequest request = buildRequest(uri, method, parametersQuery, body);
        logger.debug("Async Action - {} to {}", request.method(), request.uri());
        // Get future channel
        Future<Channel> cf = poolAcquire();
        cf.addListener((FutureListener<Channel>) future1 -> {
            if (future1.isSuccess()) {
                Channel ch = future1.getNow();
                logger.debug("Channel, {}", ch);
                group.execute(responseHandler::onChReadyToWrite);
                replaceAggregator(binary, ch);
                final NettyHttpClientHandler handler = (NettyHttpClientHandler) ch.pipeline().get(HTTP_HANDLER);
                handler.reset();
                ch.writeAndFlush(request).addListener(future2 ->
                        group.execute(() -> {
                            try {
                                logger.debug("Wait for response...");
                                handler.waitForResponse(READ_TIMEOUT_SEC);
                                if (handler.getException() != null) {
                                    logger.debug("got an error: {}", handler.getException().toString());
                                    onFailure(responseHandler, new RestException(handler.getException()));
                                } else if (httpResponseOkay(handler.getResponseStatus())) {
                                    logger.debug("got OK response");
                                    if (binary) {
                                        responseHandler.onSuccess(handler.getResponseBytes());
                                    } else {
                                        responseHandler.onSuccess(handler.getResponseText());
                                    }
                                } else {
                                    logger.debug("...done waiting");
                                    onFailure(responseHandler, makeException(handler.getResponseStatus(), handler.getResponseText(), errors));
                                }
                            } finally {
                                poolRelease(ch);
                            }
                        })
                );
            } else {
                onFailure(responseHandler, future1.cause());
            }
        });
    }
    // WsClient implementation - connect to WebSocket server

    private void onFailure(HttpResponseHandler responseHandler, Throwable cause) {
        if (!group.isShuttingDown()) {
            group.execute(() -> responseHandler.onFailure(cause));
        }
    }

    @Override
    public WsClientConnection connect(final HttpResponseHandler callback, final String url, final List<HttpParam> lParamQuery) throws RestException {
        if (isWsConnected()) {
            return wsClientConnection;
        }
        try {
            WebSocketClientHandshaker handshake = getWsHandshake(url, lParamQuery);
            logger.debug("WS Connect uri: {}", handshake.uri());
            this.wsEventsUrl = url;
            this.wsEventsParamQuery = lParamQuery;
            this.wsHandler = new NettyWSClientHandler(handshake, this);
            this.wsCallback = callback;
            return connect(new Bootstrap(), callback);
        } catch (Exception e) {
            throw new RestException("WS Connection Error - " + e.getMessage(), e);
        }
    }

    protected WsClientConnection connect(Bootstrap wsBootStrap, final HttpResponseHandler callback) {
        wsBootStrap.group(group);
        bootstrapOptions(wsBootStrap);
        wsBootStrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                addSSLIfRequired(pipeline, baseUri);
                pipeline.addFirst("logger", new LoggingHandler(HTTPLogger.class, LogLevel.TRACE, ByteBufFormat.SIMPLE));
                pipeline.addLast(HTTP_CODEC, new HttpClientCodec());
                pipeline.addLast(HTTP_AGGREGATOR, new HttpObjectAggregator(MAX_HTTP_REQUEST));
                pipeline.addLast("ws-handler", wsHandler);
            }
        });
        wsConnectionTimeout = group.schedule(
                () -> reconnectWs(new RestException("WS Connect Timeout")), CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS);
        wsChannelFuture = wsBootStrap.connect(baseUri.getHost(), getPort());
        wsFuture = new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    logger.debug("HTTP connected, waiting for WS Upgrade...");
                    wsHandler.handshakeFuture().addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            // cancel the connection timeout
                            cancelWsConnectionTimeout();
                            if (future.isSuccess()) {
                                logger.debug("WS connected...");
                                // start a ping and reset reconnect counter
                                startPing();
                                reconnectCount.set(0);
                                if (!group.isShuttingDown()) {
                                    group.execute(callback::onChReadyToWrite);
                                }
                            } else {
                                if (future.cause() != null) {
                                    logger.error("WS Upgrade Error - {}", future.cause().getMessage(), future.cause());
                                    reconnectWs(future.cause());
                                } else {
                                    logger.error("WS Upgrade Error - Unknown");
                                    reconnectWs(new RestException("WS Upgrade Error - Unknown"));
                                }
                            }
                        }
                    });
                } else {
                    cancelWsConnectionTimeout();
                    if (future.cause() != null) {
                        logger.error("WS/HTTP Connection Error - {}", future.cause().getMessage(), future.cause());
                        reconnectWs(future.cause());
                    } else {
                        logger.error("WS/HTTP Connection Error - Unknown");
                        reconnectWs(new RestException("WS/HTTP Connection Error - Unknown"));
                    }
                }
            }
        };
        wsChannelFuture.addListener(wsFuture);

        // Provide disconnection handle to client
        return createWsClientConnection();
    }

    private void cancelWsConnectionTimeout() {
        if (wsConnectionTimeout != null) {
            wsConnectionTimeout.cancel(true);
            wsConnectionTimeout = null;
        }
    }

    private void startPing() {
        if (wsPingTimer == null) {
            pongFailureCount = 0;
            wsPingTimer = group.scheduleAtFixedRate(() -> {
                if (isWsConnected() && (System.currentTimeMillis() - wsCallback.getLastResponseTime()) > 15000) {
                    WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer("ari4j".getBytes(ARIEncoder.ENCODING)));
                    logger.debug("Send Ping at {}", System.currentTimeMillis());
                    wsChannelFuture.channel().writeAndFlush(frame);
                    boolean noPong = true;
                    for (int i = 0; i < 10; i++) {
                        if (wsHandler != null && wsHandler.isShuttingDown()) {
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {//NOSONAR
                            // probably from the reconnect, so stop running...
                            return;
                        }
                        if ((System.currentTimeMillis() - lastPong) < 10000) {
                            logger.debug("Pong at {}", lastPong);
                            pongFailureCount = 0;
                            noPong = false;
                            break;
                        } else {
                            logger.warn("No Pong at {}", System.currentTimeMillis());
                        }
                    }
                    if (noPong && wsHandler != null && !wsHandler.isShuttingDown()) {
                        pongFailureCount++;
                        if (pongFailureCount >= 1) {
                            logger.warn("No Ping response from server, reconnect...");
                            reconnectWs(new RestException("No Ping response from server"));
                        }
                    }
                }
            }, 1, pingPeriod, pingTimeUnit);
        }
    }

    private WsClientConnection createWsClientConnection() {
        if (this.wsClientConnection == null) {
            this.wsClientConnection = new WsClientConnection() {

                @Override
                public void disconnect() throws RestException {
                    Channel ch = wsChannelFuture.channel();
                    if (ch != null) {
                        // if connected send CloseWebSocketFrame as NettyWSClientHandler will close the connection when the server responds to it
                        // only when not shutdown yet, so we don't try to send another close frame
                        if (!wsHandler.isShuttingDown()) {
                            logger.debug("Send CloseWebSocketFrame ...");
                            ch.writeAndFlush(new CloseWebSocketFrame());
                        }
                        wsHandler.setShuttingDown(true);
                        // if the server is no longer there then close any way
                        ch.close();
                    }
                    wsChannelFuture.removeListener(wsFuture);
                    wsChannelFuture.cancel(true);
                }
            };
        }
        return this.wsClientConnection;
    }

    /**
     * Checks if a response is okay.
     * All 2XX responses are supposed to be okay.
     *
     * @param status http status
     * @return whether it is a 2XX code or not (error!)
     */
    private boolean httpResponseOkay(HttpResponseStatus status) {
        return HttpResponseStatus.OK.equals(status)
                || HttpResponseStatus.NO_CONTENT.equals(status)
                || HttpResponseStatus.ACCEPTED.equals(status)
                || HttpResponseStatus.CREATED.equals(status);
    }

    public void reconnectWs(Throwable cause) {
        // send the disconnect callback
        if (!group.isShuttingDown()) {
            group.execute(() -> wsCallback.onDisconnect());
        }
        // cancel the ping timer
        if (wsPingTimer != null) {
            wsPingTimer.cancel(true);
            wsPingTimer = null;
        }

        if (!autoReconnect || (maxReconnectCount > -1 && reconnectCount.get() >= maxReconnectCount)) {
            logger.warn("Cannot connect: {} - executing failure callback", cause.getMessage());
            if (!group.isShuttingDown()) {
                group.execute(() -> wsCallback.onFailure(cause));
            }
            return;
        }

        // if not shutdown reconnect, note the check not on the shutDownGroup
        if (!group.isShuttingDown()) {
            // schedule reconnect after a 2,5,10 seconds
            long[] timeouts = {2L, 5L, 10L};
            long timeout = reconnectCount.get() >= timeouts.length ? timeouts[timeouts.length - 1] : timeouts[reconnectCount.get()];
            reconnectCount.incrementAndGet();
            logger.error("WS Connect Error: {}, reconnecting in {} seconds... try: {}", cause.getMessage(), timeout, reconnectCount.get());
            shutDownGroup.schedule(() -> {
                try {
                    // 1st close up
                    wsClientConnection.disconnect();
                    // then connect again
                    connect(wsCallback, wsEventsUrl, wsEventsParamQuery);
                } catch (RestException e) {
                    group.execute(() -> wsCallback.onFailure(e));
                }
            }, timeout, TimeUnit.SECONDS);
        }
    }

    public void onWSResponseReceived() {
        if (!group.isShuttingDown()) {
            group.execute(() -> wsCallback.onResponseReceived());
        }
    }

    public void onWSSuccess(String text) {
        if (!group.isShuttingDown()) {
            group.execute(() -> wsCallback.onSuccess(text));
        }
    }

    public void onWSFailure(Throwable cause) {
        if (!group.isShuttingDown()) {
            group.execute(() -> wsCallback.onFailure(cause));
        }
    }

    public void onWSPong() {
        lastPong = System.currentTimeMillis();
    }

    /**
     * The ability to turn on/off the websocket auto reconnect, defaulted to on
     *
     * @param val auto reconnect
     */
    public static void setAutoReconnect(boolean val) {
        NettyHttpClient.autoReconnect = val;
    }

    /**
     * The ability to provide a custom SSL Contect for
     *
     * @param sslContext the ssl context
     */
    public static void setSslContext(SslContext sslContext) {
        NettyHttpClient.sslContext = sslContext;
    }

    /**
     * Checks if websocket is connected
     *
     * @return true when connected, false otherwise
     */
    public boolean isWsConnected() {
        return wsClientConnection != null && wsHandler != null && !wsHandler.isShuttingDown() && wsChannelFuture != null &&
                !wsChannelFuture.isCancelled() && wsChannelFuture.channel() != null && wsChannelFuture.channel().isActive();
    }

    /**
     * Sets maximal reconnect count
     *
     * @param count max number of reconnect attempts, -1 for infinite reconnecting
     */
    public void setMaxReconnectCount(int count) {
        maxReconnectCount = count;
    }
}
