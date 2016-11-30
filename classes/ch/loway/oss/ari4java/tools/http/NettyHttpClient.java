package ch.loway.oss.ari4java.tools.http;

import ch.loway.oss.ari4java.tools.*;
import ch.loway.oss.ari4java.tools.HttpResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.concurrent.ScheduledFuture;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * HTTP and WebSocket client implementation based on netty.io.
 * 
 * Threading is handled by NioEventLoopGroup, which selects on multiple
 * sockets and provides threads to handle the events on the sockets.
 * 
 * Requires netty-all-4.0.12.Final.jar
 * 
 * @author mwalton
 *
 */
public class NettyHttpClient implements HttpClient, WsClient, WsClientAutoReconnect {

    public static final int MAX_HTTP_REQUEST_KB = 256;
    
    private Bootstrap bootStrap;
    private URI baseUri;
    private EventLoopGroup group;
    private EventLoopGroup shutDownGroup;
    private String username;
    private String password;

    private HttpResponseHandler wsCallback;
    private String wsEventsUrl;
    private List<HttpParam> wsEventsParamQuery;
    private WsClientConnection wsClientConnection;
    private int reconnectCount = 0;
    private ChannelFuture wsChannelFuture;
    private ScheduledFuture<?> wsPingTimer = null;
    private NettyWSClientHandler wsHandler;
    private ChannelFutureListener wsFuture;

    public NettyHttpClient() {
        group = new NioEventLoopGroup();
        shutDownGroup = new NioEventLoopGroup();
    }

    public void initialize(String baseUrl, String username, String password) throws URISyntaxException {
        this.username = username;
        this.password = password;
        baseUri = new URI(baseUrl);
        String protocol = baseUri.getScheme();
        if (!"http".equals(protocol)) {
            throw new IllegalArgumentException("Unsupported protocol: " + protocol);
        }
        // Bootstrap is the factory for HTTP connections
        bootStrap = new Bootstrap();
        bootStrap.group(group);
        bootStrap.channel(NioSocketChannel.class);
        bootStrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("http-codec", new HttpClientCodec());
                pipeline.addLast("aggregator", new HttpObjectAggregator( MAX_HTTP_REQUEST_KB * 1024));
                pipeline.addLast("http-handler", new NettyHttpClientHandler());
            }
        });
    }

    public void destroy() {
        // use a different event group to execute the shutdown to avoid deadlocks
        shutDownGroup.schedule(new Runnable() {
            @Override
            public void run() {
                if (wsClientConnection != null) {
                    try {
                        wsClientConnection.disconnect();
                    } catch (RestException e) {
                        // not bubbling exception up, just ignoring
                    }
                }
                if (group != null && !group.isShuttingDown()) {
                    group.shutdownGracefully(5, 10, TimeUnit.SECONDS).syncUninterruptibly();
                    group = null;
                }
            }
        }, 250L, TimeUnit.MILLISECONDS);
    }

    private String buildURL(String path, List<HttpParam> parametersQuery) throws UnsupportedEncodingException {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(baseUri.getPath());
        uriBuilder.append("ari");
        uriBuilder.append(path);
        uriBuilder.append("?api_key=");
        uriBuilder.append(URLEncoder.encode(username, "UTF-8"));
        uriBuilder.append(":");
        uriBuilder.append(URLEncoder.encode(password, "UTF-8"));
        if (parametersQuery != null) {
            for (HttpParam hp : parametersQuery) {
                if (hp.value != null && !hp.value.isEmpty()) {
                    uriBuilder.append("&");
                    uriBuilder.append(hp.name);
                    uriBuilder.append("=");
                    uriBuilder.append(URLEncoder.encode(hp.value, "UTF-8"));
                }
            }
        }
        return uriBuilder.toString();
    }

    // Factory for WS handshakes
    private WebSocketClientHandshaker getWsHandshake(String path, List<HttpParam> parametersQuery) throws UnsupportedEncodingException {
        String url = buildURL(path, parametersQuery);
        try {
            URI uri = new URI(url.replaceFirst("http", "ws"));
            return WebSocketClientHandshakerFactory.newHandshaker(
                    uri, WebSocketVersion.V13, null, false, null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Build the HTTP request based on the given parameters
    private HttpRequest buildRequest(String path, String method, List<HttpParam> parametersQuery, List<HttpParam> parametersForm, List<HttpParam> parametersBody) throws UnsupportedEncodingException {
        String url = buildURL(path, parametersQuery);
        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.valueOf(method), url);
        //System.out.println(request.getUri());
        if (parametersBody != null && !parametersBody.isEmpty()) {
            String vars = makeBodyVariables(parametersBody);
            ByteBuf bbuf = Unpooled.copiedBuffer(vars, StandardCharsets.UTF_8);

            request.headers().add(HttpHeaders.Names.CONTENT_TYPE, "application/json");
            request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, bbuf.readableBytes());
            request.content().clear().writeBytes(bbuf);
        }
        request.headers().set(HttpHeaders.Names.HOST, "localhost");
        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
        return request;
    }

    private String makeBodyVariables(List<HttpParam> variables) {
        StringBuilder varBuilder = new StringBuilder();
        varBuilder.append("{").append("\"variables\": {");
        Iterator<HttpParam> entryIterator = variables.iterator();
        while(entryIterator.hasNext()) {
            HttpParam param = entryIterator.next();
            varBuilder.append("\"").append(param.name).append("\"").append(": ").append("\"").append(param.value).append("\"");
            if (entryIterator.hasNext()) {
                varBuilder.append(",");
            }
        }
        varBuilder.append("}}");

        return varBuilder.toString();
    }

    private RestException makeException(HttpResponseStatus status, String response, List<HttpResponse> errors) {
        
        if (status == null ) {            
            return new RestException("Shutdown: " + response);
        }

        for (HttpResponse hr : errors) {
            if (hr.code == status.code()) {
                return new RestException(hr.description);
            }
        }
        return new RestException(response);
    }

    // Synchronous HTTP action
    @Override
    public String httpActionSync(String uri, String method, List<HttpParam> parametersQuery, List<HttpParam> parametersForm, List<HttpParam> parametersBody,
            List<HttpResponse> errors) throws RestException {
        Channel ch;
        try {
            HttpRequest request = buildRequest(uri, method, parametersQuery, parametersForm, parametersBody);
            //handler.reset();
            ch = bootStrap.connect(baseUri.getHost(), baseUri.getPort()).sync().channel();
            NettyHttpClientHandler handler = (NettyHttpClientHandler) ch.pipeline().get("http-handler");
            ch.writeAndFlush(request);
            ch.closeFuture().sync();
            if ( httpResponseOkay(handler.getResponseStatus())) {
                return handler.getResponseText();
            } else {
                throw makeException(handler.getResponseStatus(), handler.getResponseText(), errors);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RestException(e);
        } catch (InterruptedException e) {
            throw new RestException(e);
        }
    }

    // Asynchronous HTTP action, response is passed to HttpResponseHandler
    @Override
    public void httpActionAsync(String uri, String method, List<HttpParam> parametersQuery, List<HttpParam> parametersForm, List<HttpParam> parametersBody,
            final List<HttpResponse> errors, final HttpResponseHandler responseHandler)
            throws RestException {
        try {
            final HttpRequest request = buildRequest(uri, method, parametersQuery, parametersForm, parametersBody);
            // Get future channel
            ChannelFuture cf = bootStrap.connect(baseUri.getHost(), baseUri.getPort());
            cf.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        Channel ch = future.channel();
                        responseHandler.onChReadyToWrite();
                        ch.writeAndFlush(request);
                        ch.closeFuture().addListener(new ChannelFutureListener() {

                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception {
                                responseHandler.onResponseReceived();
                                if (future.isSuccess()) {
                                    NettyHttpClientHandler handler = (NettyHttpClientHandler) future.channel().pipeline().get("http-handler");
                                    HttpResponseStatus rStatus = handler.getResponseStatus();

                                    if ( httpResponseOkay(rStatus)) {
                                        responseHandler.onSuccess(handler.getResponseText());
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
        } catch (UnsupportedEncodingException e) {
            throw new RestException(e);
        }
    }
    // WsClient implementation - connect to WebSocket server

    @Override
    public WsClientConnection connect(final HttpResponseHandler callback, final String url, final List<HttpParam> lParamQuery) throws RestException {

        this.wsCallback = callback;
        this.wsEventsUrl = url;
        this.wsEventsParamQuery = lParamQuery;
        try {
            this.wsHandler = new NettyWSClientHandler(getWsHandshake(url, lParamQuery), callback, this);
        } catch (UnsupportedEncodingException e) {
            throw new RestException(e);
        }

        Bootstrap wsBootStrap = new Bootstrap();
        wsBootStrap.group(group);
        wsBootStrap.channel(NioSocketChannel.class);
        wsBootStrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("http-codec", new HttpClientCodec());
                pipeline.addLast("aggregator", new HttpObjectAggregator(MAX_HTTP_REQUEST_KB * 1024));
                pipeline.addLast("ws-handler", wsHandler);
            }
        });

        wsChannelFuture = wsBootStrap.connect(baseUri.getHost(), baseUri.getPort());
        wsFuture = new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    callback.onChReadyToWrite();
                    // reset the reconnect counter on successful connect
                    reconnectCount = 0;
                } else {
                    if (reconnectCount >= 10) {
                        callback.onFailure(future.cause());
                    } else {
                        reconnectWs();
                    }
                }
            }
        };
        wsChannelFuture.addListener(wsFuture);

        // start a ws ping schedule
        startPing();

        // Provide disconnection handle to client
        return createWsClientConnection();
    }

    private void startPing() {
        if (wsPingTimer == null) {
            wsPingTimer = group.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    if (System.currentTimeMillis() - wsCallback.getLastResponseTime() > 15000) {
                        if (!wsChannelFuture.isCancelled() && wsChannelFuture.channel() != null) {
                            WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer("ari4j".getBytes( StandardCharsets.UTF_8 )));
                            wsChannelFuture.channel().writeAndFlush(frame);
                        }
                    }
                }
            }, 5, 5, TimeUnit.MINUTES);
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
                        // NettyWSClientHandler will close the connection when the server
                        // responds to the CloseWebSocketFrame.
                        ch.writeAndFlush(new CloseWebSocketFrame());
                        // if the server is no longer there then close any way
                        ch.close();
                    }
                    wsChannelFuture.removeListener(wsFuture);
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
    public void reconnectWs() {
        // cancel the ping timer
        if (wsPingTimer != null) {
            wsPingTimer.cancel(false);
            wsPingTimer = null;
        }
        // if not shutdown reconnect, note the check not on the shutDownGroup
        if (!group.isShuttingDown()) {
            // schedule reconnect after a 2,5,10 seconds
            long[] timeouts = {2L, 5L, 10L};
            reconnectCount++;
            shutDownGroup.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 1st close up
                        wsClientConnection.disconnect();
                        System.err.println(System.currentTimeMillis() + " ** connecting...  try:" + reconnectCount + " ++");
                        // then connect again
                        connect(wsCallback, wsEventsUrl, wsEventsParamQuery);
                    } catch (RestException e) {
                        wsCallback.onFailure(e);
                    }
                }
            }, reconnectCount >= timeouts.length ? timeouts[timeouts.length - 1] : timeouts[reconnectCount], TimeUnit.SECONDS);
        }
    }
}
