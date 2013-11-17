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

public interface ActionSounds {

// void getSounds String String AriCallback<List<? extends Sound>> callback
/**********************************************************
 * 
 *********************************************************/
public void getSounds(String lang, String format, AriCallback<List<? extends Sound>> callback);



// List<? extends Sound> getSounds String String
/**********************************************************
 * List all sounds.
 *********************************************************/
public List<? extends Sound> getSounds(String lang, String format) throws RestException;



// Sound getStoredSound String
/**********************************************************
 * Get a sound's details.
 *********************************************************/
public Sound getStoredSound(String soundId) throws RestException;



// void getStoredSound String AriCallback<Sound> callback
/**********************************************************
 * 
 *********************************************************/
public void getStoredSound(String soundId, AriCallback<Sound> callback);


}
;
