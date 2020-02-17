package ch.loway.oss.ari4java.tools.http;

import ch.loway.oss.ari4java.tools.ARIEncoder;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class NettyHttpClientHandlerTest {

    @Test
    public void testResponse() {
        NettyHttpClientHandler h = new NettyHttpClientHandler();
        EmbeddedChannel channel = new EmbeddedChannel();
        channel.pipeline().addLast("http-codec", new HttpClientCodec());
        channel.pipeline().addLast("aggregator", new HttpObjectAggregator(NettyHttpClient.MAX_HTTP_REQUEST_KB * 1024));
        channel.pipeline().addLast("http-handler", h);
        channel.writeInbound(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer("test", ARIEncoder.ENCODING)));
        assertEquals("test", h.getResponseText());
    }

}
