package ch.loway.oss.ari4java.tools.http;

import ch.loway.oss.ari4java.ARI;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * HttpClientHandler handles the asynchronous response from the remote
 * HTTP server.
 *
 * @author mwalton
 *
 */

@ChannelHandler.Sharable
public class NettyHttpClientHandler extends SimpleChannelInboundHandler<Object> {
    protected byte[] responseBytes;
    protected HttpResponseStatus responseStatus;

    public void reset() {
        responseBytes = null;
        responseStatus = null;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            responseBytes = new byte[response.content().readableBytes()];
            response.content().readBytes(responseBytes);
            responseStatus = response.getStatus();
        } else {
            // TODO: what?
            if (msg != null) {
                System.out.println("Unknown object:" + msg);
            }
        }
    }

    public String getResponseText() {
        if (responseBytes == null || responseBytes.length == 0) {
            return null;
        }
        return new String(responseBytes, ARI.ENCODING);
    }

    public byte[] getResponseBytes() {
        return responseBytes;
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

