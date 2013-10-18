package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;

public interface ActionRecordings {

// void muteRecording String
/** =====================================================
 * Mute a live recording.
 * Muting a recording suspends silence detection, which will be restarted when the recording is unmuted.
 * ====================================================== */
public void muteRecording(String recordingName) throws RestException;



// void deleteStoredRecording String
/** =====================================================
 * Delete a stored recording.
 * ====================================================== */
public void deleteStoredRecording(String recordingName) throws RestException;



// LiveRecording getLiveRecording String
/** =====================================================
 * List live recordings.
 * ====================================================== */
public LiveRecording getLiveRecording(String recordingName) throws RestException;



// List<StoredRecording> getStoredRecordings
/** =====================================================
 * List recordings that are complete.
 * ====================================================== */
public List<StoredRecording> getStoredRecordings() throws RestException;



// StoredRecording getStoredRecording String
/** =====================================================
 * Get a stored recording's details.
 * ====================================================== */
public StoredRecording getStoredRecording(String recordingName) throws RestException;



// void unmuteRecording String
/** =====================================================
 * Unmute a live recording.
 * ====================================================== */
public void unmuteRecording(String recordingName) throws RestException;



// void pauseRecording String
/** =====================================================
 * Pause a live recording.
 * Pausing a recording suspends silence detection, which will be restarted when the recording is unpaused. Paused time is not included in the accounting for maxDurationSeconds.
 * ====================================================== */
public void pauseRecording(String recordingName) throws RestException;



// void cancelRecording String
/** =====================================================
 * Stop a live recording and discard it.
 * ====================================================== */
public void cancelRecording(String recordingName) throws RestException;



// void stopRecording String
/** =====================================================
 * Stop a live recording and store it.
 * ====================================================== */
public void stopRecording(String recordingName) throws RestException;



// void unpauseRecording String
/** =====================================================
 * Unpause a live recording.
 * ====================================================== */
public void unpauseRecording(String recordingName) throws RestException;


}
;
