package ch.loway.oss.ari4java.tools.http;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.nio.charset.Charset;

/**
 * HttpClientHandler handles the asynchronous response from the remote
 * HTTP server.
 *
 * @author mwalton
 *
 */

@ChannelHandler.Sharable
public class NettyHttpClientHandler extends SimpleChannelInboundHandler<Object> {
    protected String responseText;
    protected HttpResponseStatus responseStatus;

    public void reset() {
        responseText = null;
        responseStatus = null;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        //Channel ch = ctx.channel();
        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            responseText = response.content().toString(Charset.defaultCharset());
            responseStatus = response.getStatus();
            
            //System.out.println( "S:" + responseStatus + " T:" + responseText);
            
        } else {
            // TODO: what?
            if ( msg != null ) {
                System.out.println( "Unknown object:" + msg);
            }
        }
    }

    public String getResponseText() {
        return responseText;
    }

    public HttpResponseStatus getResponseStatus() {
        return responseStatus;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}

