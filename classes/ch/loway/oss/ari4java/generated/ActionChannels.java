package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;

public interface ActionChannels {

// Dialed dial String String String String int
/**********************************************************
 * Create a new channel (originate) and bridge to this channel.
 *********************************************************/
public Dialed dial(String channelId, String endpoint, String extension, String context, int timeout) throws RestException;



// void holdChannel String
/**********************************************************
 * Hold a channel.
 *********************************************************/
public void holdChannel(String channelId) throws RestException;



// void continueInDialplan String String String int
/**********************************************************
 * Exit application; continue execution in the dialplan.
 *********************************************************/
public void continueInDialplan(String channelId, String context, String extension, int priority) throws RestException;



// List<? extends Channel> getChannels
/**********************************************************
 * List active channels.
 *********************************************************/
public List<? extends Channel> getChannels() throws RestException;



// void deleteChannel String
/**********************************************************
 * Delete (i.e. hangup) a channel.
 *********************************************************/
public void deleteChannel(String channelId) throws RestException;



// Variable getChannelVar String String
/**********************************************************
 * Get the value of a channel variable or function.
 *********************************************************/
public Variable getChannelVar(String channelId, String variable) throws RestException;



// void originate String String String long String String String int
/**********************************************************
 * Create a new channel (originate).
 * The new channel is not created until the dialed party picks up. Not wanting to block this request indefinitely, this request returns immediately with a 204 No Content. When the channel is created, a StasisStart event is sent with the provided app and appArgs. In the event of a failure (timeout, busy, etc.), an OriginationFailed event is sent.
 *********************************************************/
public void originate(String endpoint, String extension, String context, long priority, String app, String appArgs, String callerId, int timeout) throws RestException;



// void answerChannel String
/**********************************************************
 * Answer a channel.
 *********************************************************/
public void answerChannel(String channelId) throws RestException;



// void unholdChannel String
/**********************************************************
 * Remove a channel from hold.
 *********************************************************/
public void unholdChannel(String channelId) throws RestException;



// void mohStartChannel String String
/**********************************************************
 * Play music on hold to a channel.
 * Using media operations such as playOnChannel on a channel playing MOH in this manner will suspend MOH without resuming automatically. If continuing music on hold is desired, the stasis application must reinitiate music on hold.
 *********************************************************/
public void mohStartChannel(String channelId, String mohClass) throws RestException;



// void muteChannel String String
/**********************************************************
 * Mute a channel.
 *********************************************************/
public void muteChannel(String channelId, String direction) throws RestException;



// void unmuteChannel String String
/**********************************************************
 * Unmute a channel.
 *********************************************************/
public void unmuteChannel(String channelId, String direction) throws RestException;



// Playback playOnChannel String String String int int
/**********************************************************
 * Start playback of media.
 * The media URI may be any of a number of URI's. You may use http: and https: URI's, as well as sound: and recording: URI's. This operation creates a playback resource that can be used to control the playback of media (pause, rewind, fast forward, etc.)
 *********************************************************/
public Playback playOnChannel(String channelId, String media, String lang, int offsetms, int skipms) throws RestException;



// Channel getChannel String
/**********************************************************
 * Channel details.
 *********************************************************/
public Channel getChannel(String channelId) throws RestException;



// LiveRecording recordChannel String String String int int String boolean String
/**********************************************************
 * Start a recording.
 * Record audio from a channel. Note that this will not capture audio sent to the channel. The bridge itself has a record feature if that's what you want.
 *********************************************************/
public LiveRecording recordChannel(String channelId, String name, String format, int maxDurationSeconds, int maxSilenceSeconds, String ifExists, boolean beep, String terminateOn) throws RestException;



// void mohStopChannel String
/**********************************************************
 * Stop playing music on hold to a channel.
 *********************************************************/
public void mohStopChannel(String channelId) throws RestException;



// void setChannelVar String String String
/**********************************************************
 * Set the value of a channel variable or function.
 *********************************************************/
public void setChannelVar(String channelId, String variable, String value) throws RestException;


}
;
