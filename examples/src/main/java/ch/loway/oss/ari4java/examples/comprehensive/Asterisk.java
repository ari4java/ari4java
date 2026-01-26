package ch.loway.oss.ari4java.examples.comprehensive;

import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.AriVersion;
import ch.loway.oss.ari4java.generated.AriWSHelper;
import ch.loway.oss.ari4java.generated.models.*;
import ch.loway.oss.ari4java.tools.AriCallback;
import ch.loway.oss.ari4java.tools.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Asterisk {

    private static final String ARI_APP_NAME = "comprehensive-app";
    private static final Logger logger = LoggerFactory.getLogger(Asterisk.class);
    private static final Map<String, String> lookups = new HashMap<>();
    private static final Map<String, State> states = new HashMap<>();
    private final String address;
    private final String user;
    private final String pass;
    private final AriVersion version;
    private ARI ari;
    private ExecutorService threadPool;

    public Asterisk(String address, String user, String pass, AriVersion version) {
        this.address = address;
        this.user = user;
        this.pass = pass;
        this.version = version;
    }

    /**
     * Starts the ARI Event Websocket to Asterisk using the Handler class
     * @see Asterisk.Handler
     * @return true if connected successfully
     */
    public boolean start() {
        logger.info("Starting ARI...");
        try {
            ari = ARI.build(address, ARI_APP_NAME, user, pass, version);
            // execute syncronously to validate ARI before starting the event websocket
            AsteriskInfo info = ari.asterisk().getInfo().execute();
            logger.info("Asterisk {}", info.getSystem().getVersion());
            threadPool = Executors.newFixedThreadPool(5);
            ari.events().eventWebsocket(ARI_APP_NAME).execute(new Handler());
            return true;
        } catch (Throwable t) {
            logger.error("Error: {}", t.getMessage(), t);
        }
        return false;
    }

    private void getAsteriskInfo() {
        try {
            ari.asterisk().getInfo().execute(new AriCallback<AsteriskInfo>() {
                @Override
                public void onSuccess(AsteriskInfo info) {
                    logger.info("Asterisk {}", info.getSystem().getVersion());
                }

                @Override
                public void onFailure(RestException e) {
                    logger.error("Error getting Asterisk version: {}", e.getMessage(), e);
                }
            });
        } catch (Throwable t) {
            logger.error("Error: {}", t.getMessage(), t);
        }
    }

    /**
     * Stops the ARI Event Websocket
     */
    public void stop() {
        if (ari != null) ari.cleanup();
        if (threadPool != null) threadPool.shutdown();
    }

    /**
     * Start a call by creating a channel to the provided <em>from</em> number,
     * the events will then create a channel to the <em>to</em> number and add them to a bridge
     * @param from the 1st number to call
     * @param to the 2nd number to call
     * @return an id used for the call state, can be used to end the call
     * @throws RestException raised by ARI interactions
     */
    public String startCall(String from, String to) throws RestException {
        State state = new State();
        state.from = from;
        state.to = to;
        state.channel1 = createChannel(state.from, "Outbound: " + state.to);
        synchronized (states) {
            states.put(state.id, state);
        }
        synchronized (lookups) {
            lookups.put(state.channel1, state.id);
        }
        return state.id;
    }

    /**
     * Create channel, when to is 123 then use type Local, else PJSIP
     * @param to the destination
     * @param from the source the destination will see in the CLI
     * @return the id of the created channel
     * @throws RestException raised by ARI interactions
     */
    private String createChannel(String to, String from) throws RestException {
        String endpoint = "PJSIP/" + to;
        if ("123".equals(to)) {
            // 123 is the Echo App in the dialplan, setting the type to Local executes the dialplan
            endpoint = "Local/123@from-internal";
        }
        // AppArgs is set to "me" so we know when the StasisStart event occurs we can ignore it as we started the process
        // Context from-internal is what was setup in the vagrant box
        Channel channel = ari.channels().originate(endpoint).setApp(ARI_APP_NAME).setCallerId(from).setAppArgs("me")
                .setContext("from-internal").execute();
        logger.debug("Channel created to {}, Id: {}", channel.getConnected().getNumber(), channel.getId());
        return channel.getId();
    }

    /**
     * End a call by hanging up channel1 from the State object, this will in turn hangup channel2
     * @param stateId the id of the State object
     */
    public void endCall(String stateId) {
        if (states.containsKey(stateId)) {
            hangupChannel(states.get(stateId).channel1);
        }
    }

    /**
     * Hangs up the channel if in the lookups
     * @param channelId the channel id
     */
    private void hangupChannel(String channelId) {
        if (channelId != null) {
            if (lookups.containsKey(channelId)) {
                try {
                    ari.channels().hangup(channelId).execute(new AriCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            logger.debug("hanging up channel");
                        }

                        @Override
                        public void onFailure(RestException e) {
                            logger.error("Error hanging up channel", e);
                        }
                    });
                } catch (RestException e) {
                    logger.error("Error hanging up channel", e);
                }
            } else {
                logger.warn("Channel not found in lookup {}", channelId);
            }
        }
    }

    /**
     * Creates a bridge adds its id to the State object and adds channel1 to the bridge
     * @param state the state for the call
     * @throws RestException raised by ARI interactions
     */
    private void createBridgeAndAddChannel1(State state) throws RestException {
        ari.bridges().create().execute(new AriCallback<Bridge>() {
            @Override
            public void onSuccess(Bridge bridge) {
                logger.debug("Bridge created id: {}", bridge.getId());
                state.bridge = bridge.getId();
                try {
                    addChannelToBridge(state.bridge, state.channel1);
                } catch (RestException e) {
                    logger.error("Error adding channel to bridge: {}", e.getMessage(), e);
                }
            }

            @Override
            public void onFailure(RestException e) {
                logger.error("Error creating bridge: {}", e.getMessage(), e);
            }
        });
    }

    /**
     * @param bridgeId id of the bridge
     * @param channelId id of the channel
     * @throws RestException raised by ARI interactions
     */
    private void addChannelToBridge(String bridgeId, String channelId) throws RestException {
        ari.bridges().addChannel(bridgeId, channelId).execute(new AriCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                logger.debug("Channel {}, added to Bridge {}", channelId, bridgeId);
            }

            @Override
            public void onFailure(RestException e) {
                logger.debug("Error adding Channel {} to Bridge {}: {}", channelId, bridgeId, e.getMessage(), e);
            }
        });
    }

    /**
     * @param id channel or bridge id
     * @return the State object
     */
    private State lookupState(String id) {
        if (id != null && !id.isEmpty()) {
            synchronized (lookups) {
                if (lookups.containsKey(id)) {
                    return states.get(lookups.get(id));
                }
            }
        }
        return null;
    }

    /**
     * Remove the provided id from the lookups and the State object, if all ids cleared in the State object remove from the states
     * @param id the channel or bridge id
     */
    private void clear(String id) {
        if (id == null || id.trim().isEmpty()) {
            return;
        }
        String stateId = null;
        synchronized (lookups) {
            if (lookups.containsKey(id)) {
                stateId = lookups.remove(id);
            }
        }
        if (stateId != null) {
            synchronized (states) {
                State state = states.get(stateId);
                if (state != null) {
                    // state was found clear the relevant id...
                    if (id.equals(state.channel1)) {
                        state.channel1 = null;
                    } else if (id.equals(state.channel2)) {
                        state.channel2 = null;
                    } else if (id.equals(state.bridge)) {
                        state.bridge = null;
                    }
                    // all ids are cleared, remove from states...
                    if (state.channel1 == null && state.channel2 == null && state.bridge == null) {
                        states.remove(stateId);
                    }
                }
            }
        }
    }

    /**
     * An object to contain some ids to aid with processing the events.
     */
    static class State {
        private final String id = UUID.randomUUID().toString();
        private String from;
        private String to;
        private String channel1;
        private String channel2;
        private String bridge;
    }

    /**
     * Extension of the AriWSHelper to handle the events from Asterisk.
     */
    class Handler extends AriWSHelper {

        @Override
        public void onSuccess(Message message) {
            // execute message handling in a thead pool to offload the websocket event worker,
            // so we can get the next event and avoid a potential netty worker group starvation
            threadPool.execute(() -> super.onSuccess(message));
        }

        @Override
        protected void onStasisStart(StasisStart message) {
            // StasisStart is created by both the Stasis dialplan app and a call to the channels API in ARI,
            // so we check an argument set in the createChannel code and ignore
            logger.debug("onStasisStart, chan id: {}, name: {}", message.getChannel().getId(), message.getChannel().getName());
            if (message.getArgs() != null && !message.getArgs().isEmpty() && "me".equals(message.getArgs().get(0))) {
                logger.debug("started by me, not processing...");
                return;
            }
            // was not created by "me" so we assume it's from the Stasis dialplan app
            // create a State object and extract the info...
            State state = new State();
            state.from = message.getChannel().getCaller().getNumber();
            state.to = message.getChannel().getDialplan().getExten();
            // 902 & 903 are "virtual" dialplan extensions that should call endpoint 100 or 200
            if ("902".equals(state.to) || "903".equals(state.to)) {
                state.to = "902".equals(state.to) ? "100" : "200";
                logger.debug("Call received on virtual extension changed to {}", state.to);
            }
            state.channel1 = message.getChannel().getId();
            synchronized (states) {
                states.put(state.id, state);
            }
            synchronized (lookups) {
                lookups.put(state.channel1, state.id);
            }
            // add the inbound call channel to a bridge
            try {
                createBridgeAndAddChannel1(state);
            } catch (RestException e) {
                logger.error("Error creating bridge", e);
                hangupChannel(state.channel1);
            }
        }

        @Override
        protected void onChannelStateChange(ChannelStateChange message) {
            logger.debug("onChannelStateChange {} {}", message.getChannel().getId(), message.getChannel().getState());
            // find the State object for the channel
            State state = lookupState(message.getChannel().getId());
            if (state != null) {
                // some debug logging...
                if (logger.isDebugEnabled()) {
                    String chan = "?";
                    if (message.getChannel().getId().equals(state.channel1)) {
                        chan = "channel1";
                    } else if (message.getChannel().getId().equals(state.channel2)) {
                        chan = "channel2";
                    }
                    logger.debug("state id: {}, chan: {}", state.id, chan);
                }
                if ("Up".equals(message.getChannel().getState()) && message.getChannel().getId().equals(state.channel1)) {
                    // the channel state has changed to Up and is channel1 in the State object ...
                    // this occurs in the startCall flow, when the phone answers we want to create a bridge and add the channel
                    // then create a channel for the to number stored in the State object
                    logger.debug("Channel 1 answered, create bridge and add channel...");
                    try {
                        createBridgeAndAddChannel1(state);
                    } catch (RestException e) {
                        logger.error("Error creating bridge", e);
                        hangupChannel(state.channel1);
                    }
                } else if ("Ringing".equals(message.getChannel().getState()) && message.getChannel().getId().equals(state.channel2)) {
                    // the channel state has changed to Ringing and is channel2 in the State object ...
                    // execute ring on channel1 so the person knows its ringing on the other end...
                    logger.debug("Channel 2 is ringing, ring channel 1...");
                    try {
                        ari.channels().ring(state.channel1).execute();
                    } catch (RestException e) {
                        logger.error("Error ringing channel", e);
                    }
                } else if ("Up".equals(message.getChannel().getState()) && message.getChannel().getId().equals(state.channel2)) {
                    // the channel state has changed to Up and is channel2 in the State object ...
                    // add it to the bridge now both parties are connected and can communicate
                    logger.debug("Channel 2 answered, add to bridge...");
                    try {
                        addChannelToBridge(state.bridge, state.channel2);
                    } catch (RestException e) {
                        logger.error("Error adding channel to bridge", e);
                        hangupChannel(state.channel2);
                    }
                } else {
                    logger.debug("Not handling this channel state change");
                }
            } else {
                logger.error("Could not find state for channel {}", message.getChannel().getId());
            }
        }

        @Override
        protected void onChannelEnteredBridge(ChannelEnteredBridge message) {
            logger.debug("onChannelEnteredBridge {} {}", message.getBridge().getId(), message.getChannel().getId());
            State state = lookupState(message.getChannel().getId());
            if (message.getChannel().getId().equals(state.channel1)) {
                try {
                    state.channel2 = createChannel(state.to, state.from);
                    synchronized (lookups) {
                        lookups.put(state.channel2, state.id);
                    }
                    logger.debug("channel2: {}", state.channel2);
                } catch (RestException e) {
                    logger.error("Error creating channel2", e);
                    hangupChannel(state.channel1);
                }
            }
        }

        @Override
        protected void onChannelLeftBridge(ChannelLeftBridge message) {
            logger.debug("onChannelLeftBridge {} {}", message.getBridge().getId(), message.getChannel().getId());
            if (message.getBridge().getChannels().isEmpty()) {
                // the bridge is no longer needed - it's empty, so destroy it
                logger.debug("No more channels in bridge, destroying...");
                try {
                    ari.bridges().destroy(message.getBridge().getId()).execute();
                } catch (RestException e) {
                    logger.error("Error destroying bridge", e);
                }
            } else {
                // a channel left the bridge and there's another channel - so hangup the 1st channel as it's lonely here now...
                String chan = message.getBridge().getChannels().get(0);
                logger.debug("Hangup the other channel {}", chan);
                hangupChannel(chan);
            }
        }

        @Override
        protected void onChannelDestroyed(ChannelDestroyed message) {
            logger.debug("onChannelDestroyed {}", message.getChannel().getId());
            // ChannelDestroyed is usually when the endpoint doesn't answer
            // ChannelDestroyed & StasisEnd sometimes both occur, so we need to check the State is not null
            State state = lookupState(message.getChannel().getId());
            if (state != null) {
                // remove the channel from the lookups and State
                clear(message.getChannel().getId());
                if (message.getChannel().getId().equals(state.channel2) && state.channel1 != null) {
                    // this is channel2, so hangup the 1st channel as it's lonely here now...
                    hangupChannel(state.channel1);
                }
            }
        }

        @Override
        protected void onBridgeDestroyed(BridgeDestroyed message) {
            logger.debug("onBridgeDestroyed {}", message.getBridge().getId());
            // remove the bridge from the lookups and State
            clear(message.getBridge().getId());
        }

        @Override
        protected void onStasisEnd(StasisEnd message) {
            logger.debug("onStasisEnd {}", message.getChannel().getId());
            // remove the channel from the lookups and State
            clear(message.getChannel().getId());
        }
    }

}
