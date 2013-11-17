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

public class ActionBridges_impl_ari_0_0_1 extends BaseAriAction  implements ActionBridges {
/**********************************************************
 * Active bridges
 * 
 * List active bridges.
 *********************************************************/
private void buildGetBridges() {
reset();
url = "/bridges";
}

@Override
public List<? extends Bridge> getBridges() throws RestException {
buildGetBridges();
String json = httpActionSync();
return deserializeJson( json, new TypeReference<List<Bridge_impl_ari_0_0_1>>() {} ); 
}

@Override
public void getBridges(AriCallback<List<? extends Bridge>> callback) {
buildGetBridges();
httpActionAsync(callback, new TypeReference<List<Bridge_impl_ari_0_0_1>>() {});
}

/**********************************************************
 * Active bridges
 * 
 * Create a new bridge.
 * This bridge persists until it has been shut down, or Asterisk has been shut down.
 *********************************************************/
private void buildNewBridge(String type) {
reset();
url = "/bridges";
lParamQuery.add( BaseAriAction.HttpParam.build( "type", type) );
}

@Override
public Bridge newBridge(String type) throws RestException {
buildNewBridge(type);
String json = httpActionSync();
return deserializeJson( json, Bridge_impl_ari_0_0_1.class ); 
}

@Override
public void newBridge(String type, AriCallback<Bridge> callback) {
buildNewBridge(type);
httpActionAsync(callback, Bridge_impl_ari_0_0_1.class);
}

/**********************************************************
 * Individual bridge
 * 
 * Get bridge details.
 *********************************************************/
private void buildGetBridge(String bridgeId) {
reset();
url = "/bridges/" + bridgeId + "";
lE.add( BaseAriAction.HttpResponse.build( 404, "Bridge not found") );
}

@Override
public Bridge getBridge(String bridgeId) throws RestException {
buildGetBridge(bridgeId);
String json = httpActionSync();
return deserializeJson( json, Bridge_impl_ari_0_0_1.class ); 
}

@Override
public void getBridge(String bridgeId, AriCallback<Bridge> callback) {
buildGetBridge(bridgeId);
httpActionAsync(callback, Bridge_impl_ari_0_0_1.class);
}

/**********************************************************
 * Individual bridge
 * 
 * Shut down a bridge.
 * If any channels are in this bridge, they will be removed and resume whatever they were doing beforehand.
 *********************************************************/
private void buildDeleteBridge(String bridgeId) {
reset();
url = "/bridges/" + bridgeId + "";
lE.add( BaseAriAction.HttpResponse.build( 404, "Bridge not found") );
}

@Override
public void deleteBridge(String bridgeId) throws RestException {
buildDeleteBridge(bridgeId);
String json = httpActionSync();
}

@Override
public void deleteBridge(String bridgeId, AriCallback<Void> callback) {
buildDeleteBridge(bridgeId);
httpActionAsync(callback);
}

/**********************************************************
 * Add a channel to a bridge
 * 
 * Add a channel to a bridge.
 *********************************************************/
private void buildAddChannelToBridge(String bridgeId, String channel, String role) {
reset();
url = "/bridges/" + bridgeId + "/addChannel";
lParamQuery.add( BaseAriAction.HttpParam.build( "channel", channel) );
lParamQuery.add( BaseAriAction.HttpParam.build( "role", role) );
lE.add( BaseAriAction.HttpResponse.build( 400, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 404, "Bridge not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Bridge not in Stasis application") );
lE.add( BaseAriAction.HttpResponse.build( 422, "Channel not in Stasis application") );
}

@Override
public void addChannelToBridge(String bridgeId, String channel, String role) throws RestException {
buildAddChannelToBridge(bridgeId, channel, role);
String json = httpActionSync();
}

@Override
public void addChannelToBridge(String bridgeId, String channel, String role, AriCallback<Void> callback) {
buildAddChannelToBridge(bridgeId, channel, role);
httpActionAsync(callback);
}

/**********************************************************
 * Remove a channel from a bridge
 * 
 * Remove a channel from a bridge.
 *********************************************************/
private void buildRemoveChannelFromBridge(String bridgeId, String channel) {
reset();
url = "/bridges/" + bridgeId + "/removeChannel";
lParamQuery.add( BaseAriAction.HttpParam.build( "channel", channel) );
lE.add( BaseAriAction.HttpResponse.build( 400, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 404, "Bridge not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Bridge not in Stasis application") );
lE.add( BaseAriAction.HttpResponse.build( 422, "Channel not in this bridge") );
}

@Override
public void removeChannelFromBridge(String bridgeId, String channel) throws RestException {
buildRemoveChannelFromBridge(bridgeId, channel);
String json = httpActionSync();
}

@Override
public void removeChannelFromBridge(String bridgeId, String channel, AriCallback<Void> callback) {
buildRemoveChannelFromBridge(bridgeId, channel);
httpActionAsync(callback);
}

/**********************************************************
 * Play music on hold to a bridge
 * 
 * Play music on hold to a bridge or change the MOH class that is playing.
 *********************************************************/
private void buildMohStartBridge(String bridgeId, String mohClass) {
reset();
url = "/bridges/" + bridgeId + "/mohStart";
lParamQuery.add( BaseAriAction.HttpParam.build( "mohClass", mohClass) );
lE.add( BaseAriAction.HttpResponse.build( 404, "Bridge not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Bridge not in Stasis application") );
}

@Override
public void mohStartBridge(String bridgeId, String mohClass) throws RestException {
buildMohStartBridge(bridgeId, mohClass);
String json = httpActionSync();
}

@Override
public void mohStartBridge(String bridgeId, String mohClass, AriCallback<Void> callback) {
buildMohStartBridge(bridgeId, mohClass);
httpActionAsync(callback);
}

/**********************************************************
 * Stop music on hold for a bridge
 * 
 * Stop playing music on hold to a bridge.
 * This will only stop music on hold being played via bridges/{bridgeId}/mohStart.
 *********************************************************/
private void buildMohStopBridge(String bridgeId) {
reset();
url = "/bridges/" + bridgeId + "/mohStop";
lE.add( BaseAriAction.HttpResponse.build( 404, "Bridge not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Bridge not in Stasis application") );
}

@Override
public void mohStopBridge(String bridgeId) throws RestException {
buildMohStopBridge(bridgeId);
String json = httpActionSync();
}

@Override
public void mohStopBridge(String bridgeId, AriCallback<Void> callback) {
buildMohStopBridge(bridgeId);
httpActionAsync(callback);
}

/**********************************************************
 * Play media to the participants of a bridge
 * 
 * Start playback of media on a bridge.
 * The media URI may be any of a number of URI's. You may use http: and https: URI's, as well as sound: and recording: URI's. This operation creates a playback resource that can be used to control the playback of media (pause, rewind, fast forward, etc.)
 *********************************************************/
private void buildPlayOnBridge(String bridgeId, String media, String lang, int offsetms, int skipms) {
reset();
url = "/bridges/" + bridgeId + "/play";
lParamQuery.add( BaseAriAction.HttpParam.build( "media", media) );
lParamQuery.add( BaseAriAction.HttpParam.build( "lang", lang) );
lParamQuery.add( BaseAriAction.HttpParam.build( "offsetms", offsetms) );
lParamQuery.add( BaseAriAction.HttpParam.build( "skipms", skipms) );
lE.add( BaseAriAction.HttpResponse.build( 404, "Bridge not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Bridge not in a Stasis application") );
}

@Override
public Playback playOnBridge(String bridgeId, String media, String lang, int offsetms, int skipms) throws RestException {
buildPlayOnBridge(bridgeId, media, lang, offsetms, skipms);
String json = httpActionSync();
return deserializeJson( json, Playback_impl_ari_0_0_1.class ); 
}

@Override
public void playOnBridge(String bridgeId, String media, String lang, int offsetms, int skipms, AriCallback<Playback> callback) {
buildPlayOnBridge(bridgeId, media, lang, offsetms, skipms);
httpActionAsync(callback, Playback_impl_ari_0_0_1.class);
}

/**********************************************************
 * Record audio on a bridge
 * 
 * Start a recording.
 * This records the mixed audio from all channels participating in this bridge.
 *********************************************************/
private void buildRecordBridge(String bridgeId, String name, String format, int maxDurationSeconds, int maxSilenceSeconds, String ifExists, boolean beep, String terminateOn) {
reset();
url = "/bridges/" + bridgeId + "/record";
lParamQuery.add( BaseAriAction.HttpParam.build( "name", name) );
lParamQuery.add( BaseAriAction.HttpParam.build( "format", format) );
lParamQuery.add( BaseAriAction.HttpParam.build( "maxDurationSeconds", maxDurationSeconds) );
lParamQuery.add( BaseAriAction.HttpParam.build( "maxSilenceSeconds", maxSilenceSeconds) );
lParamQuery.add( BaseAriAction.HttpParam.build( "ifExists", ifExists) );
lParamQuery.add( BaseAriAction.HttpParam.build( "beep", beep) );
lParamQuery.add( BaseAriAction.HttpParam.build( "terminateOn", terminateOn) );
}

@Override
public LiveRecording recordBridge(String bridgeId, String name, String format, int maxDurationSeconds, int maxSilenceSeconds, String ifExists, boolean beep, String terminateOn) throws RestException {
buildRecordBridge(bridgeId, name, format, maxDurationSeconds, maxSilenceSeconds, ifExists, beep, terminateOn);
String json = httpActionSync();
return deserializeJson( json, LiveRecording_impl_ari_0_0_1.class ); 
}

@Override
public void recordBridge(String bridgeId, String name, String format, int maxDurationSeconds, int maxSilenceSeconds, String ifExists, boolean beep, String terminateOn, AriCallback<LiveRecording> callback) {
buildRecordBridge(bridgeId, name, format, maxDurationSeconds, maxSilenceSeconds, ifExists, beep, terminateOn);
httpActionAsync(callback, LiveRecording_impl_ari_0_0_1.class);
}

};

