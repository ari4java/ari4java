package ch.loway.oss.ari4java.sandbox;

import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.AriVersion;
import ch.loway.oss.ari4java.generated.*;
import ch.loway.oss.ari4java.tools.ARIException;
import ch.loway.oss.ari4java.tools.AriCallback;
import ch.loway.oss.ari4java.tools.MessageQueue;
import ch.loway.oss.ari4java.tools.RestException;
import ch.loway.oss.ari4java.tools.amqp.GoAriRabbitMQClient;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class TestGoARIProxy {
    private GoAriRabbitMQClient goAriClient = new GoAriRabbitMQClient();
    private ARI ari = new ARI();

    @Before
    public void setUp() {
        goAriClient.initialize("localhost", 5672, "guest", "guest",
                Arrays.asList("dialer", "bridges"), "Asterisk");
    }

    @After
    public void tearDown() throws InterruptedException {
        // Hopefully this simulates that there will be a calling thread that remains running while client does tasks
        Thread.sleep(GoAriRabbitMQClient.COMMAND_TIMEOUT_MILLIS);
    }

    @Test
    public void does_go_ari_client_init() {
        // Just calls setup() which calls client initialize(), which if params make sense in setup() will work
        // This is an integration test. If not running a rabbitmq server and go-ari-proxysomewhere this can safely be set @Ignore
        Assert.assertEquals(true, true);
    }

    @Test
    public void get_asterisk_app_list_using_go_ari_sync() throws ARIException {
        // This is an integration test. If not running a rabbitmq server and go-ari-proxy somewhere this can safely be set @Ignore
        ari.setHttpClient(goAriClient);
//        ari.setWsClient(goAriClient);
        ari.setVersion(AriVersion.ARI_2_0_0);
        ActionApplications ac = ari.getActionImpl(ActionApplications.class);
        System.out.println("You have 10 seconds to generate MQ tunnels from Asterisk side, go-ari-proxy has one-way " +
                "initialization at this time. For instance: start an app that causes StasisStart right after you start this test");
        List response = ac.list();
        Assert.assertTrue(response.size()>0);
    }

    @Test
    public void get_asterisk_info_using_go_ari_sync() throws ARIException {
        // This is an integration test. If not running a rabbitmq server and go-ari-proxy somewhere this can safely be set @Ignore
        ari.setHttpClient(goAriClient);
//        ari.setWsClient(goAriClient);
        ari.setVersion(AriVersion.ARI_2_0_0);
        ActionAsterisk aa = ari.getActionImpl(ActionAsterisk.class);
        System.out.println("You have 10 seconds to generate MQ tunnels from Asterisk side, go-ari-proxy has one-way " +
                "initialization at this time. For instance: start an app that causes StasisStart right after you start this test");
        AsteriskInfo ai = aa.getInfo("");
        System.out.println(ai.getSystem().getEntity_id());
        Assert.assertFalse(ai.getSystem().getEntity_id().isEmpty());
    }

    @Test
    public void get_asterisk_info_using_go_ari_async() throws ARIException {
        // This is an integration test. If not running a rabbitmq server and go-ari-proxy somewhere this can safely be set @Ignore
        ari.setHttpClient(goAriClient);
//        ari.setWsClient(goAriClient);
        ari.setVersion(AriVersion.ARI_2_0_0);
        ActionAsterisk aa = ari.getActionImpl(ActionAsterisk.class);
        System.out.println("You have 10 seconds to generate MQ tunnels from Asterisk side, go-ari-proxy has one-way " +
                "initialization at this time. For instance: start an app that causes StasisStart right after you start this test");
        aa.getInfo("", new AriCallback<AsteriskInfo>() {
            @Override
            public void onSuccess(AsteriskInfo result) {
                System.out.println(result.getSystem().getEntity_id());
            }
            @Override
            public void onFailure(RestException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void create_bridge_using_go_ari_proxy() throws  ARIException {
        // This is an integration test. If not running a rabbitmq server and go-ari-proxy somewhere this can safely be set @Ignore
        ari.setHttpClient(goAriClient);
        ari.setVersion(AriVersion.ARI_2_0_0);
        System.out.println("You have 10 seconds to generate MQ tunnels from Asterisk side, go-ari-proxy has one-way " +
                "initialization at this time. For instance: start an app that causes StasisStart right after you start this test");
        ActionBridges ab = ari.getActionImpl(ActionBridges.class);
        Bridge bridgeCreated = ab.create("mixing","bridgeID2", "bridgeName2");
        Assert.assertTrue(bridgeCreated.getId() != null);
    }

    @Test
    public void process_event_queue() throws ARIException, InterruptedException {
        // Only requires that the client initialize() has been called; it should have been in setUp()
        ari.setVersion(AriVersion.ARI_2_0_0);
        System.out.println("You have 10 seconds to generate MQ tunnels from Asterisk side, go-ari-proxy has one-way " +
                "initialization at this time. For instance: start an app that causes StasisStart right after you start this test");
        MessageQueue mq = goAriClient.getEventMessageQueue(AriVersion.ARI_2_0_0);
        Message m = mq.dequeueMax( GoAriRabbitMQClient.COMMAND_TIMEOUT_MILLIS, 20 );
        if (m instanceof StasisStart) {
            StasisStart event = (StasisStart) m;
            Assert.assertNotNull(event.getChannel().getId());
            Assert.assertNotNull(event.getChannel().getState());
        } else {
            System.out.println("Nothing seems to have generated a StasisStart event on the go-ari-proxy to Asterisk");
            Assert.fail();
        }
    }
}
