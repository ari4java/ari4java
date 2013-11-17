package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;
import ch.loway.oss.ari4java.tools.AriCallback;

public interface ActionChannels {

// void originate String String String long String String String int AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void originate(String endpoint, String extension, String context, long priority, String app, String appArgs, String callerId, int timeout, AriCallback<Void> callback);



// void deleteChannel String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void deleteChannel(String channelId, AriCallback<Void> callback);



// void deleteChannel String
/**********************************************************
 * Delete (i.e. hangup) a channel.
 *********************************************************/
public void deleteChannel(String channelId) throws RestException;



// void getChannels AriCallback<List<? extends Channel>> callback
/**********************************************************
 * 
 *********************************************************/
public void getChannels(AriCallback<List<? extends Channel>> callback);



// Variable getChannelVar String String
/**********************************************************
 * Get the value of a channel variable or function.
 *********************************************************/
public Variable getChannelVar(String channelId, String variable) throws RestException;



// void unholdChannel String
/**********************************************************
 * Remove a channel from hold.
 *********************************************************/
public void unholdChannel(String channelId) throws RestException;



// void answerChannel String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void answerChannel(String channelId, AriCallback<Void> callback);



// void getChannelVar String String AriCallback<Variable> callback
/**********************************************************
 * 
 *********************************************************/
public void getChannelVar(String channelId, String variable, AriCallback<Variable> callback);



// void mohStartChannel String String
/**********************************************************
 * Play music on hold to a channel.
 * Using media operations such as playOnChannel on a channel playing MOH in this manner will suspend MOH without resuming automatically. If continuing music on hold is desired, the stasis application must reinitiate music on hold.
 *********************************************************/
public void mohStartChannel(String channelId, String mohClass) throws RestException;



// void unmuteChannel String String
/**********************************************************
 * Unmute a channel.
 *********************************************************/
public void unmuteChannel(String channelId, String direction) throws RestException;



// void holdChannel String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void holdChannel(String channelId, AriCallback<Void> callback);



// void setChannelVar String String String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void setChannelVar(String channelId, String variable, String value, AriCallback<Void> callback);



// Playback playOnChannel String String String int int
/**********************************************************
 * Start playback of media.
 * The media URI may be any of a number of URI's. You may use http: and https: URI's, as well as sound: and recording: URI's. This operation creates a playback resource that can be used to control the playback of media (pause, rewind, fast forward, etc.)
 *********************************************************/
public Playback playOnChannel(String channelId, String media, String lang, int offsetms, int skipms) throws RestException;



// void continueInDialplan String String String int AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void continueInDialplan(String channelId, String context, String extension, int priority, AriCallback<Void> callback);



// LiveRecording recordChannel String String String int int String boolean String
/**********************************************************
 * Start a recording.
 * Record audio from a channel. Note that this will not capture audio sent to the channel. The bridge itself has a record feature if that's what you want.
 *********************************************************/
public LiveRecording recordChannel(String channelId, String name, String format, int maxDurationSeconds, int maxSilenceSeconds, String ifExists, boolean beep, String terminateOn) throws RestException;



// void dial String String String String int AriCallback<Dialed> callback
/**********************************************************
 * 
 *********************************************************/
public void dial(String channelId, String endpoint, String extension, String context, int timeout, AriCallback<Dialed> callback);



// void setChannelVar String String String
/**********************************************************
 * Set the value of a channel variable or function.
 *********************************************************/
public void setChannelVar(String channelId, String variable, String value) throws RestException;



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



// void muteChannel String String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void muteChannel(String channelId, String direction, AriCallback<Void> callback);



// void continueInDialplan String String String int
/**********************************************************
 * Exit application; continue execution in the dialplan.
 *********************************************************/
public void continueInDialplan(String channelId, String context, String extension, int priority) throws RestException;



// void mohStartChannel String String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void mohStartChannel(String channelId, String mohClass, AriCallback<Void> callback);



// List<? extends Channel> getChannels
/**********************************************************
 * List active channels.
 *********************************************************/
public List<? extends Channel> getChannels() throws RestException;



// void unmuteChannel String String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void unmuteChannel(String channelId, String direction, AriCallback<Void> callback);



// void getChannel String AriCallback<Channel> callback
/**********************************************************
 * 
 *********************************************************/
public void getChannel(String channelId, AriCallback<Channel> callback);



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



// void recordChannel String String String int int String boolean String AriCallback<LiveRecording> callback
/**********************************************************
 * 
 *********************************************************/
public void recordChannel(String channelId, String name, String format, int maxDurationSeconds, int maxSilenceSeconds, String ifExists, boolean beep, String terminateOn, AriCallback<LiveRecording> callback);



// void unholdChannel String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void unholdChannel(String channelId, AriCallback<Void> callback);



// void muteChannel String String
/**********************************************************
 * Mute a channel.
 *********************************************************/
public void muteChannel(String channelId, String direction) throws RestException;



// void mohStopChannel String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void mohStopChannel(String channelId, AriCallback<Void> callback);



// void playOnChannel String String String int int AriCallback<Playback> callback
/**********************************************************
 * 
 *********************************************************/
public void playOnChannel(String channelId, String media, String lang, int offsetms, int skipms, AriCallback<Playback> callback);



// Channel getChannel String
/**********************************************************
 * Channel details.
 *********************************************************/
public Channel getChannel(String channelId) throws RestException;



// void mohStopChannel String
/**********************************************************
 * Stop playing music on hold to a channel.
 *********************************************************/
public void mohStopChannel(String channelId) throws RestException;


}
;
