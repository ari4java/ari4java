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

public interface ActionPlaybacks {

// void get String AriCallback<Playback> callback
/**********************************************************
 * 
 *********************************************************/
public void get(String playbackId, AriCallback<Playback> callback);



// void control String String
/**********************************************************
 * Control a playback.
 *********************************************************/
public void control(String playbackId, String operation) throws RestException;



// void stop String
/**********************************************************
 * Stop a playback.
 *********************************************************/
public void stop(String playbackId) throws RestException;



// Playback get String
/**********************************************************
 * Get a playback's details.
 *********************************************************/
public Playback get(String playbackId) throws RestException;



// void control String String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void control(String playbackId, String operation, AriCallback<Void> callback);



// void stop String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void stop(String playbackId, AriCallback<Void> callback);


}
;
