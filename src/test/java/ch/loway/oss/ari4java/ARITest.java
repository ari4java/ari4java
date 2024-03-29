package ch.loway.oss.ari4java;

import ch.loway.oss.ari4java.generated.actions.ActionAsterisk;
import ch.loway.oss.ari4java.generated.actions.ActionBridges;
import ch.loway.oss.ari4java.generated.actions.ActionEvents;
import ch.loway.oss.ari4java.generated.ari_0_0_1.actions.ActionAsterisk_impl_ari_0_0_1;
import ch.loway.oss.ari4java.generated.ari_0_0_1.actions.ActionBridges_impl_ari_0_0_1;
import ch.loway.oss.ari4java.generated.ari_0_0_1.models.Bridge_impl_ari_0_0_1;
import ch.loway.oss.ari4java.generated.ari_1_0_0.actions.*;
import ch.loway.oss.ari4java.generated.models.Bridge;
import ch.loway.oss.ari4java.generated.models.Mailbox;
import ch.loway.oss.ari4java.tools.*;
import ch.loway.oss.ari4java.tools.http.NettyHttpClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author lenz
 */
public class ARITest {

    static NettyHttpClient client;

    @BeforeAll
    public static void start() {
        client = mock(NettyHttpClient.class);
        AriFactory.nettyHttpClient = client;
    }

    @Test
    public void testImplementationFactory() throws Exception {
        ARI ari = new ARI();
        ari.setHttpClient(mock(HttpClient.class));
        ari.setWsClient(mock(WsClient.class));
        try {
            ari.getActionImpl(ActionAsterisk.class);
            fail("Expecting an exception");
        } catch (Exception e) {
            assertEquals("AriVersion not set", e.getMessage());
        }
        ari.setVersion(AriVersion.ARI_0_0_1);
        assertEquals(AriVersion.ARI_0_0_1, ari.getVersion());
        assertEquals(ActionBridges_impl_ari_0_0_1.class, ari.getActionImpl(ActionBridges.class).getClass());
        assertEquals(Bridge_impl_ari_0_0_1.class, ari.getModelImpl(Bridge.class).getClass());
        boolean exception = false;
        try {
            ari.getModelImpl(Mailbox.class);
        } catch (ARIException e) {
            exception = e.getMessage().contains("No concrete implementation");
        }
        assertTrue(exception, "Expected 'No concrete implementation' exception for getModelImpl(String.class)");
        exception = false;
        try {
            ari.getModelImpl(String.class);
        } catch (ARIException e) {
            exception = e.getMessage().contains("Invalid Class");
        }
        assertTrue(exception, "Expected 'Invalid Class' exception for getModelImpl(String.class)");
        assertNotNull(ari.getActionImpl(ActionEvents.class), "Expected Action");
        try {
            ari.getActionImpl(String.class);
            fail("Expected exception");
        } catch (Exception e) {
            assertEquals("Invalid Class for action class java.lang.String", e.getMessage());
        }
    }

    @Test
    public void testBuildAction() throws Exception {
        ARI ari = new ARI();
        try {
            ari.asterisk();
            fail("Expecting an exception");
        } catch (Exception e) {
            assertEquals("AriVersion not set", e.getMessage());
        }
        ari.setVersion(AriVersion.ARI_0_0_1);
        ari.setHttpClient(mock(HttpClient.class));
        ari.setWsClient(mock(WsClient.class));

        ActionAsterisk asterisk = ari.asterisk();
        assertEquals(ActionAsterisk_impl_ari_0_0_1.class.toString(), asterisk.getClass().toString());

        ari.setVersion(AriVersion.ARI_1_0_0);
        asterisk = ari.asterisk();
        assertEquals(ActionAsterisk_impl_ari_1_0_0.class.toString(), asterisk.getClass().toString());
        assertNotNull(((ActionAsterisk_impl_ari_1_0_0)asterisk).getHttpClient(), "Expecting HttpClient to be set");
        assertNotNull(((ActionAsterisk_impl_ari_1_0_0)asterisk).getWsClient(), "Expecting WsClient to be set");

        asterisk = ari.getActionImpl(ActionAsterisk.class);
        assertEquals(ActionAsterisk_impl_ari_1_0_0.class.toString(), asterisk.getClass().toString());
        assertNotNull(((ActionAsterisk_impl_ari_1_0_0)asterisk).getHttpClient(), "Expecting HttpClient to be set");
        assertNotNull(((ActionAsterisk_impl_ari_1_0_0)asterisk).getWsClient(), "Expecting WsClient to be set");

        assertEquals(ActionApplications_impl_ari_1_0_0.class.toString(), ari.applications().getClass().toString());
        assertEquals(ActionBridges_impl_ari_1_0_0.class.toString(), ari.bridges().getClass().toString());
        assertEquals(ActionChannels_impl_ari_1_0_0.class.toString(), ari.channels().getClass().toString());
        assertEquals(ActionDeviceStates_impl_ari_1_0_0.class.toString(), ari.deviceStates().getClass().toString());
        assertEquals(ActionEndpoints_impl_ari_1_0_0.class.toString(), ari.endpoints().getClass().toString());
        assertEquals(ActionEvents_impl_ari_1_0_0.class.toString(), ari.events().getClass().toString());
        assertEquals(ActionPlaybacks_impl_ari_1_0_0.class.toString(), ari.playbacks().getClass().toString());
        assertEquals(ActionRecordings_impl_ari_1_0_0.class.toString(), ari.recordings().getClass().toString());
        assertEquals(ActionSounds_impl_ari_1_0_0.class.toString(), ari.sounds().getClass().toString());
    }

    @Test
    public void testCreateUid() {
        String v = ARI.getUID();
        assertTrue(v.length() > 0, "UID created");
        assertNotSame(v, ARI.getUID(), "new UID the same as previous");
    }

    @Test
    public void testBuildVersion() {
        String v = new ARI().getBuildVersion();
        assertNotNull(v, "Build Version cannot be null");
    }

    @Test
    public void testBuild() throws Exception {
        when(client.httpActionSync(eq("/applications/test"), eq("GET"), any(), any(), any())).thenReturn(
                "{\"channel_ids\": [], \"bridge_ids\": [], \"endpoint_ids\": [], \"device_names\": []}"
        );
        ARI ari = ARI.build("http://local:8088/ari/", "test", "test", "test", AriVersion.ARI_0_0_1);
        assertEquals("test", ari.getAppName());
        assertEquals(AriVersion.ARI_0_0_1, ari.getVersion());
        MessageQueue queue = ari.getWebsocketQueue();
        assertEquals(0, queue.size());
        try {
            ari.getWebsocketQueue();
            fail("Expected an exception");
        } catch (Exception e) {
            assertEquals("Websocket already present", e.getMessage());
        }
        ari.cleanup();
        ari.setHttpClient(client);
        ari.setWsClient(mock(WsClient.class)); // must be a different ref so we can get the code coverage...
        ari.cleanup();
    }

    @Test
    public void testCloseAction() throws Exception {
        ARI ari = new ARI();
        try {
            ari.closeAction(new Object());
            fail("Expecting exception");
        } catch (ARIException e) {
            assertTrue(e.getMessage().contains("is not an Action implementation"), "Expecting an implementation error");
        }
        AtomicBoolean disconnected = new AtomicBoolean(false);
        ari.closeAction(new BaseAriAction() {
            @Override
            public synchronized void disconnectWs() throws RestException {
                disconnected.set(true);
            }
        });
        assertTrue(disconnected.get(), "Expected disconnectWs to be called");
    }

    @AfterAll
    public static void end() {
        AriFactory.nettyHttpClient = null;
    }

}
