package ch.loway.oss.ari4java.examples;

import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.tools.ARIException;
import ch.loway.oss.ari4java.AriVersion;
import ch.loway.oss.ari4java.generated.models.AsteriskInfo;
import ch.loway.oss.ari4java.generated.models.Bridge;
import ch.loway.oss.ari4java.generated.models.Channel;
import ch.loway.oss.ari4java.generated.models.Message;
import ch.loway.oss.ari4java.generated.models.StasisStart;
import ch.loway.oss.ari4java.tools.AriCallback;
import ch.loway.oss.ari4java.tools.MessageQueue;
import ch.loway.oss.ari4java.tools.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class opens up an ARI application that creates a bridge with MOH.
 * When a call enters the application, a message is printed and the call
 * is connected to the bridge.
 *
 * @author lenz
 */
public class ConnectAndDial {

    private final Logger logger = LoggerFactory.getLogger(ConnectAndDial.class);

    public static final String ASTERISK_ADDRESS = "http://192.168.56.44:8088/";
    public static final String ASTERISK_USER = "ari4java";
    public static final String ASTERISK_PASS = "yothere";
    public static final String APP_NAME = "connect-and-dail-app";

    ARI ari = null;
    Bridge b = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ConnectAndDial me = new ConnectAndDial();
        me.start();
    }

    /**
     * This is the app...
     */
    public void start() {
        try {
            connect();
            createBridge();
            processEvents();
            removeBridge();
        } catch (ARIException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (ari != null) {
                ARI.sleep(500);
                ari.cleanup();
            }
        }
    }

    public void connect() throws ARIException {

        logger.info("Connecting to: " + ASTERISK_ADDRESS
                + " as " + ASTERISK_USER + ":" + ASTERISK_PASS);

        ari = ARI.build(ASTERISK_ADDRESS, APP_NAME,
                ASTERISK_USER, ASTERISK_PASS,
                AriVersion.IM_FEELING_LUCKY);

        logger.info("Connected through ARI: " + ari.getVersion());

        // let's raise an exception if the connection is not valid
        AsteriskInfo ai = ari.asterisk().getInfo().execute();
        logger.info("Hey! We're connected! Asterisk Version: " + ai.getSystem().getVersion());

    }

    public void createBridge() throws ARIException {

        // create a bridge and start playing MOH on it
        // UGLY: we should have a constant for the allowed bridge types
        logger.info("Creating a bridge");
        b = ari.bridges().create().setName("myBridge").execute();
        logger.info("Bridge ID:" + b.getId() + " Name:" + b.getName() + " Tech:" + b.getTechnology() + " Creator:" + b.getCreator());

        // start MOH on the bridge
        logger.info("Starting MOH on bridge");
        ari.bridges().startMoh(b.getId()).execute();
    }


    /**
     * The new style of event processing...
     *
     * @throws ARIException
     */
    public void processEvents() throws ARIException {
        logger.info("Starting events... ");
        MessageQueue mq = ari.getWebsocketQueue();

        Channel chan = ari.channels().originate("Local/123@from-internal").setApp(APP_NAME)
                .setExtension("123").setContext("from-internal").setPriority(1).setTimeout(10000).execute();
        logger.info("Channel:" + chan.getId() + " in state " + chan.getState());

        long lastTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - lastTime) < 10 * 1000L) {
            Message m = mq.dequeueMax(10, 200);
            if (m != null) {
                logger.info("Message: {}", m.getType());
                lastTime = System.currentTimeMillis();
                if (m instanceof StasisStart) {
                    StasisStart event = (StasisStart) m;
                    if (event.getChannel().getId().equals(chan.getId())) {
                        logger.info("Channel found, adding to bridge (BridgeId: {}, ChanId:{}, ChanState: {})",
                                b.getId(), event.getChannel().getId(), event.getChannel().getState());
                        ari.bridges().addChannel(b.getId(), event.getChannel().getId()).execute();
                    }
                }
            }
        }
        logger.info("No more events... ");
    }


    public void removeBridge() throws ARIException {
        logger.info("Removing bridge....");
        ari.bridges().destroy(b.getId()).execute(new AriCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                // Let's do something with the returned value
                logger.info("Bridge destroyed");
            }

            @Override
            public void onFailure(RestException e) {
                logger.info("Failure in removeBridge()", e);
            }
        });
    }

}
