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

public interface ActionRecordings {

// StoredRecording getStoredRecording String
/**********************************************************
 * Get a stored recording's details.
 *********************************************************/
public StoredRecording getStoredRecording(String recordingName) throws RestException;



// void unmuteRecording String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void unmuteRecording(String recordingName, AriCallback<Void> callback);



// void unmuteRecording String
/**********************************************************
 * Unmute a live recording.
 *********************************************************/
public void unmuteRecording(String recordingName) throws RestException;



// void pauseRecording String
/**********************************************************
 * Pause a live recording.
 * Pausing a recording suspends silence detection, which will be restarted when the recording is unpaused. Paused time is not included in the accounting for maxDurationSeconds.
 *********************************************************/
public void pauseRecording(String recordingName) throws RestException;



// void getStoredRecordings AriCallback<List<? extends StoredRecording>> callback
/**********************************************************
 * 
 *********************************************************/
public void getStoredRecordings(AriCallback<List<? extends StoredRecording>> callback);



// void deleteStoredRecording String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void deleteStoredRecording(String recordingName, AriCallback<Void> callback);



// void stopRecording String
/**********************************************************
 * Stop a live recording and store it.
 *********************************************************/
public void stopRecording(String recordingName) throws RestException;



// void stopRecording String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void stopRecording(String recordingName, AriCallback<Void> callback);



// void unpauseRecording String
/**********************************************************
 * Unpause a live recording.
 *********************************************************/
public void unpauseRecording(String recordingName) throws RestException;



// List<? extends StoredRecording> getStoredRecordings
/**********************************************************
 * List recordings that are complete.
 *********************************************************/
public List<? extends StoredRecording> getStoredRecordings() throws RestException;



// void muteRecording String
/**********************************************************
 * Mute a live recording.
 * Muting a recording suspends silence detection, which will be restarted when the recording is unmuted.
 *********************************************************/
public void muteRecording(String recordingName) throws RestException;



// void deleteStoredRecording String
/**********************************************************
 * Delete a stored recording.
 *********************************************************/
public void deleteStoredRecording(String recordingName) throws RestException;



// void getLiveRecording String AriCallback<LiveRecording> callback
/**********************************************************
 * 
 *********************************************************/
public void getLiveRecording(String recordingName, AriCallback<LiveRecording> callback);



// LiveRecording getLiveRecording String
/**********************************************************
 * List live recordings.
 *********************************************************/
public LiveRecording getLiveRecording(String recordingName) throws RestException;



// void muteRecording String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void muteRecording(String recordingName, AriCallback<Void> callback);



// void cancelRecording String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void cancelRecording(String recordingName, AriCallback<Void> callback);



// void unpauseRecording String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void unpauseRecording(String recordingName, AriCallback<Void> callback);



// void cancelRecording String
/**********************************************************
 * Stop a live recording and discard it.
 *********************************************************/
public void cancelRecording(String recordingName) throws RestException;



// void getStoredRecording String AriCallback<StoredRecording> callback
/**********************************************************
 * 
 *********************************************************/
public void getStoredRecording(String recordingName, AriCallback<StoredRecording> callback);



// void pauseRecording String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void pauseRecording(String recordingName, AriCallback<Void> callback);


}
;
