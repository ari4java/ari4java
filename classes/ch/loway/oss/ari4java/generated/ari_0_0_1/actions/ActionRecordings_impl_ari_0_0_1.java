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
private void buildListStored() {
reset();
url = "/recordings/stored";
method = "GET";
}

@Override
public List<? extends StoredRecording> listStored() throws RestException {
buildListStored();
String json = httpActionSync();
return deserializeJson( json, new TypeReference<List<StoredRecording_impl_ari_0_0_1>>() {} ); 
}

@Override
public void listStored(AriCallback<List<? extends StoredRecording>> callback) {
buildListStored();
httpActionAsync(callback, new TypeReference<List<StoredRecording_impl_ari_0_0_1>>() {});
}

/**********************************************************
 * Individual recording
 * 
 * Get a stored recording's details.
 *********************************************************/
private void buildGetStored(String recordingName) {
reset();
url = "/recordings/stored/" + recordingName + "";
method = "GET";
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
}

@Override
public StoredRecording getStored(String recordingName) throws RestException {
buildGetStored(recordingName);
String json = httpActionSync();
return deserializeJson( json, StoredRecording_impl_ari_0_0_1.class ); 
}

@Override
public void getStored(String recordingName, AriCallback<StoredRecording> callback) {
buildGetStored(recordingName);
httpActionAsync(callback, StoredRecording_impl_ari_0_0_1.class);
}

/**********************************************************
 * Individual recording
 * 
 * Delete a stored recording.
 *********************************************************/
private void buildDeleteStored(String recordingName) {
reset();
url = "/recordings/stored/" + recordingName + "";
method = "DELETE";
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
}

@Override
public void deleteStored(String recordingName) throws RestException {
buildDeleteStored(recordingName);
String json = httpActionSync();
}

@Override
public void deleteStored(String recordingName, AriCallback<Void> callback) {
buildDeleteStored(recordingName);
httpActionAsync(callback);
}

/**********************************************************
 * A recording that is in progress
 * 
 * List live recordings.
 *********************************************************/
private void buildGetLive(String recordingName) {
reset();
url = "/recordings/live/" + recordingName + "";
method = "GET";
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
}

@Override
public LiveRecording getLive(String recordingName) throws RestException {
buildGetLive(recordingName);
String json = httpActionSync();
return deserializeJson( json, LiveRecording_impl_ari_0_0_1.class ); 
}

@Override
public void getLive(String recordingName, AriCallback<LiveRecording> callback) {
buildGetLive(recordingName);
httpActionAsync(callback, LiveRecording_impl_ari_0_0_1.class);
}

/**********************************************************
 * A recording that is in progress
 * 
 * Stop a live recording and discard it.
 *********************************************************/
private void buildCancel(String recordingName) {
reset();
url = "/recordings/live/" + recordingName + "";
method = "DELETE";
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
}

@Override
public void cancel(String recordingName) throws RestException {
buildCancel(recordingName);
String json = httpActionSync();
}

@Override
public void cancel(String recordingName, AriCallback<Void> callback) {
buildCancel(recordingName);
httpActionAsync(callback);
}

/**********************************************************
 * 
 * 
 * Stop a live recording and store it.
 *********************************************************/
private void buildStop(String recordingName) {
reset();
url = "/recordings/live/" + recordingName + "/stop";
method = "POST";
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
}

@Override
public void stop(String recordingName) throws RestException {
buildStop(recordingName);
String json = httpActionSync();
}

@Override
public void stop(String recordingName, AriCallback<Void> callback) {
buildStop(recordingName);
httpActionAsync(callback);
}

/**********************************************************
 * 
 * 
 * Pause a live recording.
 * Pausing a recording suspends silence detection, which will be restarted when the recording is unpaused. Paused time is not included in the accounting for maxDurationSeconds.
 *********************************************************/
private void buildPause(String recordingName) {
reset();
url = "/recordings/live/" + recordingName + "/pause";
method = "POST";
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Recording not in session") );
}

@Override
public void pause(String recordingName) throws RestException {
buildPause(recordingName);
String json = httpActionSync();
}

@Override
public void pause(String recordingName, AriCallback<Void> callback) {
buildPause(recordingName);
httpActionAsync(callback);
}

/**********************************************************
 * 
 * 
 * Unpause a live recording.
 *********************************************************/
private void buildUnpause(String recordingName) {
reset();
url = "/recordings/live/" + recordingName + "/pause";
method = "DELETE";
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Recording not in session") );
}

@Override
public void unpause(String recordingName) throws RestException {
buildUnpause(recordingName);
String json = httpActionSync();
}

@Override
public void unpause(String recordingName, AriCallback<Void> callback) {
buildUnpause(recordingName);
httpActionAsync(callback);
}

/**********************************************************
 * 
 * 
 * Mute a live recording.
 * Muting a recording suspends silence detection, which will be restarted when the recording is unmuted.
 *********************************************************/
private void buildMute(String recordingName) {
reset();
url = "/recordings/live/" + recordingName + "/mute";
method = "POST";
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Recording not in session") );
}

@Override
public void mute(String recordingName) throws RestException {
buildMute(recordingName);
String json = httpActionSync();
}

@Override
public void mute(String recordingName, AriCallback<Void> callback) {
buildMute(recordingName);
httpActionAsync(callback);
}

/**********************************************************
 * 
 * 
 * Unmute a live recording.
 *********************************************************/
private void buildUnmute(String recordingName) {
reset();
url = "/recordings/live/" + recordingName + "/mute";
method = "DELETE";
lE.add( BaseAriAction.HttpResponse.build( 404, "Recording not found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "Recording not in session") );
}

@Override
public void unmute(String recordingName) throws RestException {
buildUnmute(recordingName);
String json = httpActionSync();
}

@Override
public void unmute(String recordingName, AriCallback<Void> callback) {
buildUnmute(recordingName);
httpActionAsync(callback);
}

/** No missing signatures from interface */
};

