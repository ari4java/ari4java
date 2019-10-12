package ch.loway.oss.ari4java.sandbox.sample;

import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.tools.ARIException;
import ch.loway.oss.ari4java.AriVersion;
import ch.loway.oss.ari4java.generated.AsteriskInfo;
import ch.loway.oss.ari4java.generated.Bridge;
import ch.loway.oss.ari4java.generated.Channel;
import ch.loway.oss.ari4java.generated.Message;
import ch.loway.oss.ari4java.generated.StasisStart;
import ch.loway.oss.ari4java.tools.AriCallback;
import ch.loway.oss.ari4java.tools.MessageQueue;
import ch.loway.oss.ari4java.tools.RestException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class opens up an ARI application that creates a bridge with MOH.
 * When a call enters the application, a message is printed and the call
 * is connected to the bridge.
 *
 * @author lenz
 */
public class ConnectAndDial {

    public static final String ASTERISK_ADDRESS = "http://192.168.99.100:18088/";
    public static final String ASTERISK_USER    = "ari4java";
    public static final String ASTERISK_PASS    = "yothere";
    
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
     * 
     */
    public void start() {

        try {

            connect();
            createBridge();

            processEvents();

            removeBridge();

        } catch (ARIException e) {
            e.printStackTrace();
        } finally {
            if (ari != null) {
                try {
                    ARI.sleep(500);
                    ari.cleanup();
                } catch (Throwable t) {
                }
            }
        }

    }

    public void connect() throws ARIException {

        System.out.println("Connecting to: " + ASTERISK_ADDRESS 
                + " as " + ASTERISK_USER + ":" + ASTERISK_PASS);
        
        ari = ARI.build( ASTERISK_ADDRESS, "myapp", 
                ASTERISK_USER, ASTERISK_PASS, 
                AriVersion.IM_FEELING_LUCKY);

        System.out.println("Connected through ARI: " + ari.getVersion());
        
        // let's raise an exeption if the connection is not valid
        AsteriskInfo ai = ari.asterisk().getInfo("");
        System.out.println("Hey! We're connected! Asterisk Version: " + ai.getSystem().getVersion());

    }

    public void createBridge() throws ARIException {

        // create a bridge and start playing MOH on it
        // UGLY: we should have a constant for the allowed bridge types
        System.out.println( "Creating a bridge");
        b = ari.bridges().create("", "a4j-bridge1", "myBridge");
        System.out.println("Bridge ID:" + b.getId() + " Name:" + b.getName() + " Tech:" + b.getTechnology() + " Creator:" + b.getCreator());
        
        // start MOH on the bridge
        System.out.println( "Starting MOH on bridge");
        ari.bridges().startMoh(b.getId(), "");

        // check which bridges are available
        System.out.println( "Listing bridges");
        List<Bridge> bridges = ari.bridges().list();

        for (Bridge bb : bridges) {
            printBridge(bb);
        }

    }


    /**
     * The new style of event processing...
     *
     * @throws ARIException
     */

    public void processEvents() throws ARIException {

        System.out.println( "Starting events... " );
        MessageQueue mq = ari.getWebsocketQueue();

        long start = System.currentTimeMillis();

        String dummy = "";
  
        Channel chan = ari.channels().originate("Local/100@wdep", "100", "wdep", 1, dummy, dummy, dummy, dummy, 10000, Collections.EMPTY_MAP, dummy, dummy, dummy);
        System.out.println( "Channel:" + chan.getId() + " in state " + chan.getState() );
        
        while ((System.currentTimeMillis() - start) < 10 * 1000L) {
            
            Message m = mq.dequeueMax( 100, 20 );
            if (m != null) {

                long now = System.currentTimeMillis() - start;
                System.out.println(now + ": " + m);

                if (m instanceof StasisStart) {
                    StasisStart event = (StasisStart) m;
                    System.out.println("Channel found: " + event.getChannel().getId() + " State:" + event.getChannel().getState());

                    ari.bridges().addChannel(b.getId(), event.getChannel().getId(), "");
                }
            } 
        }

        System.out.println( "No more events... " );
    }


    public void removeBridge() throws ARIException {

        System.out.println( threadName() + "Removing bridge...."  );

        ari.bridges().destroy(b.getId(), new AriCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                // Let's do something with the returned value
                System.out.println( threadName() + "Bridge destroyed " );
            }

            @Override
            public void onFailure(RestException e) {
                System.out.println( threadName() + "Failure in removeBridge() " );
                e.printStackTrace();
            }
        });
    }

    /**
     * Dumps a bridge to string.
     * Should we have a default toString that makes more sense?
     * 
     * @param b
     */
    private void printBridge(Bridge b) {
        System.out.println(". BridgeID:" + b.getId()
                + " Name:" + b.getName()
                + " Tech:" + b.getTechnology()
                + " Creator:" + b.getCreator()
                + " Class: " + b.getBridge_class()
                + " Type: " + b.getBridge_type()
                + " Chans: " + b.getChannels().size());
        for (String s : b.getChannels()) {
            System.out.println(" - ChannelID: " + s);
        }
    }
    
    /**
     * The name of the current thread.
     * @return 
     */
    
    private String threadName() {
        return "[Thread:" + Thread.currentThread().getName() + "] ";
    }
}
