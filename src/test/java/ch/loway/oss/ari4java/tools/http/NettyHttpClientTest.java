package ch.loway.oss.ari4java.tools.http;

import ch.loway.oss.ari4java.generated.actions.requests.AsteriskPingGetRequest;
import ch.loway.oss.ari4java.generated.ari_6_0_0.actions.requests.*;
import ch.loway.oss.ari4java.generated.models.AsteriskPing;
import ch.loway.oss.ari4java.tools.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.util.concurrent.Future;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NettyHttpClientTest {

    private NettyHttpClient client;
    private ChannelFuture cf;
    private Future<Channel> fc;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private void setupTestClient(boolean init, Channel ch) throws URISyntaxException {
        client = new NettyHttpClient() {
            protected void initHttpBootstrap() {
                // for testing skip the bootstrapping
            }

            @Override
            protected Future<Channel> poolAcquire() {
                return fc;
            }

            @Override
            protected void poolRelease(Channel ch) {
                // for testing skip
            }
        };
        if (init) {
            client.initialize("http://localhost:8088/", "user", "p@ss");
        }
        if (ch instanceof EmbeddedChannel) {
            fc = ch.eventLoop().newSucceededFuture(ch);
        } else {
            //noinspection unchecked
            fc = (Future<Channel>) mock(Future.class);
            when(fc.syncUninterruptibly()).thenReturn(fc);
            when(fc.getNow()).thenReturn(ch);
        }
    }

    @AfterEach
    public void tearDown() {
        if (client != null) {
            client.destroy();
        }
    }

    @Test
    public void testInitializeBadURL() throws URISyntaxException {
        setupTestClient(false, null);
        assertThrows(URISyntaxException.class, () ->
                client.initialize(":", "", ""));
    }

    @Test
    public void testInitializeInvalidURL() throws URISyntaxException {
        setupTestClient(false, null);
        assertThrows(IllegalArgumentException.class, () ->
                client.initialize("ws://localhost:8088/", "", ""));
    }

    @Test
    public void testBuildURL() throws Exception {
        setupTestClient(true, null);
        List<HttpParam> queryParams = new ArrayList<>();
        queryParams.add(HttpParam.build("a", "b/c"));
        queryParams.add(HttpParam.build("d", "e"));
        String url = client.buildURL("/channels", queryParams, false);
        assertEquals("/ari/channels?a=b%2Fc&d=e", url);
    }

    @Test
    public void testInitialize() throws Exception {
        NettyHttpClient client = new NettyHttpClient();
        client.initialize("http://localhost:8088/", "user", "p@ss");
        client.destroy();
        assertNotNull(client.baseUri);
    }

    @Test
    public void testWsConnect() throws Exception {
        Bootstrap bootstrap = mock(Bootstrap.class);
        NettyWSClientHandler testHandler = mock(NettyWSClientHandler.class);
        ChannelFuture handshakeFuture = mock(ChannelFuture.class);
        when(testHandler.handshakeFuture()).thenReturn(handshakeFuture);
        EmbeddedChannel channel = createTestChannel("ws-handler", testHandler);
        FullHttpRequest req = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/events");
        HttpHeaders headers = req.headers();
        headers.set(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET);
        headers.set(HttpHeaderNames.SEC_WEBSOCKET_KEY, "dGhlIHNhbXBsZSBub25jZQ==");
        headers.set(HttpHeaderNames.SEC_WEBSOCKET_VERSION, WebSocketVersion.V13);
        cf = channel.closeFuture();
        when(bootstrap.connect(eq("localhost"), eq(443))).thenReturn(cf);
        ((DefaultChannelPromise) cf).setSuccess(null);
        HttpResponseHandler testWsCallback = mock(HttpResponseHandler.class);
        when(testWsCallback.getLastResponseTime()).thenReturn(0L);
        class TestNettyHttpClient extends NettyHttpClient {
            @Override
            public WsClientConnection connect(final HttpResponseHandler callback, final String url,
                                              final List<HttpParam> lParamQuery) {
                try {
                    baseUri = new URI("https://localhost/");
                    this.auth = "123";
                    getWsHandshake(url, lParamQuery); // here so the code is run for code coverage
                } catch (URISyntaxException e) {
                    // oh well
                }
                pingPeriod = 1;
                pingTimeUnit = TimeUnit.SECONDS;
                wsHandler = testHandler;
                wsCallback = testWsCallback;
                return connect(bootstrap, callback);
            }
        }
        TestNettyHttpClient client = new TestNettyHttpClient();
        WsClient.WsClientConnection connection = client.connect(mock(HttpResponseHandler.class), "/events", null);
        assertNotNull(connection, "Expected WsClientConnection");
        channel.writeInbound(req);
        ArgumentCaptor<ChannelFutureListener> captor = ArgumentCaptor.forClass(ChannelFutureListener.class);
        verify(handshakeFuture).addListener(captor.capture());
        captor.getValue().operationComplete(channel.newSucceededFuture());
        client.onWSPong();
        Thread.sleep(2500);
        System.out.println(channel.inboundMessages().toString());
        channel.readOutbound();
        client.destroy();
    }

    @Test
    public void testHttpActionSync() throws Exception {
        Channel ch = mock(Channel.class);
        ChannelPipeline p = mock(ChannelPipeline.class);
        when(ch.pipeline()).thenReturn(p);
        setupTestClient(true, ch);
        NettyHttpClientHandler h = new NettyHttpClientHandler() {
            @Override
            public void reset() {
                // dont reset for this test
            }

            @Override
            public void waitForResponse(int timeout) {
                // dont wait for test
            }
        };
        when(p.get("http-handler")).thenReturn(h);
        h.responseStatus = HttpResponseStatus.OK;
        h.responseBytes = "testing".getBytes(ARIEncoder.ENCODING);
        String res = client.httpActionSync("", "GET", null, null, null);
        assertEquals("testing", res);
    }

    private EmbeddedChannel createTestChannel() {
        EmbeddedChannel channel = createTestChannel("http-handler", new NettyHttpClientHandler());
        cf = channel.closeFuture();
        ((DefaultChannelPromise) cf).setSuccess(null);
        return channel;
    }

    private EmbeddedChannel createTestChannel(String name, ChannelHandler handler) {
        EmbeddedChannel channel = new EmbeddedChannel();
        channel.pipeline().addLast("http-codec", new HttpClientCodec());
        channel.pipeline().addLast("http-aggregator", new HttpObjectAggregator(NettyHttpClient.MAX_HTTP_REQUEST));
        channel.pipeline().addLast(name, handler);
        return channel;
    }

    private void delayedWriteInbound(EmbeddedChannel channel, HttpResponseStatus status, String data) {
        delayedWriteInbound(channel, status, data, 200);
    }

    private void delayedWriteInbound(EmbeddedChannel channel, HttpResponseStatus status, String data, long sleep) {
        executor.submit(() -> {
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            channel.writeInbound(new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(data, ARIEncoder.ENCODING)));
        });
    }

    private AsteriskPingGetRequest pingSetup(EmbeddedChannel channel) {
        delayedWriteInbound(channel, HttpResponseStatus.OK,
                "{\"ping\":\"pong\",\"timestamp\":\"2020-01-01T00:00:00.000+0000\",\"asterisk_id\":\"test_asterisk\"}", 500);
        AsteriskPingGetRequest_impl_ari_6_0_0 req = new AsteriskPingGetRequest_impl_ari_6_0_0();
        req.setHttpClient(client);
        return req;
    }

    private void pingValidate(EmbeddedChannel channel, AsteriskPing res) {
        String data = ((ByteBuf) channel.readOutbound()).toString(ARIEncoder.ENCODING);
        String expecting = "GET /ari/asterisk/ping HTTP/1.1";
        assertTrue(data.startsWith(expecting), "HTTP Request Data does not start with " + expecting);
        assertTrue(data.contains("authorization: Basic dXNlcjpwQHNz"), "Expected HTTP Auth Header");
        assertEquals("pong", res.getPing());
        assertEquals("test_asterisk", res.getAsterisk_id());
    }

    @Test
    public void testHttpActionSyncPing() throws Exception {
        EmbeddedChannel channel = createTestChannel();
        setupTestClient(true, channel);
        AsteriskPingGetRequest req = pingSetup(channel);
        AsteriskPing res = req.execute();
        pingValidate(channel, res);
    }

    @Test
    public void testHttpActionAsyncPing() throws Exception {
        EmbeddedChannel channel = createTestChannel();
        setupTestClient(true, channel);
        AsteriskPingGetRequest req = pingSetup(channel);
        final AtomicBoolean callback = new AtomicBoolean(false);
        req.execute(new AriCallback<AsteriskPing>() {
            @Override
            public void onSuccess(AsteriskPing res) {
                pingValidate(channel, res);
                callback.set(true);
            }

            @Override
            public void onFailure(RestException e) {
                fail(e.toString());
            }
        });
        Thread.sleep(550);
        channel.runPendingTasks();
        assertTrue(callback.get(), "No onSuccess Callback");
    }

    @Test
    public void testHttpActionException() throws Exception {
        EmbeddedChannel channel = createTestChannel();
        setupTestClient(true, channel);
        ApplicationsGetRequest_impl_ari_6_0_0 req = new ApplicationsGetRequest_impl_ari_6_0_0("test");
        req.setHttpClient(client);
        // when the response is JSON error then return the error from the server
        delayedWriteInbound(channel, HttpResponseStatus.NOT_FOUND, "{\"message\":\"a test error\"}");
        boolean exception = false;
        try {
            req.execute();
        } catch (RestException e) {
            assertEquals("a test error", e.getMessage());
            exception = true;
        }
        assertTrue(exception, "Expecting an exception");
        // when the response is not JSON and there is an error definition from the API then return API definition
        delayedWriteInbound(channel, HttpResponseStatus.NOT_FOUND, "Not found");
        exception = false;
        try {
            req.execute();
        } catch (RestException e) {
            assertEquals("Application does not exist.", e.getMessage());
            exception = true;
        }
        assertTrue(exception, "Expecting an exception");
    }

    @Test
    public void testBodyFieldSerialisation() throws Exception {
        EmbeddedChannel channel = createTestChannel();
        setupTestClient(true, channel);
        delayedWriteInbound(channel, HttpResponseStatus.OK, "[]");
        AsteriskUpdateObjectPutRequest_impl_ari_6_0_0 req = new AsteriskUpdateObjectPutRequest_impl_ari_6_0_0(
                "cc", "ot", "id");
        req.setHttpClient(client);
        req.addFields("key1", "val1").addFields("key2", "val2").execute();
        validateBody(channel, "fields");
    }

    @Test
    public void testBodyVariableSerialisation() throws Exception {
        EmbeddedChannel channel = createTestChannel();
        setupTestClient(true, channel);
        delayedWriteInbound(channel, HttpResponseStatus.OK, "{}");
        EndpointsSendMessagePutRequest_impl_ari_6_0_0 req = new EndpointsSendMessagePutRequest_impl_ari_6_0_0("to", "from");
        req.setHttpClient(client);
        req.addVariables("key1", "val1").addVariables("key2", "val2").execute();
        validateBody(channel, "variables");
    }

    @Test
    public void testBodyObjectSerialisation() throws Exception {
        final EmbeddedChannel channel = createTestChannel();
        setupTestClient(true, channel);
        delayedWriteInbound(channel, HttpResponseStatus.OK, "{}");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "val1");
        map.put("key2", "val2");
        ApplicationsFilterPutRequest_impl_ari_6_0_0 req = new ApplicationsFilterPutRequest_impl_ari_6_0_0("app");
        req.setHttpClient(client);
        req.setFilter(map).execute();
        validateBody(channel, "filter");
    }

    private void validateBody(EmbeddedChannel channel, String field) {
        String expected = "{\"" + field + "\":{\"key1\":\"val1\",\"key2\":\"val2\"}}";
        if ("fields".equals(field)) {
            expected = "{\"fields\":[{\"attribute\":\"key1\",\"value\":\"val1\"},{\"attribute\":\"key2\",\"value\":\"val2\"}]}";
        }
        StringBuilder buffer = new StringBuilder();
        ByteBuf data = channel.readOutbound();
        while (data != null) {
            if (data.readableBytes() > 0) {
                buffer.append(data.toString(ARIEncoder.ENCODING));
            }
            data = channel.readOutbound();
        }
        String[] lines = buffer.toString().split("\n");
        assertEquals(expected, lines[lines.length - 1].trim());
    }

}
