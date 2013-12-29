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

// void ring String
/**********************************************************
 * Indicate ringing to a channel.
 *********************************************************/
public void ring(String channelId) throws RestException;



// void stopSilence String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void stopSilence(String channelId, AriCallback<Void> callback);



// Variable getChannelVar String String
/**********************************************************
 * Get the value of a channel variable or function.
 *********************************************************/
public Variable getChannelVar(String channelId, String variable) throws RestException;



// void snoopChannel String String String String String AriCallback<Channel> callback
/**********************************************************
 * 
 *********************************************************/
public void snoopChannel(String channelId, String spy, String whisper, String app, String appArgs, AriCallback<Channel> callback);



// void getChannelVar String String AriCallback<Variable> callback
/**********************************************************
 * 
 *********************************************************/
public void getChannelVar(String channelId, String variable, AriCallback<Variable> callback);



// void ringStop String
/**********************************************************
 * Stop ringing indication on a channel if locally generated.
 *********************************************************/
public void ringStop(String channelId) throws RestException;



// void sendDTMF String String int int int int
/**********************************************************
 * Send provided DTMF to a given channel.
 *********************************************************/
public void sendDTMF(String channelId, String dtmf, int before, int between, int duration, int after) throws RestException;



// void answer String
/**********************************************************
 * Answer a channel.
 *********************************************************/
public void answer(String channelId) throws RestException;



// void unmute String String
/**********************************************************
 * Unmute a channel.
 *********************************************************/
public void unmute(String channelId, String direction) throws RestException;



// void hold String
/**********************************************************
 * Hold a channel.
 *********************************************************/
public void hold(String channelId) throws RestException;



// void setChannelVar String String String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void setChannelVar(String channelId, String variable, String value, AriCallback<Void> callback);



// void ringStop String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void ringStop(String channelId, AriCallback<Void> callback);



// void continueInDialplan String String String int AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void continueInDialplan(String channelId, String context, String extension, int priority, AriCallback<Void> callback);



// void list AriCallback<List<? extends Channel>> callback
/**********************************************************
 * 
 *********************************************************/
public void list(AriCallback<List<? extends Channel>> callback);



// void hangup String String
/**********************************************************
 * Delete (i.e. hangup) a channel.
 *********************************************************/
public void hangup(String channelId, String reason) throws RestException;



// void unmute String String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void unmute(String channelId, String direction, AriCallback<Void> callback);



// void stopMoh String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void stopMoh(String channelId, AriCallback<Void> callback);



// void setChannelVar String String String
/**********************************************************
 * Set the value of a channel variable or function.
 *********************************************************/
public void setChannelVar(String channelId, String variable, String value) throws RestException;



// void get String AriCallback<Channel> callback
/**********************************************************
 * 
 *********************************************************/
public void get(String channelId, AriCallback<Channel> callback);



// void ring String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void ring(String channelId, AriCallback<Void> callback);



// void startSilence String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void startSilence(String channelId, AriCallback<Void> callback);



// void unhold String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void unhold(String channelId, AriCallback<Void> callback);



// void record String String String int int String boolean String AriCallback<LiveRecording> callback
/**********************************************************
 * 
 *********************************************************/
public void record(String channelId, String name, String format, int maxDurationSeconds, int maxSilenceSeconds, String ifExists, boolean beep, String terminateOn, AriCallback<LiveRecording> callback);



// void play String String String int int AriCallback<Playback> callback
/**********************************************************
 * 
 *********************************************************/
public void play(String channelId, String media, String lang, int offsetms, int skipms, AriCallback<Playback> callback);



// Playback play String String String int int
/**********************************************************
 * Start playback of media.
 * The media URI may be any of a number of URI's. Currently sound: and recording: URI's are supported. This operation creates a playback resource that can be used to control the playback of media (pause, rewind, fast forward, etc.)
 *********************************************************/
public Playback play(String channelId, String media, String lang, int offsetms, int skipms) throws RestException;



// void hangup String String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void hangup(String channelId, String reason, AriCallback<Void> callback);



// void continueInDialplan String String String int
/**********************************************************
 * Exit application; continue execution in the dialplan.
 *********************************************************/
public void continueInDialplan(String channelId, String context, String extension, int priority) throws RestException;



// void answer String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void answer(String channelId, AriCallback<Void> callback);



// void mute String String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void mute(String channelId, String direction, AriCallback<Void> callback);



// Channel get String
/**********************************************************
 * Channel details.
 *********************************************************/
public Channel get(String channelId) throws RestException;



// void originate String String String long String String String int AriCallback<Channel> callback
/**********************************************************
 * 
 *********************************************************/
public void originate(String endpoint, String extension, String context, long priority, String app, String appArgs, String callerId, int timeout, AriCallback<Channel> callback);



// void stopSilence String
/**********************************************************
 * Stop playing silence to a channel.
 *********************************************************/
public void stopSilence(String channelId) throws RestException;



// void startSilence String
/**********************************************************
 * Play silence to a channel.
 * Using media operations such as /play on a channel playing silence in this manner will suspend silence without resuming automatically.
 *********************************************************/
public void startSilence(String channelId) throws RestException;



// void mute String String
/**********************************************************
 * Mute a channel.
 *********************************************************/
public void mute(String channelId, String direction) throws RestException;



// void stopMoh String
/**********************************************************
 * Stop playing music on hold to a channel.
 *********************************************************/
public void stopMoh(String channelId) throws RestException;



// List<? extends Channel> list
/**********************************************************
 * List all active channels in Asterisk.
 *********************************************************/
public List<? extends Channel> list() throws RestException;



// void sendDTMF String String int int int int AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void sendDTMF(String channelId, String dtmf, int before, int between, int duration, int after, AriCallback<Void> callback);



// Channel snoopChannel String String String String String
/**********************************************************
 * Start snooping.
 * Snoop (spy/whisper) on a specific channel.
 *********************************************************/
public Channel snoopChannel(String channelId, String spy, String whisper, String app, String appArgs) throws RestException;



// void startMoh String String
/**********************************************************
 * Play music on hold to a channel.
 * Using media operations such as /play on a channel playing MOH in this manner will suspend MOH without resuming automatically. If continuing music on hold is desired, the stasis application must reinitiate music on hold.
 *********************************************************/
public void startMoh(String channelId, String mohClass) throws RestException;



// void unhold String
/**********************************************************
 * Remove a channel from hold.
 *********************************************************/
public void unhold(String channelId) throws RestException;



// void hold String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void hold(String channelId, AriCallback<Void> callback);



// void startMoh String String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void startMoh(String channelId, String mohClass, AriCallback<Void> callback);



// LiveRecording record String String String int int String boolean String
/**********************************************************
 * Start a recording.
 * Record audio from a channel. Note that this will not capture audio sent to the channel. The bridge itself has a record feature if that's what you want.
 *********************************************************/
public LiveRecording record(String channelId, String name, String format, int maxDurationSeconds, int maxSilenceSeconds, String ifExists, boolean beep, String terminateOn) throws RestException;



// Channel originate String String String long String String String int
/**********************************************************
 * Create a new channel (originate).
 * The new channel is created immediately and a snapshot of it returned. If a Stasis application is provided it will be automatically subscribed to the originated channel for further events and updates.
 *********************************************************/
public Channel originate(String endpoint, String extension, String context, long priority, String app, String appArgs, String callerId, int timeout) throws RestException;


}
;
