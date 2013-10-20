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

public class ActionBridges_impl_ari_0_0_1 extends BaseAriAction  implements ActionBridges {
/** =====================================================
 * Active bridges
 * 
 * List active bridges.
 * ====================================================== */
public List<Bridge> getBridges() throws RestException {
String url = "/bridges";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
String json = httpAction( url, "GET", lParamQuery, lParamForm, lE);
return (List<Bridge>) deserializeJson( json, new TypeReference<List<Bridge_impl_ari_0_0_1>>() {} ); 
}

/** =====================================================
 * Active bridges
 * 
 * Create a new bridge.
 * This bridge persists until it has been shut down, or Asterisk has been shut down.
 * ====================================================== */
public Bridge newBridge(String type) throws RestException {
String url = "/bridges";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lParamQuery.add( BaseAriAction.HttpParam.build( "type", type) );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
return (Bridge) deserializeJson( json, Bridge_impl_ari_0_0_1.class ); 
}

/** =====================================================
 * Individual bridge
 * 
 * Get bridge details.
 * ====================================================== */
public Bridge getBridge(String bridgeId) throws RestException {
String url = "/bridges/" + bridgeId + "";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lE.add( BaseAriAction.HttpResponse.build( 404, "Bridge not found") );
String json = httpAction( url, "GET", lParamQuery, lParamForm, lE);
return (Bridge) deserializeJson( json, Bridge_impl_ari_0_0_1.class ); 
}

/** =====================================================
 * Individual bridge
 * 
 * Shut down a bridge.
 * If any channels are in this bridge, they will be removed and resume whatever they were doing beforehand.
 * ====================================================== */
public void deleteBridge(String bridgeId) throws RestException {
String url = "/bridges/" + bridgeId + "";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lE.add( BaseAriAction.HttpResponse.build( 404, "Bridge not found") );
String json = httpAction( url, "DELETE", lParamQuery, lParamForm, lE);
}

/** =====================================================
 * Add a channel to a bridge
 * 
 * Add a channel to a bridge.
 * ====================================================== */
public void addChannelToBridge(String bridgeId, String channel, String role) throws RestException {
String url = "/bridges/" + bridgeId + "/addChannel";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lParamQuery.add( BaseAriAction.HttpParam.build( "channel", channel) );
lParamQuery.add( BaseAriAction.HttpParam.build( "role", role) );
lE.add( BaseAriAction.HttpResponse.build( 400, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 404, "Bridge not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Bridge not in Stasis application") );
lE.add( BaseAriAction.HttpResponse.build( 422, "Channel not in Stasis application") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
}

/** =====================================================
 * Remove a channel from a bridge
 * 
 * Remove a channel from a bridge.
 * ====================================================== */
public void removeChannelFromBridge(String bridgeId, String channel) throws RestException {
String url = "/bridges/" + bridgeId + "/removeChannel";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lParamQuery.add( BaseAriAction.HttpParam.build( "channel", channel) );
lE.add( BaseAriAction.HttpResponse.build( 400, "Channel not found") );
lE.add( BaseAriAction.HttpResponse.build( 404, "Bridge not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Bridge not in Stasis application") );
lE.add( BaseAriAction.HttpResponse.build( 422, "Channel not in this bridge") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
}

/** =====================================================
 * Play music on hold to a bridge
 * 
 * Play music on hold to a bridge or change the MOH class that is playing.
 * ====================================================== */
public void mohStartBridge(String bridgeId, String mohClass) throws RestException {
String url = "/bridges/" + bridgeId + "/mohStart";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lParamQuery.add( BaseAriAction.HttpParam.build( "mohClass", mohClass) );
lE.add( BaseAriAction.HttpResponse.build( 404, "Bridge not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Bridge not in Stasis application") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
}

/** =====================================================
 * Stop music on hold for a bridge
 * 
 * Stop playing music on hold to a bridge.
 * This will only stop music on hold being played via bridges/{bridgeId}/mohStart.
 * ====================================================== */
public void mohStopBridge(String bridgeId) throws RestException {
String url = "/bridges/" + bridgeId + "/mohStop";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lE.add( BaseAriAction.HttpResponse.build( 404, "Bridge not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Bridge not in Stasis application") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
}

/** =====================================================
 * Play media to the participants of a bridge
 * 
 * Start playback of media on a bridge.
 * The media URI may be any of a number of URI's. You may use http: and https: URI's, as well as sound: and recording: URI's. This operation creates a playback resource that can be used to control the playback of media (pause, rewind, fast forward, etc.)
 * ====================================================== */
public Playback playOnBridge(String bridgeId, String media, String lang, int offsetms, int skipms) throws RestException {
String url = "/bridges/" + bridgeId + "/play";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lParamQuery.add( BaseAriAction.HttpParam.build( "media", media) );
lParamQuery.add( BaseAriAction.HttpParam.build( "lang", lang) );
lParamQuery.add( BaseAriAction.HttpParam.build( "offsetms", offsetms) );
lParamQuery.add( BaseAriAction.HttpParam.build( "skipms", skipms) );
lE.add( BaseAriAction.HttpResponse.build( 404, "Bridge not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Bridge not in a Stasis application") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
return (Playback) deserializeJson( json, Playback_impl_ari_0_0_1.class ); 
}

/** =====================================================
 * Record audio on a bridge
 * 
 * Start a recording.
 * This records the mixed audio from all channels participating in this bridge.
 * ====================================================== */
public LiveRecording recordBridge(String bridgeId, String name, String format, int maxDurationSeconds, int maxSilenceSeconds, String ifExists, boolean beep, String terminateOn) throws RestException {
String url = "/bridges/" + bridgeId + "/record";
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
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
return (LiveRecording) deserializeJson( json, LiveRecording_impl_ari_0_0_1.class ); 
}

};

