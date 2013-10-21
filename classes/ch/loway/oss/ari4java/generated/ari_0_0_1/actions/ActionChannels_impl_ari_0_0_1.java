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
import com.fasterxml.jackson.core.type.TypeReference;
import ch.loway.oss.ari4java.generated.ari_0_0_1.models.*;

public class ActionChannels_impl_ari_0_0_1 extends BaseAriAction  implements ActionChannels {
/**********************************************************
 * Active channels
 * 
 * List active channels.
 *********************************************************/
public List<Channel> getChannels() throws RestException {
String url = "/channels";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
String json = httpAction( url, "GET", lParamQuery, lParamForm, lE);
return (List<Channel>) deserializeJson( json, new TypeReference<List<Channel_impl_ari_0_0_1>>() {} ); 
}

/**********************************************************
 * Active channels
 * 
 * Create a new channel (originate).
 * The new channel is not created until the dialed party picks up. Not wanting to block this request indefinitely, this request returns immediately with a 204 No Content. When the channel is created, a StasisStart event is sent with the provided app and appArgs. In the event of a failure (timeout, busy, etc.), an OriginationFailed event is sent.
 *********************************************************/
public void originate(String endpoint, String extension, String context, long priority, String app, String appArgs, String callerId, int timeout) throws RestException {
String url = "/channels";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lParamQuery.add( BaseAriAction.HttpParam.build( "endpoint", endpoint) );
lParamQuery.add( BaseAriAction.HttpParam.build( "extension", extension) );
lParamQuery.add( BaseAriAction.HttpParam.build( "context", context) );
lParamQuery.add( BaseAriAction.HttpParam.build( "priority", priority) );
lParamQuery.add( BaseAriAction.HttpParam.build( "app", app) );
lParamQuery.add( BaseAriAction.HttpParam.build( "appArgs", appArgs) );
lParamQuery.add( BaseAriAction.HttpParam.build( "callerId", callerId) );
lParamQuery.add( BaseAriAction.HttpParam.build( "timeout", timeout) );
lE.add( BaseAriAction.HttpResponse.build( 400, "Invalid parameters for originating a channel.") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
}

/**********************************************************
 * Active channel
 * 
 * Channel details.
 *********************************************************/
public Channel getChannel(String channelId) throws RestException {
String url = "/channels/" + channelId + "";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
String json = httpAction( url, "GET", lParamQuery, lParamForm, lE);
return (Channel) deserializeJson( json, Channel_impl_ari_0_0_1.class ); 
}

/**********************************************************
 * Active channel
 * 
 * Delete (i.e. hangup) a channel.
 *********************************************************/
public void deleteChannel(String channelId) throws RestException {
String url = "/channels/" + channelId + "";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
String json = httpAction( url, "DELETE", lParamQuery, lParamForm, lE);
}

/**********************************************************
 * Create a new channel (originate) and bridge to this channel
 * 
 * Create a new channel (originate) and bridge to this channel.
 *********************************************************/
public Dialed dial(String channelId, String endpoint, String extension, String context, int timeout) throws RestException {
String url = "/channels/" + channelId + "/dial";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lParamQuery.add( BaseAriAction.HttpParam.build( "endpoint", endpoint) );
lParamQuery.add( BaseAriAction.HttpParam.build( "extension", extension) );
lParamQuery.add( BaseAriAction.HttpParam.build( "context", context) );
lParamQuery.add( BaseAriAction.HttpParam.build( "timeout", timeout) );
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
return (Dialed) deserializeJson( json, Dialed_impl_ari_0_0_1.class ); 
}

/**********************************************************
 * Exit application; continue execution in the dialplan
 * 
 * Exit application; continue execution in the dialplan.
 *********************************************************/
public void continueInDialplan(String channelId, String context, String extension, int priority) throws RestException {
String url = "/channels/" + channelId + "/continue";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lParamQuery.add( BaseAriAction.HttpParam.build( "context", context) );
lParamQuery.add( BaseAriAction.HttpParam.build( "extension", extension) );
lParamQuery.add( BaseAriAction.HttpParam.build( "priority", priority) );
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
}

/**********************************************************
 * Answer a channel
 * 
 * Answer a channel.
 *********************************************************/
public void answerChannel(String channelId) throws RestException {
String url = "/channels/" + channelId + "/answer";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
}

/**********************************************************
 * Mute a channel
 * 
 * Mute a channel.
 *********************************************************/
public void muteChannel(String channelId, String direction) throws RestException {
String url = "/channels/" + channelId + "/mute";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lParamQuery.add( BaseAriAction.HttpParam.build( "direction", direction) );
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
}

/**********************************************************
 * Unmute a channel
 * 
 * Unmute a channel.
 *********************************************************/
public void unmuteChannel(String channelId, String direction) throws RestException {
String url = "/channels/" + channelId + "/unmute";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lParamQuery.add( BaseAriAction.HttpParam.build( "direction", direction) );
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
}

/**********************************************************
 * Put a channel on hold
 * 
 * Hold a channel.
 *********************************************************/
public void holdChannel(String channelId) throws RestException {
String url = "/channels/" + channelId + "/hold";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
}

/**********************************************************
 * Remove a channel from hold
 * 
 * Remove a channel from hold.
 *********************************************************/
public void unholdChannel(String channelId) throws RestException {
String url = "/channels/" + channelId + "/unhold";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
}

/**********************************************************
 * Play music on hold to a channel
 * 
 * Play music on hold to a channel.
 * Using media operations such as playOnChannel on a channel playing MOH in this manner will suspend MOH without resuming automatically. If continuing music on hold is desired, the stasis application must reinitiate music on hold.
 *********************************************************/
public void mohStartChannel(String channelId, String mohClass) throws RestException {
String url = "/channels/" + channelId + "/mohstart";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lParamQuery.add( BaseAriAction.HttpParam.build( "mohClass", mohClass) );
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
}

/**********************************************************
 * Stop playing music on hold to a channel
 * 
 * Stop playing music on hold to a channel.
 *********************************************************/
public void mohStopChannel(String channelId) throws RestException {
String url = "/channels/" + channelId + "/mohstop";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
}

/**********************************************************
 * Play media to a channel
 * 
 * Start playback of media.
 * The media URI may be any of a number of URI's. You may use http: and https: URI's, as well as sound: and recording: URI's. This operation creates a playback resource that can be used to control the playback of media (pause, rewind, fast forward, etc.)
 *********************************************************/
public Playback playOnChannel(String channelId, String media, String lang, int offsetms, int skipms) throws RestException {
String url = "/channels/" + channelId + "/play";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lParamQuery.add( BaseAriAction.HttpParam.build( "media", media) );
lParamQuery.add( BaseAriAction.HttpParam.build( "lang", lang) );
lParamQuery.add( BaseAriAction.HttpParam.build( "offsetms", offsetms) );
lParamQuery.add( BaseAriAction.HttpParam.build( "skipms", skipms) );
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
return (Playback) deserializeJson( json, Playback_impl_ari_0_0_1.class ); 
}

/**********************************************************
 * Record audio from a channel
 * 
 * Start a recording.
 * Record audio from a channel. Note that this will not capture audio sent to the channel. The bridge itself has a record feature if that's what you want.
 *********************************************************/
public LiveRecording recordChannel(String channelId, String name, String format, int maxDurationSeconds, int maxSilenceSeconds, String ifExists, boolean beep, String terminateOn) throws RestException {
String url = "/channels/" + channelId + "/record";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
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
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
return (LiveRecording) deserializeJson( json, LiveRecording_impl_ari_0_0_1.class ); 
}

/**********************************************************
 * Variables on a channel
 * 
 * Get the value of a channel variable or function.
 *********************************************************/
public Variable getChannelVar(String channelId, String variable) throws RestException {
String url = "/channels/" + channelId + "/variable";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lParamQuery.add( BaseAriAction.HttpParam.build( "variable", variable) );
lE.add( BaseAriAction.HttpResponse.build( 400, "Missing variable parameter.") );
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
String json = httpAction( url, "GET", lParamQuery, lParamForm, lE);
return (Variable) deserializeJson( json, Variable_impl_ari_0_0_1.class ); 
}

/**********************************************************
 * Variables on a channel
 * 
 * Set the value of a channel variable or function.
 *********************************************************/
public void setChannelVar(String channelId, String variable, String value) throws RestException {
String url = "/channels/" + channelId + "/variable";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lParamQuery.add( BaseAriAction.HttpParam.build( "variable", variable) );
lParamQuery.add( BaseAriAction.HttpParam.build( "value", value) );
lE.add( BaseAriAction.HttpResponse.build( 400, "Missing variable parameter.") );
lE.add( BaseAriAction.HttpResponse.build( 404, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Channel not in a Stasis application") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
}

};

