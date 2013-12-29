
package ch.loway.oss.ari4java.connector.netty;


import ch.loway.oss.ari4java.tools.BaseAriAction.HttpParam;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.base64.Base64;
import io.netty.handler.codec.http.ClientCookieEncoder;
import io.netty.handler.codec.http.DefaultCookie;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lenz
 */
public class BlockingHttpClient {

public HttpClientHandler httpRequestViaNetty(URI uri, HttpMethod method, String login, String password, List<HttpParam> queryParms) throws InterruptedException {

        String host = uri.getHost();
        int port = 8088;

        HttpClientHandler clientBuffer = new HttpClientHandler();
        
        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();            
            b.group(group).channel(NioSocketChannel.class).handler(new HttpClientInitializer( clientBuffer ));

            // Make the connection attempt.
            Channel ch = b.connect(host, port).sync().channel();

            // encode the GET parameters
             QueryStringEncoder encoder = new QueryStringEncoder( uri.getRawPath() );
             for ( HttpParam p: queryParms ) {
                 encoder.addParam(p.name, p.value);
             }

            // Prepare the HTTP request.
            HttpRequest request = new DefaultHttpRequest(
                    HttpVersion.HTTP_1_1, method, encoder.toString() );
            request.headers().set(HttpHeaders.Names.HOST, host);
            request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
            //request.headers().set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);

            String authString = login + ":" + password;
            ByteBuf authChannelBuffer = Unpooled.copiedBuffer(authString, CharsetUtil.UTF_8);
            ByteBuf encodedAuthChannelBuffer = Base64.encode(authChannelBuffer);

            request.headers().set(HttpHeaders.Names.AUTHORIZATION,
                    "Basic " + encodedAuthChannelBuffer.toString(CharsetUtil.UTF_8));

            // Set some example cookies.
            request.headers().set(
                    HttpHeaders.Names.COOKIE,
                    ClientCookieEncoder.encode(
                    new DefaultCookie("X-library", "Ari4Java/0.0.0" ),
                    new DefaultCookie("another-cookie", "bar")));

            // Send the HTTP request.
            ch.writeAndFlush(request);

                // Wait for the server to close the connection.
                ch.closeFuture().sync();
        } finally {
            // Shut down executor threads to exit.
            group.shutdownGracefully();
        }

        return clientBuffer;
    }

    public static class HttpClientInitializer extends ChannelInitializer<SocketChannel> {

        HttpClientHandler handler = null;

        public HttpClientInitializer(HttpClientHandler myHandler) {
            handler = myHandler;
        }


        @Override
        public void initChannel(SocketChannel ch) throws Exception {
            // Create a default pipeline implementation.
            ChannelPipeline p = ch.pipeline();

            p.addLast("log", new LoggingHandler(LogLevel.INFO));

            p.addLast("codec", new HttpClientCodec());

            // Remove the following line if you don't want automatic content decompression.
            //p.addLast("inflater", new HttpContentDecompressor());

            // Uncomment the following line if you don't want to handle HttpChunks.
            //p.addLast("aggregator", new HttpObjectAggregator(1048576));

            p.addLast("handler", handler );
        }
    }

    public static class HttpClientHandler extends SimpleChannelInboundHandler<HttpObject> {

        public Map<String,String> httpHeaders = new HashMap<String,String>();
        public int responseCode = 0;
        public StringBuilder sb = new StringBuilder();


        @Override
        protected void channelRead0(ChannelHandlerContext chc, HttpObject msg) throws Exception {
            if (msg instanceof HttpResponse) {
                HttpResponse response = (HttpResponse) msg;

                System.out.println("STATUS: " + response.getStatus());
                System.out.println("VERSION: " + response.getProtocolVersion());
                System.out.println();

                responseCode = response.getStatus().code();

                if (!response.headers().isEmpty()) {
                    for (String name : response.headers().names()) {
                        for (String value : response.headers().getAll(name)) {
                            System.out.println("HEADER: " + name + " = " + value);
                        }
                    }
                    System.out.println();
                }

                if (HttpHeaders.isTransferEncodingChunked(response)) {
                    System.out.println("CHUNKED CONTENT {");
                } else {
                    System.out.println("CONTENT {");
                }
            }
            if (msg instanceof HttpContent) {
                HttpContent content = (HttpContent) msg;



//                System.out.print(content.content().toString(CharsetUtil.UTF_8));
//                System.out.flush();

                sb.append( content.content().toString(CharsetUtil.UTF_8) );

                if (content instanceof LastHttpContent) {
                    System.out.println("} END OF CONTENT");
                }
            }
        }

        @Override
        public void exceptionCaught(
                ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
