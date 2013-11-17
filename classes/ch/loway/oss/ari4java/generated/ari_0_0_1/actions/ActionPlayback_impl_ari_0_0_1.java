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

public class ActionPlayback_impl_ari_0_0_1 extends BaseAriAction  implements ActionPlayback {
/**********************************************************
 * Control object for a playback operation.
 * 
 * Get a playback's details.
 *********************************************************/
private void buildGetPlayback(String playbackId) {
reset();
url = "/playback/" + playbackId + "";
}

@Override
public Playback getPlayback(String playbackId) throws RestException {
buildGetPlayback(playbackId);
String json = httpActionSync();
return deserializeJson( json, Playback_impl_ari_0_0_1.class ); 
}

@Override
public void getPlayback(String playbackId, AriCallback<Playback> callback) {
buildGetPlayback(playbackId);
httpActionAsync(callback, Playback_impl_ari_0_0_1.class);
}

/**********************************************************
 * Control object for a playback operation.
 * 
 * Stop a playback.
 *********************************************************/
private void buildStopPlayback(String playbackId) {
reset();
url = "/playback/" + playbackId + "";
}

@Override
public void stopPlayback(String playbackId) throws RestException {
buildStopPlayback(playbackId);
String json = httpActionSync();
}

@Override
public void stopPlayback(String playbackId, AriCallback<Void> callback) {
buildStopPlayback(playbackId);
httpActionAsync(callback);
}

/**********************************************************
 * Control object for a playback operation.
 * 
 * Get a playback's details.
 *********************************************************/
private void buildControlPlayback(String playbackId, String operation) {
reset();
url = "/playback/" + playbackId + "/control";
lParamQuery.add( BaseAriAction.HttpParam.build( "operation", operation) );
lE.add( BaseAriAction.HttpResponse.build( 400, "The provided operation parameter was invalid") );
lE.add( BaseAriAction.HttpResponse.build( 404, "The playback cannot be found") );
lE.add( BaseAriAction.HttpResponse.build( 409, "The operation cannot be performed in the playback's current state") );
}

@Override
public void controlPlayback(String playbackId, String operation) throws RestException {
buildControlPlayback(playbackId, operation);
String json = httpActionSync();
}

@Override
public void controlPlayback(String playbackId, String operation, AriCallback<Void> callback) {
buildControlPlayback(playbackId, operation);
httpActionAsync(callback);
}

};

