package ch.loway.oss.ari4java.examples.comprehensive;

import ch.loway.oss.ari4java.tools.RestException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {

    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private final Asterisk asterisk;
    private final int port;
    private EventLoopGroup parentGroup;
    private EventLoopGroup workerGroup;

    public WebServer(int port, Asterisk asterisk) {
        this.port = port;
        this.asterisk = asterisk;
    }

    public void start() {
        logger.info("Starting HTTP Server on port {}", port);
        parentGroup = new MultiThreadIoEventLoopGroup(1, NioIoHandler.newFactory());
        workerGroup = new MultiThreadIoEventLoopGroup(5, NioIoHandler.newFactory());
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(parentGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new HttpRequestDecoder());
                            p.addLast(new HttpResponseEncoder());
                            p.addLast(new ServerHandler());
                        }
                    });

            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            stop();
        }
    }

    public void stop() {
        if (parentGroup != null) parentGroup.shutdownGracefully();
        if (workerGroup != null) workerGroup.shutdownGracefully();
    }

    class ServerHandler extends SimpleChannelInboundHandler<Object> {

        private String uri;
        private HttpMethod method;

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
            ctx.flush();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof HttpRequest request) {
                uri = request.uri();
                method = request.method();
                logger.debug("HttpRequest method: {}, uri: {}", method, uri);
            }
            if (msg instanceof LastHttpContent) {
                HttpResponseStatus status = HttpResponseStatus.NOT_FOUND;
                ByteBuf content = Unpooled.copiedBuffer("{\"message\": \"not found\"}", CharsetUtil.UTF_8);
                try {
                    if (HttpMethod.GET.equals(method)) {
                        String[] parts = uri.split("/");
                        logger.debug("action = {}", parts[1]);
                        if ("start".equals(parts[1]) && parts.length == 4) {
                            String id = asterisk.startCall(parts[2], parts[3]);
                            status = HttpResponseStatus.OK;
                            content = Unpooled.copiedBuffer("{\"message\": \"success\", \"id\": \"" + id + "\"}", CharsetUtil.UTF_8);
                        } else if ("end".equals(parts[1]) && parts.length == 3) {
                            asterisk.endCall(parts[2]);
                            status = HttpResponseStatus.OK;
                            content = Unpooled.copiedBuffer("{\"message\": \"success\"}", CharsetUtil.UTF_8);
                        } else {
                            logger.debug("invalid action");
                        }
                    }
                } catch (RestException e) {
                    status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
                    content = Unpooled.copiedBuffer("{\"message\": \"" + e.getMessage() + "\"}", CharsetUtil.UTF_8);
                }
                FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);
                httpResponse.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
                httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
                httpResponse.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());
                ctx.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            logger.warn("exceptionCaught", cause);
            ctx.close();
        }
    }

}
