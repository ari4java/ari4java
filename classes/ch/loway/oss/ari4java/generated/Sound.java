package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;

public interface Sound {

// void setFormats List<? extends FormatLangPair>
/**********************************************************
 * The formats and languages in which this sound is available.
 *********************************************************/
 public void setFormats(List<? extends FormatLangPair> val );



// String getId
/**********************************************************
 * Sound's identifier.
 *********************************************************/
 public String getId();



// void setId String
/**********************************************************
 * Sound's identifier.
 *********************************************************/
 public void setId(String val );



// String getText
/**********************************************************
 * Text description of the sound, usually the words spoken.
 *********************************************************/
 public String getText();



// List<? extends FormatLangPair> getFormats
/**********************************************************
 * The formats and languages in which this sound is available.
 *********************************************************/
 public List<? extends FormatLangPair> getFormats();



// void setText String
/**********************************************************
 * Text description of the sound, usually the words spoken.
 *********************************************************/
 public void setText(String val );


}
;
