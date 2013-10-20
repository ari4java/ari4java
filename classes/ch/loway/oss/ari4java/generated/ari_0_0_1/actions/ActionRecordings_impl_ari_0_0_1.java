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

public class ActionRecordings_impl_ari_0_0_1 extends BaseAriAction  implements ActionRecordings {
/** =====================================================
 * Recordings
 * 
 * List recordings that are complete.
 * ====================================================== */
public List<StoredRecording> getStoredRecordings() throws RestException {
String url = "/recordings/stored";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
String json = httpAction( url, "GET", lParamQuery, lParamForm, lE);
return (List<StoredRecording>) deserializeJson( json, new TypeReference<List<StoredRecording_impl_ari_0_0_1>>() {} ); 
}

/** =====================================================
 * Individual recording
 * 
 * Get a stored recording's details.
 * ====================================================== */
public StoredRecording getStoredRecording(String recordingName) throws RestException {
String url = "/recordings/stored/" + recordingName + "";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
String json = httpAction( url, "GET", lParamQuery, lParamForm, lE);
return (StoredRecording) deserializeJson( json, StoredRecording_impl_ari_0_0_1.class ); 
}

/** =====================================================
 * Individual recording
 * 
 * Delete a stored recording.
 * ====================================================== */
public void deleteStoredRecording(String recordingName) throws RestException {
String url = "/recordings/stored/" + recordingName + "";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
String json = httpAction( url, "DELETE", lParamQuery, lParamForm, lE);
}

/** =====================================================
 * A recording that is in progress
 * 
 * List live recordings.
 * ====================================================== */
public LiveRecording getLiveRecording(String recordingName) throws RestException {
String url = "/recordings/live/" + recordingName + "";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
String json = httpAction( url, "GET", lParamQuery, lParamForm, lE);
return (LiveRecording) deserializeJson( json, LiveRecording_impl_ari_0_0_1.class ); 
}

/** =====================================================
 * A recording that is in progress
 * 
 * Stop a live recording and discard it.
 * ====================================================== */
public void cancelRecording(String recordingName) throws RestException {
String url = "/recordings/live/" + recordingName + "";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
String json = httpAction( url, "DELETE", lParamQuery, lParamForm, lE);
}

/** =====================================================
 * 
 * 
 * Stop a live recording and store it.
 * ====================================================== */
public void stopRecording(String recordingName) throws RestException {
String url = "/recordings/live/" + recordingName + "/stop";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
}

/** =====================================================
 * 
 * 
 * Pause a live recording.
 * Pausing a recording suspends silence detection, which will be restarted when the recording is unpaused. Paused time is not included in the accounting for maxDurationSeconds.
 * ====================================================== */
public void pauseRecording(String recordingName) throws RestException {
String url = "/recordings/live/" + recordingName + "/pause";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Recording not in session") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
}

/** =====================================================
 * 
 * 
 * Unpause a live recording.
 * ====================================================== */
public void unpauseRecording(String recordingName) throws RestException {
String url = "/recordings/live/" + recordingName + "/unpause";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Recording not in session") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
}

/** =====================================================
 * 
 * 
 * Mute a live recording.
 * Muting a recording suspends silence detection, which will be restarted when the recording is unmuted.
 * ====================================================== */
public void muteRecording(String recordingName) throws RestException {
String url = "/recordings/live/" + recordingName + "/mute";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Recording not in session") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
}

/** =====================================================
 * 
 * 
 * Unmute a live recording.
 * ====================================================== */
public void unmuteRecording(String recordingName) throws RestException {
String url = "/recordings/live/" + recordingName + "/unmute";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Recording not in session") );
String json = httpAction( url, "POST", lParamQuery, lParamForm, lE);
}

};

