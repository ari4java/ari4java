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

public class ActionRecordings_impl_ari_0_0_1 extends BaseAriAction  implements ActionRecordings {
/**********************************************************
 * Recordings
 * 
 * List recordings that are complete.
 *********************************************************/
private void buildGetStoredRecordings() {
reset();
url = "/recordings/stored";
}

@Override
public List<? extends StoredRecording> getStoredRecordings() throws RestException {
buildGetStoredRecordings();
String json = httpActionSync();
return deserializeJson( json, new TypeReference<List<StoredRecording_impl_ari_0_0_1>>() {} ); 
}

@Override
public void getStoredRecordings(AriCallback<List<? extends StoredRecording>> callback) {
buildGetStoredRecordings();
httpActionAsync(callback, new TypeReference<List<StoredRecording_impl_ari_0_0_1>>() {});
}

/**********************************************************
 * Individual recording
 * 
 * Get a stored recording's details.
 *********************************************************/
private void buildGetStoredRecording(String recordingName) {
reset();
url = "/recordings/stored/" + recordingName + "";
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
}

@Override
public StoredRecording getStoredRecording(String recordingName) throws RestException {
buildGetStoredRecording(recordingName);
String json = httpActionSync();
return deserializeJson( json, StoredRecording_impl_ari_0_0_1.class ); 
}

@Override
public void getStoredRecording(String recordingName, AriCallback<StoredRecording> callback) {
buildGetStoredRecording(recordingName);
httpActionAsync(callback, StoredRecording_impl_ari_0_0_1.class);
}

/**********************************************************
 * Individual recording
 * 
 * Delete a stored recording.
 *********************************************************/
private void buildDeleteStoredRecording(String recordingName) {
reset();
url = "/recordings/stored/" + recordingName + "";
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
}

@Override
public void deleteStoredRecording(String recordingName) throws RestException {
buildDeleteStoredRecording(recordingName);
String json = httpActionSync();
}

@Override
public void deleteStoredRecording(String recordingName, AriCallback<Void> callback) {
buildDeleteStoredRecording(recordingName);
httpActionAsync(callback);
}

/**********************************************************
 * A recording that is in progress
 * 
 * List live recordings.
 *********************************************************/
private void buildGetLiveRecording(String recordingName) {
reset();
url = "/recordings/live/" + recordingName + "";
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
}

@Override
public LiveRecording getLiveRecording(String recordingName) throws RestException {
buildGetLiveRecording(recordingName);
String json = httpActionSync();
return deserializeJson( json, LiveRecording_impl_ari_0_0_1.class ); 
}

@Override
public void getLiveRecording(String recordingName, AriCallback<LiveRecording> callback) {
buildGetLiveRecording(recordingName);
httpActionAsync(callback, LiveRecording_impl_ari_0_0_1.class);
}

/**********************************************************
 * A recording that is in progress
 * 
 * Stop a live recording and discard it.
 *********************************************************/
private void buildCancelRecording(String recordingName) {
reset();
url = "/recordings/live/" + recordingName + "";
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
}

@Override
public void cancelRecording(String recordingName) throws RestException {
buildCancelRecording(recordingName);
String json = httpActionSync();
}

@Override
public void cancelRecording(String recordingName, AriCallback<Void> callback) {
buildCancelRecording(recordingName);
httpActionAsync(callback);
}

/**********************************************************
 * 
 * 
 * Stop a live recording and store it.
 *********************************************************/
private void buildStopRecording(String recordingName) {
reset();
url = "/recordings/live/" + recordingName + "/stop";
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
}

@Override
public void stopRecording(String recordingName) throws RestException {
buildStopRecording(recordingName);
String json = httpActionSync();
}

@Override
public void stopRecording(String recordingName, AriCallback<Void> callback) {
buildStopRecording(recordingName);
httpActionAsync(callback);
}

/**********************************************************
 * 
 * 
 * Pause a live recording.
 * Pausing a recording suspends silence detection, which will be restarted when the recording is unpaused. Paused time is not included in the accounting for maxDurationSeconds.
 *********************************************************/
private void buildPauseRecording(String recordingName) {
reset();
url = "/recordings/live/" + recordingName + "/pause";
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Recording not in session") );
}

@Override
public void pauseRecording(String recordingName) throws RestException {
buildPauseRecording(recordingName);
String json = httpActionSync();
}

@Override
public void pauseRecording(String recordingName, AriCallback<Void> callback) {
buildPauseRecording(recordingName);
httpActionAsync(callback);
}

/**********************************************************
 * 
 * 
 * Unpause a live recording.
 *********************************************************/
private void buildUnpauseRecording(String recordingName) {
reset();
url = "/recordings/live/" + recordingName + "/unpause";
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Recording not in session") );
}

@Override
public void unpauseRecording(String recordingName) throws RestException {
buildUnpauseRecording(recordingName);
String json = httpActionSync();
}

@Override
public void unpauseRecording(String recordingName, AriCallback<Void> callback) {
buildUnpauseRecording(recordingName);
httpActionAsync(callback);
}

/**********************************************************
 * 
 * 
 * Mute a live recording.
 * Muting a recording suspends silence detection, which will be restarted when the recording is unmuted.
 *********************************************************/
private void buildMuteRecording(String recordingName) {
reset();
url = "/recordings/live/" + recordingName + "/mute";
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Recording not in session") );
}

@Override
public void muteRecording(String recordingName) throws RestException {
buildMuteRecording(recordingName);
String json = httpActionSync();
}

@Override
public void muteRecording(String recordingName, AriCallback<Void> callback) {
buildMuteRecording(recordingName);
httpActionAsync(callback);
}

/**********************************************************
 * 
 * 
 * Unmute a live recording.
 *********************************************************/
private void buildUnmuteRecording(String recordingName) {
reset();
url = "/recordings/live/" + recordingName + "/unmute";
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Recording not in session") );
}

@Override
public void unmuteRecording(String recordingName) throws RestException {
buildUnmuteRecording(recordingName);
String json = httpActionSync();
}

@Override
public void unmuteRecording(String recordingName, AriCallback<Void> callback) {
buildUnmuteRecording(recordingName);
httpActionAsync(callback);
}

};

