
package ch.loway.oss.ari4java;

import ch.loway.oss.ari4java.generated.ActionApplications;
import ch.loway.oss.ari4java.generated.ActionAsterisk;
import ch.loway.oss.ari4java.generated.ActionBridges;
import ch.loway.oss.ari4java.generated.ActionChannels;
import ch.loway.oss.ari4java.generated.ActionDeviceStates;
import ch.loway.oss.ari4java.generated.ActionEndpoints;
import ch.loway.oss.ari4java.generated.ActionEvents;
import ch.loway.oss.ari4java.generated.ActionPlaybacks;
import ch.loway.oss.ari4java.generated.ActionRecordings;
import ch.loway.oss.ari4java.generated.ActionSounds;
import ch.loway.oss.ari4java.generated.Application;
import ch.loway.oss.ari4java.generated.ApplicationReplaced;
import ch.loway.oss.ari4java.generated.AriBuilder;
import ch.loway.oss.ari4java.generated.AsteriskInfo;
import ch.loway.oss.ari4java.generated.Bridge;
import ch.loway.oss.ari4java.generated.BridgeCreated;
import ch.loway.oss.ari4java.generated.BridgeDestroyed;
import ch.loway.oss.ari4java.generated.BridgeMerged;
import ch.loway.oss.ari4java.generated.BuildInfo;
import ch.loway.oss.ari4java.generated.CallerID;
import ch.loway.oss.ari4java.generated.Channel;
import ch.loway.oss.ari4java.generated.ChannelCallerId;
import ch.loway.oss.ari4java.generated.ChannelCreated;
import ch.loway.oss.ari4java.generated.ChannelDestroyed;
import ch.loway.oss.ari4java.generated.ChannelDialplan;
import ch.loway.oss.ari4java.generated.ChannelDtmfReceived;
import ch.loway.oss.ari4java.generated.ChannelEnteredBridge;
import ch.loway.oss.ari4java.generated.ChannelHangupRequest;
import ch.loway.oss.ari4java.generated.ChannelLeftBridge;
import ch.loway.oss.ari4java.generated.ChannelStateChange;
import ch.loway.oss.ari4java.generated.ChannelUserevent;
import ch.loway.oss.ari4java.generated.ChannelVarset;
import ch.loway.oss.ari4java.generated.ConfigInfo;
import ch.loway.oss.ari4java.generated.DeviceState;
import ch.loway.oss.ari4java.generated.DeviceStateChanged;
import ch.loway.oss.ari4java.generated.Dial;
import ch.loway.oss.ari4java.generated.Dialed;
import ch.loway.oss.ari4java.generated.DialplanCEP;
import ch.loway.oss.ari4java.generated.Endpoint;
import ch.loway.oss.ari4java.generated.EndpointStateChange;
import ch.loway.oss.ari4java.generated.Event;
import ch.loway.oss.ari4java.generated.FormatLangPair;
import ch.loway.oss.ari4java.generated.LiveRecording;
import ch.loway.oss.ari4java.generated.Message;
import ch.loway.oss.ari4java.generated.MissingParams;
import ch.loway.oss.ari4java.generated.Playback;
import ch.loway.oss.ari4java.generated.PlaybackFinished;
import ch.loway.oss.ari4java.generated.PlaybackStarted;
import ch.loway.oss.ari4java.generated.RecordingFailed;
import ch.loway.oss.ari4java.generated.RecordingFinished;
import ch.loway.oss.ari4java.generated.RecordingStarted;
import ch.loway.oss.ari4java.generated.SetId;
import ch.loway.oss.ari4java.generated.Sound;
import ch.loway.oss.ari4java.generated.StasisEnd;
import ch.loway.oss.ari4java.generated.StasisStart;
import ch.loway.oss.ari4java.generated.StatusInfo;
import ch.loway.oss.ari4java.generated.StoredRecording;
import ch.loway.oss.ari4java.generated.SystemInfo;
import ch.loway.oss.ari4java.generated.Variable;
import ch.loway.oss.ari4java.generated.ari_0_0_1.AriBuilder_impl_ari_0_0_1;
import ch.loway.oss.ari4java.generated.ari_1_0_0.AriBuilder_impl_ari_1_0_0;

/**
 * The version of ARI to be used.
 * 
 * @author lenz
 */
public enum AriVersion {


    ARI_0_0_1 ( new AriBuilder_impl_ari_0_0_1() ),   /** Asterisk 12 beta 1 */
    ARI_1_0_0 ( new AriBuilder_impl_ari_1_0_0() ),   /** Asterisk 12 */
    IM_FEELING_LUCKY ( null );

    final AriBuilder builder;

    private AriVersion( AriBuilder ab ) {
        builder = ab;
    }

    /**
     * You cannot get a builder for IM_FEELING_LUCKY or similar.
     * If you try to do this, it s a logical error and you get an exception.
     *
     * @return the builder object
     * @throws IllegalArgumentException
     */

    public AriBuilder builder() {
        if ( builder == null ) {
            throw new IllegalArgumentException("This version has no builder. Library error for :" + this.name());
        } else {
            return builder;
        }
    }


}

