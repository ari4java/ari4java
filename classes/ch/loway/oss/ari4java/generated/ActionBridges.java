package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;

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



// LiveRecording recordBridge String String String int int String boolean String
/**********************************************************
 * Start a recording.
 * This records the mixed audio from all channels participating in this bridge.
 *********************************************************/
public LiveRecording recordBridge(String bridgeId, String name, String format, int maxDurationSeconds, int maxSilenceSeconds, String ifExists, boolean beep, String terminateOn) throws RestException;



// Playback playOnBridge String String String int int
/**********************************************************
 * Start playback of media on a bridge.
 * The media URI may be any of a number of URI's. You may use http: and https: URI's, as well as sound: and recording: URI's. This operation creates a playback resource that can be used to control the playback of media (pause, rewind, fast forward, etc.)
 *********************************************************/
public Playback playOnBridge(String bridgeId, String media, String lang, int offsetms, int skipms) throws RestException;



// void mohStartBridge String String
/**********************************************************
 * Play music on hold to a bridge or change the MOH class that is playing.
 *********************************************************/
public void mohStartBridge(String bridgeId, String mohClass) throws RestException;



// Bridge getBridge String
/**********************************************************
 * Get bridge details.
 *********************************************************/
public Bridge getBridge(String bridgeId) throws RestException;



// Bridge newBridge String
/**********************************************************
 * Create a new bridge.
 * This bridge persists until it has been shut down, or Asterisk has been shut down.
 *********************************************************/
public Bridge newBridge(String type) throws RestException;



// List<? extends Bridge> getBridges
/**********************************************************
 * List active bridges.
 *********************************************************/
public List<? extends Bridge> getBridges() throws RestException;



// void removeChannelFromBridge String String
/**********************************************************
 * Remove a channel from a bridge.
 *********************************************************/
public void removeChannelFromBridge(String bridgeId, String channel) throws RestException;



// void addChannelToBridge String String String
/**********************************************************
 * Add a channel to a bridge.
 *********************************************************/
public void addChannelToBridge(String bridgeId, String channel, String role) throws RestException;


}
;
