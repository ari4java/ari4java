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

public interface ActionPlayback {

// void stopPlayback String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void stopPlayback(String playbackId, AriCallback<Void> callback);



// Playback getPlayback String
/**********************************************************
 * Get a playback's details.
 *********************************************************/
public Playback getPlayback(String playbackId) throws RestException;



// void controlPlayback String String
/**********************************************************
 * Get a playback's details.
 *********************************************************/
public void controlPlayback(String playbackId, String operation) throws RestException;



// void controlPlayback String String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void controlPlayback(String playbackId, String operation, AriCallback<Void> callback);



// void getPlayback String AriCallback<Playback> callback
/**********************************************************
 * 
 *********************************************************/
public void getPlayback(String playbackId, AriCallback<Playback> callback);



// void stopPlayback String
/**********************************************************
 * Stop a playback.
 *********************************************************/
public void stopPlayback(String playbackId) throws RestException;


}
;
