package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
//    Generated on: Tue Dec 19 09:55:48 CET 2017
// ----------------------------------------------------

import ch.loway.oss.ari4java.ARI;

public interface AriBuilder {
    public abstract ActionApplications actionApplications();
    public abstract ActionAsterisk actionAsterisk();
    public abstract ActionBridges actionBridges();
    public abstract ActionChannels actionChannels();
    public abstract ActionDeviceStates actionDeviceStates();
    public abstract ActionEndpoints actionEndpoints();
    public abstract ActionEvents actionEvents();
    public abstract ActionPlaybacks actionPlaybacks();
    public abstract ActionRecordings actionRecordings();
    public abstract ActionSounds actionSounds();
    public abstract Application application();
    public abstract ApplicationReplaced applicationReplaced();
    public abstract AsteriskInfo asteriskInfo();
    public abstract Bridge bridge();
    public abstract BridgeAttendedTransfer bridgeAttendedTransfer();
    public abstract BridgeBlindTransfer bridgeBlindTransfer();
    public abstract BridgeCreated bridgeCreated();
    public abstract BridgeDestroyed bridgeDestroyed();
    public abstract BridgeMerged bridgeMerged();
    public abstract BridgeVideoSourceChanged bridgeVideoSourceChanged();
    public abstract BuildInfo buildInfo();
    public abstract CallerID callerID();
    public abstract Channel channel();
    public abstract ChannelCallerId channelCallerId();
    public abstract ChannelConnectedLine channelConnectedLine();
    public abstract ChannelCreated channelCreated();
    public abstract ChannelDestroyed channelDestroyed();
    public abstract ChannelDialplan channelDialplan();
    public abstract ChannelDtmfReceived channelDtmfReceived();
    public abstract ChannelEnteredBridge channelEnteredBridge();
    public abstract ChannelHangupRequest channelHangupRequest();
    public abstract ChannelHold channelHold();
    public abstract ChannelLeftBridge channelLeftBridge();
    public abstract ChannelStateChange channelStateChange();
    public abstract ChannelTalkingFinished channelTalkingFinished();
    public abstract ChannelTalkingStarted channelTalkingStarted();
    public abstract ChannelUnhold channelUnhold();
    public abstract ChannelUserevent channelUserevent();
    public abstract ChannelVarset channelVarset();
    public abstract ConfigInfo configInfo();
    public abstract ConfigTuple configTuple();
    public abstract ContactInfo contactInfo();
    public abstract ContactStatusChange contactStatusChange();
    public abstract DeviceState deviceState();
    public abstract DeviceStateChanged deviceStateChanged();
    public abstract Dial dial();
    public abstract Dialed dialed();
    public abstract DialplanCEP dialplanCEP();
    public abstract Endpoint endpoint();
    public abstract EndpointStateChange endpointStateChange();
    public abstract Event event();
    public abstract FormatLangPair formatLangPair();
    public abstract LiveRecording liveRecording();
    public abstract LogChannel logChannel();
    public abstract Message message();
    public abstract MissingParams missingParams();
    public abstract Module module();
    public abstract Peer peer();
    public abstract PeerStatusChange peerStatusChange();
    public abstract Playback playback();
    public abstract PlaybackContinuing playbackContinuing();
    public abstract PlaybackFinished playbackFinished();
    public abstract PlaybackStarted playbackStarted();
    public abstract RecordingFailed recordingFailed();
    public abstract RecordingFinished recordingFinished();
    public abstract RecordingStarted recordingStarted();
    public abstract SetId setId();
    public abstract Sound sound();
    public abstract StasisEnd stasisEnd();
    public abstract StasisStart stasisStart();
    public abstract StatusInfo statusInfo();
    public abstract StoredRecording storedRecording();
    public abstract SystemInfo systemInfo();
    public abstract TextMessage textMessage();
    public abstract TextMessageReceived textMessageReceived();
    public abstract TextMessageVariable textMessageVariable();
    public abstract Variable variable();


	public abstract ARI.ClassFactory getClassFactory();


}

