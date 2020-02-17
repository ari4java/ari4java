package ch.loway.oss.ari4java.tools.http;

import ch.loway.oss.ari4java.generated.actions.requests.AsteriskPingGetRequest;
import ch.loway.oss.ari4java.generated.ari_6_0_0.actions.requests.ApplicationsGetRequest_impl_ari_6_0_0;
import ch.loway.oss.ari4java.generated.ari_6_0_0.actions.requests.AsteriskPingGetRequest_impl_ari_6_0_0;
import ch.loway.oss.ari4java.generated.models.AsteriskPing;
import ch.loway.oss.ari4java.tools.ARIEncoder;
import ch.loway.oss.ari4java.tools.AriCallback;
import ch.loway.oss.ari4java.tools.HttpParam;
import ch.loway.oss.ari4java.tools.RestException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.*;
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NettyHttpClientTest {

    private NettyHttpClient client;
    private ChannelFuture cf;

    @Before
    public void setUp() throws URISyntaxException {
        client = new NettyHttpClient() {
            protected void bootstrap() {
                // for testing skip the bootstrapping
            }

            protected ChannelFuture httpConnect() {
                return cf;
            }
        };
        client.initialize("http://localhost:8088/", "user", "p@ss");
    }

    @Test(expected = URISyntaxException.class)
    public void testInitializeBadURL() throws URISyntaxException {
        client.initialize(":", "", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitializeInvalidURL() throws URISyntaxException {
        client.initialize("ws://localhost:8088/", "", "");
    }

    @Test
    public void testBuildURL() {
        List<HttpParam> queryParams = new ArrayList<>();
        queryParams.add(HttpParam.build("a", "b/c"));
        queryParams.add(HttpParam.build("d", "e"));
        String url = client.buildURL("/channels", queryParams, false);
        assertEquals("/ari/channels?a=b%2Fc&d=e", url);
    }

    private void setupSync(NettyHttpClientHandler h) throws Exception {
        cf = mock(ChannelFuture.class);
        when(cf.addListener(any())).thenReturn(cf);
        when(cf.syncUninterruptibly()).thenReturn(cf);
        Channel ch = mock(Channel.class);
        when(ch.closeFuture()).thenReturn(cf);
        when(cf.channel()).thenReturn(ch);
        ChannelPipeline p = mock(ChannelPipeline.class);
        when(p.get("http-handler")).thenReturn(h);
        when(ch.pipeline()).thenReturn(p);
    }

    @Test
    public void testHttpActionSync() throws Exception {
        NettyHttpClientHandler h = new NettyHttpClientHandler();
        setupSync(h);
        h.responseStatus = HttpResponseStatus.OK;
        h.responseBytes = "testing".getBytes(ARIEncoder.ENCODING);
        String res = client.httpActionSync("", "GET", null, null, null);
        assertEquals("testing", res);
    }

    private EmbeddedChannel createTestChannel() {
        EmbeddedChannel channel = new EmbeddedChannel();
        channel.pipeline().addLast("http-codec", new HttpClientCodec());
        channel.pipeline().addLast("aggregator", new HttpObjectAggregator(NettyHttpClient.MAX_HTTP_REQUEST_KB * 1024));
        channel.pipeline().addLast("http-handler", new NettyHttpClientHandler());
        cf = channel.closeFuture();
        ((DefaultChannelPromise) cf).setSuccess(null);
        return channel;
    }

    private AsteriskPingGetRequest pingSetup(EmbeddedChannel channel) {
        channel.writeInbound(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                Unpooled.copiedBuffer("{\"ping\":\"pong\",\"timestamp\":\"2020-01-01T00:00:00.000+0000\",\"asterisk_id\":\"test_asterisk\"}", ARIEncoder.ENCODING)));
        AsteriskPingGetRequest_impl_ari_6_0_0 req = new AsteriskPingGetRequest_impl_ari_6_0_0();
        req.setHttpClient(client);
        return req;
    }

    private void pingValidate(EmbeddedChannel channel, AsteriskPing res) {
        String data = ((ByteBuf) channel.readOutbound()).toString(ARIEncoder.ENCODING);
        String expecting = "GET /ari/asterisk/ping HTTP/1.1";
        assertTrue("HTTP Request Data does not start with " + expecting, data.startsWith(expecting));
        assertTrue("Expected HTTP Auth Header", data.contains("authorization: Basic dXNlcjpwQHNz"));
        assertEquals("pong", res.getPing());
        assertEquals("test_asterisk", res.getAsterisk_id());
    }

    @Test
    public void testHttpActionSyncPing() throws Exception {
        System.out.println("TEST: testHttpActionSyncPing");
        EmbeddedChannel channel = createTestChannel();
        AsteriskPingGetRequest req = pingSetup(channel);
        AsteriskPing res = req.execute();
        pingValidate(channel, res);
    }

    @Test
    public void testHttpActionAsyncPing() throws InterruptedException {
        EmbeddedChannel channel = createTestChannel();
        AsteriskPingGetRequest req = pingSetup(channel);
        final boolean[] callback = {false};
        req.execute(new AriCallback<AsteriskPing>() {
            @Override
            public void onSuccess(AsteriskPing res) {
                pingValidate(channel, res);
                callback[0] = true;
            }

            @Override
            public void onFailure(RestException e) {
                fail(e.toString());
            }
        });
        channel.runPendingTasks();
        assertTrue("No onSuccess Callback", callback[0]);
    }

    @Test
    public void testHttpActionException() {
        EmbeddedChannel channel = createTestChannel();
        ApplicationsGetRequest_impl_ari_6_0_0 req = new ApplicationsGetRequest_impl_ari_6_0_0("test");
        req.setHttpClient(client);
        // when the response is JSON error then return the error from the server
        channel.writeInbound(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND,
                Unpooled.copiedBuffer("{\"message\":\"a test error\"}", ARIEncoder.ENCODING)));
        boolean exception = false;
        try {
            req.execute();
        } catch (RestException e) {
            assertEquals("a test error", e.getMessage());
            exception = true;
        }
        assertTrue("Expecting an exception", exception);
        // when the response is not JSON and there is an error definition from the API then return API definition
        channel.writeInbound(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND,
                Unpooled.copiedBuffer("Not found", ARIEncoder.ENCODING)));
        exception = false;
        try {
            req.execute();
        } catch (RestException e) {
            assertEquals("Application does not exist.", e.getMessage());
            exception = true;
        }
        assertTrue("Expecting an exception", exception);
    }

}
