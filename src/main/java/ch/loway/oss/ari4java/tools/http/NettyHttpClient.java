package ch.loway.oss.ari4java.tools.http;

import ch.loway.oss.ari4java.tools.HttpResponse;
import ch.loway.oss.ari4java.tools.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.base64.Base64;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * HTTP and WebSocket client implementation based on netty.io.
 * <p>
 * Threading is handled by NioEventLoopGroup, which selects on multiple
 * sockets and provides threads to handle the events on the sockets.
 * <p>
 * Requires netty-all-4.0.12.Final.jar
 *
 * @author mwalton
 */
public class NettyHttpClient implements HttpClient, WsClient, WsClientAutoReconnect {

    public static final int CONNECTION_TIMEOUT_SEC = 10;
    public static final int READ_TIMEOUT_SEC = 30;
    public static final int MAX_HTTP_REQUEST = 16 * 1024 * 1024; // 16MB
    public static final int MAX_HTTP_BIN_REQUEST = 150 * 1024 * 1024; // 150MB

    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static final String HTTP_CODEC = "http-codec";
    private static final String HTTP_AGGREGATOR = "http-aggregator";
    private static final String HTTP_HANDLER = "http-handler";

    private Logger logger = LoggerFactory.getLogger(NettyHttpClient.class);

    protected Bootstrap httpBootstrap;
    protected URI baseUri;
    private EventLoopGroup group;
    private EventLoopGroup shutDownGroup;
    protected String auth;

    private HttpResponseHandler wsCallback;
    private String wsEventsUrl;
    private List<HttpParam> wsEventsParamQuery;
    private WsClientConnection wsClientConnection;
    private int reconnectCount = -1;
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

    public NettyHttpClient() {
        group = new NioEventLoopGroup();
        shutDownGroup = new NioEventLoopGroup();
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
        this.auth = "Basic " + Base64.encode(Unpooled.copiedBuffer((username + ":" + password), ARIEncoder.ENCODING)).toString(ARIEncoder.ENCODING);
        initHttpBootstrap();
    }

    protected void initHttpBootstrap() {
        if (httpBootstrap == null) {
            // Bootstrap is the factory for HTTP connections
            logger.debug("Bootstrap with\n" +
                            " connection timeout: {},\n" +
                            " read timeout: {},\n" +
                            " aggregator max-length: {}",
                    CONNECTION_TIMEOUT_SEC,
                    READ_TIMEOUT_SEC,
                    MAX_HTTP_REQUEST);
            httpBootstrap = new Bootstrap();
            bootstrapOptions(httpBootstrap);
            httpBootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    addSSLIfRequired(pipeline, baseUri);
                    pipeline.addLast("read-timeout", new ReadTimeoutHandler(READ_TIMEOUT_SEC));
                    pipeline.addLast(HTTP_CODEC, new HttpClientCodec());
                    pipeline.addLast(HTTP_AGGREGATOR, new HttpObjectAggregator(MAX_HTTP_REQUEST));
                    pipeline.addLast(HTTP_HANDLER, new NettyHttpClientHandler());
                }
            });
        }
    }

    private void bootstrapOptions(Bootstrap bootStrap) {
        bootStrap.group(group);
        bootStrap.channel(NioSocketChannel.class);
        bootStrap.option(ChannelOption.TCP_NODELAY, true);
        bootStrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootStrap.option(ChannelOption.SO_REUSEADDR, false);
        bootStrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECTION_TIMEOUT_SEC * 1000);
    }

    private synchronized static void addSSLIfRequired(ChannelPipeline pipeline, URI baseUri) throws SSLException {
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

    protected ChannelFuture httpConnect() {
        logger.debug("HTTP Connect uri: {}", baseUri.toString());
        return httpBootstrap.connect(baseUri.getHost(), getPort());
    }

    @Override
    public void destroy() {
        logger.debug("destroy...");
        // use a different event group to execute the shutdown to avoid deadlocks
        shutDownGroup.schedule(new Runnable() {
            @Override
            public void run() {
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
                    logger.debug("group shutdownGracefully");
                    group.shutdownGracefully(0, 1, TimeUnit.SECONDS).syncUninterruptibly();
                    group = null;
                    logger.debug("group shutdown complete");
                }
            }
        }, 250L, TimeUnit.MILLISECONDS);
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
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        request.headers().set(HttpHeaderNames.AUTHORIZATION, this.auth);
        HTTPLogger.traceRequest(request, body);
        return request;
    }

    private RestException makeException(HttpResponseStatus status, String response, List<HttpResponse> errors) {

        if (status == null && response == null) {
            return new RestException("Client Shutdown");
        } else if (status == null) {
            return new RestException("Client Shutdown: " + response);
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
        return handler.getResponseText();
    }

    // Synchronous HTTP action
    @Override
    public byte[] httpActionSyncAsBytes(String uri, String method, List<HttpParam> parametersQuery,
                                        String body, List<HttpResponse> errors) throws RestException {
        NettyHttpClientHandler handler = httpActionSyncHandler(uri, method, parametersQuery, body, errors, true);
        return handler.getResponseBytes();
    }

    private NettyHttpClientHandler httpActionSyncHandler(String uri, String method, List<HttpParam> parametersQuery,
                                                         String body, List<HttpResponse> errors) throws RestException {
        return httpActionSyncHandler(uri, method, parametersQuery, body, errors, false);
    }

    private NettyHttpClientHandler httpActionSyncHandler(String uri, String method, List<HttpParam> parametersQuery,
                                                         String body, List<HttpResponse> errors, boolean binary) throws RestException {
        HttpRequest request = buildRequest(uri, method, parametersQuery, body);
        logger.debug("Sync Action - {} to {}", request.method().toString(), request.uri());
        Channel ch = httpConnect().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    logger.debug("HTTP connected");
                    replaceAggregator(binary, future.channel());
                } else if (future.cause() != null) {
                    logger.error("HTTP Connection Error - {}", future.cause().getMessage(), future.cause());
                } else {
                    logger.error("HTTP Connection Error - Unknown");
                }
            }
        }).syncUninterruptibly().channel();
        NettyHttpClientHandler handler = (NettyHttpClientHandler) ch.pipeline().get(HTTP_HANDLER);
        ch.writeAndFlush(request);
        ch.closeFuture().syncUninterruptibly();
        if (handler.getException() != null) {
            throw new RestException(handler.getException());
        } else if (httpResponseOkay(handler.getResponseStatus())) {
            return handler;
        } else {
            throw makeException(handler.getResponseStatus(), handler.getResponseText(), errors);
        }
    }

    private void replaceAggregator(boolean binary, Channel ch) {
        if (binary) {
            logger.debug("Is Binary, replace http-aggregator ...");
            ch.pipeline().replace(
                    HTTP_AGGREGATOR, HTTP_AGGREGATOR, new HttpObjectAggregator(MAX_HTTP_BIN_REQUEST));
        }
    }

    // Asynchronous HTTP action, response is passed to HttpResponseHandler
    @Override
    public void httpActionAsync(String uri, String method, List<HttpParam> parametersQuery,
                                String body, final List<HttpResponse> errors,
                                final HttpResponseHandler responseHandler, boolean binary) {

        final HttpRequest request = buildRequest(uri, method, parametersQuery, body);
        logger.debug("Async Action - {} to {}", request.method().toString(), request.uri());
        // Get future channel
        ChannelFuture cf = httpConnect();
        cf.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    logger.debug("HTTP connected");
                    Channel ch = future.channel();
                    replaceAggregator(binary, ch);
                    responseHandler.onChReadyToWrite();
                    ch.writeAndFlush(request);
                    ch.closeFuture().addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            responseHandler.onResponseReceived();
                            if (future.isSuccess()) {
                                NettyHttpClientHandler handler = (NettyHttpClientHandler) future.channel().pipeline().get(HTTP_HANDLER);
                                if (handler.getException() != null) {
                                    responseHandler.onFailure(new RestException(handler.getException()));
                                } else if (httpResponseOkay(handler.getResponseStatus())) {
                                    if (binary) {
                                        responseHandler.onSuccess(handler.getResponseBytes());
                                    } else {
                                        responseHandler.onSuccess(handler.getResponseText());
                                    }
                                } else {
                                    responseHandler.onFailure(makeException(handler.getResponseStatus(), handler.getResponseText(), errors));
                                }
                            } else {
                                responseHandler.onFailure(future.cause());
                            }
                        }
                    });
                } else {
                    responseHandler.onFailure(future.cause());
                }
            }
        });
    }
    // WsClient implementation - connect to WebSocket server

    @Override
    public WsClientConnection connect(final HttpResponseHandler callback, final String url, final List<HttpParam> lParamQuery) throws RestException {
        if (isWsConnected()) {
            return wsClientConnection;
        }
        try {
            WebSocketClientHandshaker handshake = getWsHandshake(url, lParamQuery);
            logger.debug("WS Connect uri: {}", handshake.uri().toString());
            this.wsEventsUrl = url;
            this.wsEventsParamQuery = lParamQuery;
            this.wsHandler = new NettyWSClientHandler(handshake, callback, this);
            this.wsCallback = callback;
            return connect(new Bootstrap(), callback);
        } catch (Exception e) {
            throw new RestException("WS Connection Error - " + e.getMessage(), e);
        }
    }

    protected WsClientConnection connect(Bootstrap wsBootStrap, final HttpResponseHandler callback) {
        bootstrapOptions(wsBootStrap);
        wsBootStrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                addSSLIfRequired(pipeline, baseUri);
                pipeline.addLast(HTTP_CODEC, new HttpClientCodec());
                pipeline.addLast(HTTP_AGGREGATOR, new HttpObjectAggregator(MAX_HTTP_REQUEST));
                pipeline.addLast("ws-handler", wsHandler);
            }
        });
        wsConnectionTimeout = group.schedule(new Runnable() {
            @Override
            public void run() {
                reconnectWs(new RestException("WS Connect Timeout"));
            }
        }, CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS);
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
                                reconnectCount = 0;
                                callback.onChReadyToWrite();
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
            wsPingTimer = group.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
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
                }
            }, 1, pingPeriod, pingTimeUnit);
        }
    }

    private WsClientConnection createWsClientConnection() {
        if (this.wsClientConnection == null) {
            this.wsClientConnection = new WsClientConnection() {

                @Override
                public void disconnect() throws RestException {
                    wsHandler.setShuttingDown(true);
                    Channel ch = wsChannelFuture.channel();
                    if (ch != null) {
                        // if connected send CloseWebSocketFrame as NettyWSClientHandler will close the connection when the server responds to it
                        if (reconnectCount == 0) {
                            logger.debug("Send CloseWebSocketFrame ...");
                            // set to -1 so we don't try send another close frame
                            reconnectCount = -1;
                            ch.writeAndFlush(new CloseWebSocketFrame());
                        }
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
     * @param status
     * @return whether it is a 2XX code or not (error!)
     */
    private boolean httpResponseOkay(HttpResponseStatus status) {

        if (HttpResponseStatus.OK.equals(status)
                || HttpResponseStatus.NO_CONTENT.equals(status)
                || HttpResponseStatus.ACCEPTED.equals(status)
                || HttpResponseStatus.CREATED.equals(status)) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void reconnectWs(Throwable cause) {
        // cancel the ping timer
        if (wsPingTimer != null) {
            wsPingTimer.cancel(true);
            wsPingTimer = null;
        }

        if (!autoReconnect || (maxReconnectCount > -1 && reconnectCount >= maxReconnectCount)) {
            logger.warn("Cannot connect: {} - executing failure callback", cause.getMessage());
            wsCallback.onFailure(cause);
            return;
        }

        // if not shutdown reconnect, note the check not on the shutDownGroup
        if (!group.isShuttingDown()) {
            // schedule reconnect after a 2,5,10 seconds
            long[] timeouts = {2L, 5L, 10L};
            long timeout = reconnectCount >= timeouts.length ? timeouts[timeouts.length - 1] : timeouts[reconnectCount];
            reconnectCount++;
            logger.error("WS Connect Error: {}, reconnecting in {} seconds... try: {}", cause.getMessage(), timeout, reconnectCount);
            shutDownGroup.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 1st close up
                        wsClientConnection.disconnect();
                        // then connect again
                        connect(wsCallback, wsEventsUrl, wsEventsParamQuery);
                    } catch (RestException e) {
                        wsCallback.onFailure(e);
                    }
                }
            }, timeout, TimeUnit.SECONDS);
        }
    }

    @Override
    public void pong() {
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
