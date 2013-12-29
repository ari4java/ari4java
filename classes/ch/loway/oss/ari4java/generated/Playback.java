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

public interface Playback {

// String getLanguage
/**********************************************************
 * For media types that support multiple languages, the language requested for playback.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public String getLanguage();



// void setState String
/**********************************************************
 * Current state of the playback operation.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public void setState(String val );



// void setLanguage String
/**********************************************************
 * For media types that support multiple languages, the language requested for playback.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public void setLanguage(String val );



// String getId
/**********************************************************
 * ID for this playback operation
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public String getId();



// void setTarget_uri String
/**********************************************************
 * URI for the channel or bridge to play the media on
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public void setTarget_uri(String val );



// void setMedia_uri String
/**********************************************************
 * URI for the media to play back.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public void setMedia_uri(String val );



// String getTarget_uri
/**********************************************************
 * URI for the channel or bridge to play the media on
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public String getTarget_uri();



// void setId String
/**********************************************************
 * ID for this playback operation
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public void setId(String val );



// String getMedia_uri
/**********************************************************
 * URI for the media to play back.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public String getMedia_uri();



// String getState
/**********************************************************
 * Current state of the playback operation.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public String getState();


}
;
