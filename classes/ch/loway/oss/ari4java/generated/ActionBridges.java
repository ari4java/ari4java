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

public interface ActionBridges {

// void deleteBridge String
/**********************************************************
 * Shut down a bridge.
 * If any channels are in this bridge, they will be removed and resume whatever they were doing beforehand.
 *********************************************************/
public void deleteBridge(String bridgeId) throws RestException;



// void mohStopBridge String
/**********************************************************
 * Stop playing music on hold to a bridge.
 * This will only stop music on hold being played via bridges/{bridgeId}/mohStart.
 *********************************************************/
public void mohStopBridge(String bridgeId) throws RestException;



// void removeChannelFromBridge String String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void removeChannelFromBridge(String bridgeId, String channel, AriCallback<Void> callback);



// void getBridges AriCallback<List<? extends Bridge>> callback
/**********************************************************
 * 
 *********************************************************/
public void getBridges(AriCallback<List<? extends Bridge>> callback);



// LiveRecording recordBridge String String String int int String boolean String
/**********************************************************
 * Start a recording.
 * This records the mixed audio from all channels participating in this bridge.
 *********************************************************/
public LiveRecording recordBridge(String bridgeId, String name, String format, int maxDurationSeconds, int maxSilenceSeconds, String ifExists, boolean beep, String terminateOn) throws RestException;



// void recordBridge String String String int int String boolean String AriCallback<LiveRecording> callback
/**********************************************************
 * 
 *********************************************************/
public void recordBridge(String bridgeId, String name, String format, int maxDurationSeconds, int maxSilenceSeconds, String ifExists, boolean beep, String terminateOn, AriCallback<LiveRecording> callback);



// Playback playOnBridge String String String int int
/**********************************************************
 * Start playback of media on a bridge.
 * The media URI may be any of a number of URI's. You may use http: and https: URI's, as well as sound: and recording: URI's. This operation creates a playback resource that can be used to control the playback of media (pause, rewind, fast forward, etc.)
 *********************************************************/
public Playback playOnBridge(String bridgeId, String media, String lang, int offsetms, int skipms) throws RestException;



// List<? extends Bridge> getBridges
/**********************************************************
 * List active bridges.
 *********************************************************/
public List<? extends Bridge> getBridges() throws RestException;



// void deleteBridge String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void deleteBridge(String bridgeId, AriCallback<Void> callback);



// void getBridge String AriCallback<Bridge> callback
/**********************************************************
 * 
 *********************************************************/
public void getBridge(String bridgeId, AriCallback<Bridge> callback);



// void newBridge String AriCallback<Bridge> callback
/**********************************************************
 * 
 *********************************************************/
public void newBridge(String type, AriCallback<Bridge> callback);



// void addChannelToBridge String String String
/**********************************************************
 * Add a channel to a bridge.
 *********************************************************/
public void addChannelToBridge(String bridgeId, String channel, String role) throws RestException;



// void mohStartBridge String String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void mohStartBridge(String bridgeId, String mohClass, AriCallback<Void> callback);



// void addChannelToBridge String String String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void addChannelToBridge(String bridgeId, String channel, String role, AriCallback<Void> callback);



// void mohStartBridge String String
/**********************************************************
 * Play music on hold to a bridge or change the MOH class that is playing.
 *********************************************************/
public void mohStartBridge(String bridgeId, String mohClass) throws RestException;



// Bridge newBridge String
/**********************************************************
 * Create a new bridge.
 * This bridge persists until it has been shut down, or Asterisk has been shut down.
 *********************************************************/
public Bridge newBridge(String type) throws RestException;



// Bridge getBridge String
/**********************************************************
 * Get bridge details.
 *********************************************************/
public Bridge getBridge(String bridgeId) throws RestException;



// void removeChannelFromBridge String String
/**********************************************************
 * Remove a channel from a bridge.
 *********************************************************/
public void removeChannelFromBridge(String bridgeId, String channel) throws RestException;



// void playOnBridge String String String int int AriCallback<Playback> callback
/**********************************************************
 * 
 *********************************************************/
public void playOnBridge(String bridgeId, String media, String lang, int offsetms, int skipms, AriCallback<Playback> callback);



// void mohStopBridge String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void mohStopBridge(String bridgeId, AriCallback<Void> callback);


}
;
