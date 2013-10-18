package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;

public interface ActionPlayback {

// Playback getPlayback String
/** =====================================================
 * Get a playback's details.
 * ====================================================== */
public Playback getPlayback(String playbackId) throws RestException;



// void controlPlayback String String
/** =====================================================
 * Get a playback's details.
 * ====================================================== */
public void controlPlayback(String playbackId, String operation) throws RestException;



// void stopPlayback String
/** =====================================================
 * Stop a playback.
 * ====================================================== */
public void stopPlayback(String playbackId) throws RestException;


}
;
