package ch.loway.oss.ari4java.tools.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;

import ch.loway.oss.ari4java.tools.HttpParam;
import ch.loway.oss.ari4java.tools.HttpResponse;
import ch.loway.oss.ari4java.tools.HttpClient;
import ch.loway.oss.ari4java.tools.HttpResponseHandler;
import ch.loway.oss.ari4java.tools.RestException;
import ch.loway.oss.ari4java.tools.WsClient;

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
public class NettyHttpClient implements HttpClient, WsClient {

    public static final int MAX_HTTP_REQUEST_KB = 256;
    
    private Bootstrap bootStrap;
    private URI baseUri;
    private EventLoopGroup group;
    private String username;
    private String password;

    public void initialize(String baseUrl, String username, String password) throws URISyntaxException {
        this.username = username;
        this.password = password;
        group = new NioEventLoopGroup();
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
                pipeline.addLast("aggregator", new HttpObjectAggregator( MAX_HTTP_REQUEST_KB *1024));
                pipeline.addLast("http-handler", new NettyHttpClientHandler());
            }
        });
    }

    public void destroy() {
        if (group != null) {
            group.shutdownGracefully();
        }
    }

    // Factory for WS handshakes
    private WebSocketClientHandshaker getWsHandshake(String path, List<HttpParam> parametersQuery) {
        String queryString = String.format("?api_key=%s:%s", username, password);
        for (HttpParam hp : parametersQuery) {
            if (hp.value != null && !hp.value.isEmpty()) {
                queryString += "&" + hp.name + "=" + hp.value;
            }
        }
        try {
            URI uri = new URI(baseUri.toString().replaceFirst("http", "ws") + "ari" + path + queryString);
            return WebSocketClientHandshakerFactory.newHandshaker(
                    uri, WebSocketVersion.V13, null, false, null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Build the HTTP request based on the given parameters
    private HttpRequest buildRequest(String path, String method, List<HttpParam> parametersQuery, List<HttpParam> parametersForm) {
        String queryString = String.format("?api_key=%s:%s", username, password);
        for (HttpParam hp : parametersQuery) {
            if (hp.value != null && !hp.value.isEmpty()) {
                queryString += "&" + hp.name + "=" + hp.value;
            }
        }
        DefaultHttpRequest request = new DefaultHttpRequest(
		HttpVersion.HTTP_1_1, HttpMethod.valueOf(method), baseUri.getPath() + "ari" + path + queryString);
        //System.out.println(request.getUri());
        request.headers().set(HttpHeaders.Names.HOST, "localhost");
        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
        return request;
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
    public String httpActionSync(String uri, String method, List<HttpParam> parametersQuery, List<HttpParam> parametersForm,
            List<HttpResponse> errors) throws RestException {
        Channel ch;
        try {
            HttpRequest request = buildRequest(uri, method, parametersQuery, parametersForm);
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
        } catch (InterruptedException e) {
            throw new RestException(e);
        }
    }

    // Asynchronous HTTP action, response is passed to HttpResponseHandler
    @Override
    public void httpActionAsync(String uri, String method, List<HttpParam> parametersQuery, List<HttpParam> parametersForm,
            final List<HttpResponse> errors, final HttpResponseHandler responseHandler)
            throws RestException {
        final HttpRequest request = buildRequest(uri, method, parametersQuery, parametersForm);
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
    }
    // WsClient implementation - connect to WebSocket server

    @Override
    public WsClientConnection connect(final HttpResponseHandler callback, final String url, final List<HttpParam> lParamQuery) throws RestException {
        Bootstrap wsBootStrap = new Bootstrap();
        wsBootStrap.group(group);
        wsBootStrap.channel(NioSocketChannel.class);
        wsBootStrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("http-codec", new HttpClientCodec());
                pipeline.addLast("aggregator", new HttpObjectAggregator(MAX_HTTP_REQUEST_KB * 1024));
                pipeline.addLast("ws-handler", new NettyWSClientHandler(getWsHandshake(url, lParamQuery), callback));
            }
        });
        final ChannelFuture cf = wsBootStrap.connect(baseUri.getHost(), baseUri.getPort());
        cf.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    callback.onChReadyToWrite();
                    // Nothing - handshake future will activate later
					/*Channel ch = future.channel();
                    NettyWSClientHandler handler = (NettyWSClientHandler) ch.pipeline().get("ws-handler");
                    handler.handshakeFuture.sync();*/
                } else {
                    callback.onFailure(future.cause());
                }
            }
        });
        // Provide disconnection handle to client
        return new WsClientConnection() {

            @Override
            public void disconnect() throws RestException {
                Channel ch = cf.channel();
                if (ch != null) {
                    ch.writeAndFlush(new CloseWebSocketFrame());
                    // NettyWSClientHandler will close the connection when the server
                    // responds to the CloseWebSocketFrame.
                    try {
                        ch.closeFuture().sync();
                    } catch (InterruptedException e) {
                        throw new RestException(e);
                    }
                }
            }
        };
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
    
    
}
