package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;

public interface ActionSounds {

// List<Sound> getSounds String String
/** =====================================================
 * List all sounds.
 * ====================================================== */
public List<Sound> getSounds(String lang, String format) throws RestException;



// Sound getStoredSound String
/** =====================================================
 * Get a sound's details.
 * ====================================================== */
public Sound getStoredSound(String soundId) throws RestException;


}
;
