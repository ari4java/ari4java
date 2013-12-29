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

public interface Sound {

// void setFormats List<? extends FormatLangPair>
/**********************************************************
 * The formats and languages in which this sound is available.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public void setFormats(List<? extends FormatLangPair> val );



// String getId
/**********************************************************
 * Sound's identifier.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public String getId();



// void setId String
/**********************************************************
 * Sound's identifier.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public void setId(String val );



// String getText
/**********************************************************
 * Text description of the sound, usually the words spoken.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public String getText();



// List<? extends FormatLangPair> getFormats
/**********************************************************
 * The formats and languages in which this sound is available.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public List<? extends FormatLangPair> getFormats();



// void setText String
/**********************************************************
 * Text description of the sound, usually the words spoken.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public void setText(String val );


}
;
