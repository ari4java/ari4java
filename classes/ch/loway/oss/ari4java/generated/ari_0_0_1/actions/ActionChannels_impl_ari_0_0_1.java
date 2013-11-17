package ch.loway.oss.ari4java.generated.ari_0_0_1.actions;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.BaseAriAction;
import ch.loway.oss.ari4java.tools.RestException;
import ch.loway.oss.ari4java.tools.AriCallback;
import com.fasterxml.jackson.core.type.TypeReference;
import ch.loway.oss.ari4java.generated.ari_0_0_1.models.*;

public class ActionChannels_impl_ari_0_0_1 extends BaseAriAction  implements ActionChannels {
/**********************************************************
 * Active channels
 * 
 * List active channels.
 *********************************************************/
private void buildGetChannels() {
reset();
url = "/channels";
}

@Override
public List<? extends Channel> getChannels() throws RestException {
buildGetChannels();
String json = httpActionSync();
return deserializeJson( json, new TypeReference<List<Channel_impl_ari_0_0_1>>() {} ); 
}

@Override
public void getChannels(AriCallback<List<? extends Channel>> callback) {
buildGetChannels();
httpActionAsync(callback, new TypeReference<List<Channel_impl_ari_0_0_1>>() {});
}

/**********************************************************
 * Active channels
 * 
 * Create a new channel (originate).
 * The new channel is not created until the dialed party picks up. Not wanting to block this request indefinitely, this request returns immediately with a 204 No Content. When the channel is created, a StasisStart event is sent with the provided app and appArgs. In the event of a failure (timeout, busy, etc.), an OriginationFailed event is sent.
 *********************************************************/
private void buildOriginate(String endpoint, String extension, String context, long priority, String app, String appArgs, String callerId, int timeout) {
reset();
url = "/channels";
lParamQuery.add( BaseAriAction.HttpParam.build( "endpoint", endpoint) );
lParamQuery.add( BaseAriAction.HttpParam.build( "extension", extension) );
lParamQuery.add( BaseAriAction.HttpParam.build( "context", context) );
lParamQuery.add( BaseAriAction.HttpParam.build( "priority", priority) );
lParamQuery.add( BaseAriAction.HttpParam.build( "app", app) );
lParamQuery.add( BaseAriAction.HttpParam.build( "appArgs", appArgs) );
lParamQuery.add( BaseAriAction.HttpParam.build( "callerId", callerId) );
lParamQuery.add( BaseAriAction.HttpParam.build( "timeout", timeout) );
lE.add( BaseAriAction.HttpResponse.build( 400, "Invalid parameters for originating a channel.") );
}

@Override
public void originate(String endpoint, String extension, String context, long priority, String app, String appArgs, String callerId, int timeout) throws RestException {
buildOriginate(endpoint, extension, context, priority, app, appArgs, callerId, timeout);
String json = httpActionSync();
}

@Override
public void originate(String endpoint, String extension, String context, long priority, String app, String appArgs, String callerId, int timeout, AriCallback<Void> callback) {
buildOriginate(endpoint, extension, context, priority, app, appArgs, callerId, timeout);
httpActionAsync(callback);
}

/**********************************************************
 * Active channel
 * 
 * Channel details.
 *********************************************************/
private void buildGetChannel(String channelId) {
reset();
url = "/channels/" + channelId + "";
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
}

@Override
public Channel getChannel(String channelId) throws RestException {
buildGetChannel(channelId);
String json = httpActionSync();
return deserializeJson( json, Channel_impl_ari_0_0_1.class ); 
}

@Override
public void getChannel(String channelId, AriCallback<Channel> callback) {
buildGetChannel(channelId);
httpActionAsync(callback, Channel_impl_ari_0_0_1.class);
}

/**********************************************************
 * Active channel
 * 
 * Delete (i.e. hangup) a channel.
 *********************************************************/
private void buildDeleteChannel(String channelId) {
reset();
url = "/channels/" + channelId + "";
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
}

@Override
public void deleteChannel(String channelId) throws RestException {
buildDeleteChannel(channelId);
String json = httpActionSync();
}

@Override
public void deleteChannel(String channelId, AriCallback<Void> callback) {
buildDeleteChannel(channelId);
httpActionAsync(callback);
}

/**********************************************************
 * Create a new channel (originate) and bridge to this channel
 * 
 * Create a new channel (originate) and bridge to this channel.
 *********************************************************/
private void buildDial(String channelId, String endpoint, String extension, String context, int timeout) {
reset();
url = "/channels/" + channelId + "/dial";
lParamQuery.add( BaseAriAction.HttpParam.build( "endpoint", endpoint) );
lParamQuery.add( BaseAriAction.HttpParam.build( "extension", extension) );
lParamQuery.add( BaseAriAction.HttpParam.build( "context", context) );
lParamQuery.add( BaseAriAction.HttpParam.build( "timeout", timeout) );
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
}

@Override
public Dialed dial(String channelId, String endpoint, String extension, String context, int timeout) throws RestException {
buildDial(channelId, endpoint, extension, context, timeout);
String json = httpActionSync();
return deserializeJson( json, Dialed_impl_ari_0_0_1.class ); 
}

@Override
public void dial(String channelId, String endpoint, String extension, String context, int timeout, AriCallback<Dialed> callback) {
buildDial(channelId, endpoint, extension, context, timeout);
httpActionAsync(callback, Dialed_impl_ari_0_0_1.class);
}

/**********************************************************
 * Exit application; continue execution in the dialplan
 * 
 * Exit application; continue execution in the dialplan.
 *********************************************************/
private void buildContinueInDialplan(String channelId, String context, String extension, int priority) {
reset();
url = "/channels/" + channelId + "/continue";
lParamQuery.add( BaseAriAction.HttpParam.build( "context", context) );
lParamQuery.add( BaseAriAction.HttpParam.build( "extension", extension) );
lParamQuery.add( BaseAriAction.HttpParam.build( "priority", priority) );
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
}

@Override
public void continueInDialplan(String channelId, String context, String extension, int priority) throws RestException {
buildContinueInDialplan(channelId, context, extension, priority);
String json = httpActionSync();
}

@Override
public void continueInDialplan(String channelId, String context, String extension, int priority, AriCallback<Void> callback) {
buildContinueInDialplan(channelId, context, extension, priority);
httpActionAsync(callback);
}

/**********************************************************
 * Answer a channel
 * 
 * Answer a channel.
 *********************************************************/
private void buildAnswerChannel(String channelId) {
reset();
url = "/channels/" + channelId + "/answer";
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
}

@Override
public void answerChannel(String channelId) throws RestException {
buildAnswerChannel(channelId);
String json = httpActionSync();
}

@Override
public void answerChannel(String channelId, AriCallback<Void> callback) {
buildAnswerChannel(channelId);
httpActionAsync(callback);
}

/**********************************************************
 * Mute a channel
 * 
 * Mute a channel.
 *********************************************************/
private void buildMuteChannel(String channelId, String direction) {
reset();
url = "/channels/" + channelId + "/mute";
lParamQuery.add( BaseAriAction.HttpParam.build( "direction", direction) );
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
}

@Override
public void muteChannel(String channelId, String direction) throws RestException {
buildMuteChannel(channelId, direction);
String json = httpActionSync();
}

@Override
public void muteChannel(String channelId, String direction, AriCallback<Void> callback) {
buildMuteChannel(channelId, direction);
httpActionAsync(callback);
}

/**********************************************************
 * Unmute a channel
 * 
 * Unmute a channel.
 *********************************************************/
private void buildUnmuteChannel(String channelId, String direction) {
reset();
url = "/channels/" + channelId + "/unmute";
lParamQuery.add( BaseAriAction.HttpParam.build( "direction", direction) );
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
}

@Override
public void unmuteChannel(String channelId, String direction) throws RestException {
buildUnmuteChannel(channelId, direction);
String json = httpActionSync();
}

@Override
public void unmuteChannel(String channelId, String direction, AriCallback<Void> callback) {
buildUnmuteChannel(channelId, direction);
httpActionAsync(callback);
}

/**********************************************************
 * Put a channel on hold
 * 
 * Hold a channel.
 *********************************************************/
private void buildHoldChannel(String channelId) {
reset();
url = "/channels/" + channelId + "/hold";
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
}

@Override
public void holdChannel(String channelId) throws RestException {
buildHoldChannel(channelId);
String json = httpActionSync();
}

@Override
public void holdChannel(String channelId, AriCallback<Void> callback) {
buildHoldChannel(channelId);
httpActionAsync(callback);
}

/**********************************************************
 * Remove a channel from hold
 * 
 * Remove a channel from hold.
 *********************************************************/
private void buildUnholdChannel(String channelId) {
reset();
url = "/channels/" + channelId + "/unhold";
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
}

@Override
public void unholdChannel(String channelId) throws RestException {
buildUnholdChannel(channelId);
String json = httpActionSync();
}

@Override
public void unholdChannel(String channelId, AriCallback<Void> callback) {
buildUnholdChannel(channelId);
httpActionAsync(callback);
}

/**********************************************************
 * Play music on hold to a channel
 * 
 * Play music on hold to a channel.
 * Using media operations such as playOnChannel on a channel playing MOH in this manner will suspend MOH without resuming automatically. If continuing music on hold is desired, the stasis application must reinitiate music on hold.
 *********************************************************/
private void buildMohStartChannel(String channelId, String mohClass) {
reset();
url = "/channels/" + channelId + "/mohstart";
lParamQuery.add( BaseAriAction.HttpParam.build( "mohClass", mohClass) );
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
}

@Override
public void mohStartChannel(String channelId, String mohClass) throws RestException {
buildMohStartChannel(channelId, mohClass);
String json = httpActionSync();
}

@Override
public void mohStartChannel(String channelId, String mohClass, AriCallback<Void> callback) {
buildMohStartChannel(channelId, mohClass);
httpActionAsync(callback);
}

/**********************************************************
 * Stop playing music on hold to a channel
 * 
 * Stop playing music on hold to a channel.
 *********************************************************/
private void buildMohStopChannel(String channelId) {
reset();
url = "/channels/" + channelId + "/mohstop";
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
}

@Override
public void mohStopChannel(String channelId) throws RestException {
buildMohStopChannel(channelId);
String json = httpActionSync();
}

@Override
public void mohStopChannel(String channelId, AriCallback<Void> callback) {
buildMohStopChannel(channelId);
httpActionAsync(callback);
}

/**********************************************************
 * Play media to a channel
 * 
 * Start playback of media.
 * The media URI may be any of a number of URI's. You may use http: and https: URI's, as well as sound: and recording: URI's. This operation creates a playback resource that can be used to control the playback of media (pause, rewind, fast forward, etc.)
 *********************************************************/
private void buildPlayOnChannel(String channelId, String media, String lang, int offsetms, int skipms) {
reset();
url = "/channels/" + channelId + "/play";
lParamQuery.add( BaseAriAction.HttpParam.build( "media", media) );
lParamQuery.add( BaseAriAction.HttpParam.build( "lang", lang) );
lParamQuery.add( BaseAriAction.HttpParam.build( "offsetms", offsetms) );
lParamQuery.add( BaseAriAction.HttpParam.build( "skipms", skipms) );
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
}

@Override
public Playback playOnChannel(String channelId, String media, String lang, int offsetms, int skipms) throws RestException {
buildPlayOnChannel(channelId, media, lang, offsetms, skipms);
String json = httpActionSync();
return deserializeJson( json, Playback_impl_ari_0_0_1.class ); 
}

@Override
public void playOnChannel(String channelId, String media, String lang, int offsetms, int skipms, AriCallback<Playback> callback) {
buildPlayOnChannel(channelId, media, lang, offsetms, skipms);
httpActionAsync(callback, Playback_impl_ari_0_0_1.class);
}

/**********************************************************
 * Record audio from a channel
 * 
 * Start a recording.
 * Record audio from a channel. Note that this will not capture audio sent to the channel. The bridge itself has a record feature if that's what you want.
 *********************************************************/
private void buildRecordChannel(String channelId, String name, String format, int maxDurationSeconds, int maxSilenceSeconds, String ifExists, boolean beep, String terminateOn) {
reset();
url = "/channels/" + channelId + "/record";
lParamQuery.add( BaseAriAction.HttpParam.build( "name", name) );
lParamQuery.add( BaseAriAction.HttpParam.build( "format", format) );
lParamQuery.add( BaseAriAction.HttpParam.build( "maxDurationSeconds", maxDurationSeconds) );
lParamQuery.add( BaseAriAction.HttpParam.build( "maxSilenceSeconds", maxSilenceSeconds) );
lParamQuery.add( BaseAriAction.HttpParam.build( "ifExists", ifExists) );
lParamQuery.add( BaseAriAction.HttpParam.build( "beep", beep) );
lParamQuery.add( BaseAriAction.HttpParam.build( "terminateOn", terminateOn) );
lE.add( BaseAriAction.HttpResponse.build( 400, "Invalid parameters") );
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel is not in a Stasis application; the channel is currently bridged with other channels; A recording with the same name is currently in progress.") );
}

@Override
public LiveRecording recordChannel(String channelId, String name, String format, int maxDurationSeconds, int maxSilenceSeconds, String ifExists, boolean beep, String terminateOn) throws RestException {
buildRecordChannel(channelId, name, format, maxDurationSeconds, maxSilenceSeconds, ifExists, beep, terminateOn);
String json = httpActionSync();
return deserializeJson( json, LiveRecording_impl_ari_0_0_1.class ); 
}

@Override
public void recordChannel(String channelId, String name, String format, int maxDurationSeconds, int maxSilenceSeconds, String ifExists, boolean beep, String terminateOn, AriCallback<LiveRecording> callback) {
buildRecordChannel(channelId, name, format, maxDurationSeconds, maxSilenceSeconds, ifExists, beep, terminateOn);
httpActionAsync(callback, LiveRecording_impl_ari_0_0_1.class);
}

/**********************************************************
 * Variables on a channel
 * 
 * Get the value of a channel variable or function.
 *********************************************************/
private void buildGetChannelVar(String channelId, String variable) {
reset();
url = "/channels/" + channelId + "/variable";
lParamQuery.add( BaseAriAction.HttpParam.build( "variable", variable) );
lE.add( BaseAriAction.HttpResponse.build( 400, "Missing variable parameter.") );
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
}

@Override
public Variable getChannelVar(String channelId, String variable) throws RestException {
buildGetChannelVar(channelId, variable);
String json = httpActionSync();
return deserializeJson( json, Variable_impl_ari_0_0_1.class ); 
}

@Override
public void getChannelVar(String channelId, String variable, AriCallback<Variable> callback) {
buildGetChannelVar(channelId, variable);
httpActionAsync(callback, Variable_impl_ari_0_0_1.class);
}

/**********************************************************
 * Variables on a channel
 * 
 * Set the value of a channel variable or function.
 *********************************************************/
private void buildSetChannelVar(String channelId, String variable, String value) {
reset();
url = "/channels/" + channelId + "/variable";
lParamQuery.add( BaseAriAction.HttpParam.build( "variable", variable) );
lParamQuery.add( BaseAriAction.HttpParam.build( "value", value) );
lE.add( BaseAriAction.HttpResponse.build( 400, "Missing variable parameter.") );
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
}

@Override
public void setChannelVar(String channelId, String variable, String value) throws RestException {
buildSetChannelVar(channelId, variable, value);
String json = httpActionSync();
}

@Override
public void setChannelVar(String channelId, String variable, String value, AriCallback<Void> callback) {
buildSetChannelVar(channelId, variable, value);
httpActionAsync(callback);
}

};

