

package ch.loway.oss.ari4java.sandbox.sample;

import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.tools.ARIException;
import ch.loway.oss.ari4java.AriVersion;
import ch.loway.oss.ari4java.generated.ActionEvents;
import ch.loway.oss.ari4java.generated.AsteriskInfo;
import ch.loway.oss.ari4java.generated.Bridge;
import ch.loway.oss.ari4java.generated.Message;
import ch.loway.oss.ari4java.generated.StasisStart;
import ch.loway.oss.ari4java.generated.Variable;
import ch.loway.oss.ari4java.tools.AriCallback;
import ch.loway.oss.ari4java.tools.RestException;
import java.util.List;


/**
 * This class opens up an ARI application that creates a bridge with MOH.
 * When a call enters the application, a message is printed and the call
 * is connected to the bridge.
 *
 * @author lenz
 */
public class ConnectAndDial {


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
                ari.cleanup();
            }
        }

    }


    public void connect() throws ARIException {

        ari = ARI.build("http://10.10.5.26:8088/", "hello", "aritestx", "testme", AriVersion.IM_FEELING_LUCKY);

        // lets raise an exeption if the connection is not valid
        AsteriskInfo ai = ari.asterisk().getInfo("");
        System.out.println( "Hey! We're connected! Asterisk Version: " + ai.getSystem().getVersion() );

    }


    public void createBridge() throws ARIException {

        // create a bridge and start playing MOH on it
        // UGLY: we should have a constant for the allowed bridge types
        b = ari.bridges().create("", "myBridge");
        System.out.println( "Bridge ID:" + b.getId() + " Name:" + b.getName() + " Tech:" + b.getTechnology() + " Creator:" + b.getCreator() );


        // start MOH on the bridge
        ari.bridges().startMoh(b.getId(), "");

        // check which bridges are available
        // UGLY: why not a List<Bridges>? the cliens should not care....
        List<? extends Bridge> bridges = ari.bridges().list();

        for ( Bridge bb: bridges ) {
            printBridge(  bb  );
        }

    }

    /**
     * This code is ugly.
     * we should have only one websocket, per app, not linked to an action....
     * 
     * @throws ARIException
     */

    public void processEvents() throws ARIException {

        ActionEvents ae = ari.events();
        ae.eventWebsocket("hello", new AriCallback<Message>() {

            @Override
            public void onSuccess(Message result) {
                // Let's do something with the event

                System.out.println(result);

                if (result instanceof StasisStart) {
                    StasisStart event = (StasisStart) result;
                    System.out.println("Channel found: " + event.getChannel().getId() + " State:" + event.getChannel().getState());

                    try {
                        ari.bridges().addChannel(b.getId(), event.getChannel().getId(), "");
                    } catch (ARIException e) {
                        // UGLY!!
                        System.out.println("Error: " + e.getMessage());
                    }
                }



            }

            @Override
            public void onFailure(RestException e) {
                e.printStackTrace();
            }
        });

        try {
            Thread.sleep(5000); // Wait 5 seconds for events
        } catch (InterruptedException e) {
        }

        // UGLIEST - if you do a closeaction, everything dies and no more actions
        // are sent
        ari.closeAction(ae); // Now close the websocket

    }

    public void removeBridge() throws ARIException {

        System.out.println( "Removing...." );

        ari.bridges().destroy(b.getId(), new AriCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                // Let's do something with the returned value
                System.out.println("Bridge destroyed");
            }

            @Override
            public void onFailure(RestException e) {
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

    private void printBridge( Bridge b ) {
        System.out.println( "Bridge ID:" + b.getId()
                              + " Name:" + b.getName()
                              + " Tech:" + b.getTechnology()
                              + " Creator:" + b.getCreator()
                              + " Class: " + b.getBridge_class()
                              + " Type: " + b.getBridge_type()
                              + " Chans: " + b.getChannels().size() );
        for ( String s: b.getChannels() ) {
            System.out.println( " - ID: " + s );
        }

    }


}
