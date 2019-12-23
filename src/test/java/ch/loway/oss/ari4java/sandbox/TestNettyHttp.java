package ch.loway.oss.ari4java.sandbox;

import ch.loway.oss.ari4java.ARI;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
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
import io.netty.handler.codec.http.HttpVersion;

import java.net.URI;

import ch.loway.oss.ari4java.generated.models.AsteriskInfo;
import ch.loway.oss.ari4java.generated.ari_0_0_1.models.AsteriskInfo_impl_ari_0_0_1;
import ch.loway.oss.ari4java.tools.BaseAriAction;
import ch.loway.oss.ari4java.tools.RestException;

public class TestNettyHttp {

    public static class HttpClientHandler extends SimpleChannelInboundHandler<Object> {

        public HttpClientHandler() {
        }


        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg)
                throws Exception {
            Channel ch = ctx.channel();

            if (msg instanceof FullHttpResponse) {
                FullHttpResponse response = (FullHttpResponse) msg;
                System.out.println("msg=" + response);
                BaseAriAction ba = new BaseAriAction();
                try {
                    AsteriskInfo m = ba.deserializeJson(response.content().toString(ARI.ENCODING), AsteriskInfo_impl_ari_0_0_1.class);
                    System.out.println(m);
                } catch (RestException e) {
                    e.printStackTrace();
                }
            } else {
            }

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();

            ctx.close();
        }

    }

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            URI uri = new URI("http://192.168.0.194:8088/");
            Bootstrap b = new Bootstrap();
            String protocol = uri.getScheme();
            if (!"http".equals(protocol)) {
                throw new IllegalArgumentException("Unsupported protocol: " + protocol);
            }
            final HttpClientHandler handler = new HttpClientHandler();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("http-codec", new HttpClientCodec());
                            pipeline.addLast("aggregator", new HttpObjectAggregator(8192));
                            pipeline.addLast("http-handler", handler);
                        }
                    });

            System.out.println("HTTP Client connecting");
            Channel ch = b.connect(uri.getHost(), uri.getPort()).sync().channel();

            HttpRequest request = new DefaultHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.GET, "/ari/asterisk/info?api_key=admin:admin");
            request.headers().set(HttpHeaders.Names.HOST, "localhost");
            request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
            ch.writeAndFlush(request);
            ch.closeFuture().sync();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
