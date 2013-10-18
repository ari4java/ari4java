package ch.loway.oss.ari4java.generated.ari_0_0_1.models;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import java.util.Date;
import java.util.List;

/** =====================================================
 * A media file that may be played back.
 * 
 * Defined in file :sounds.json
 * ====================================================== */
public class Sound_impl_ari_0_0_1 implements Sound, java.io.Serializable {
  /**  The formats and languages in which this sound is available.  */
  private List<FormatLangPair> formats;
 public List<FormatLangPair> getFormats() {
   return formats;
 }

 public void setFormats(List<FormatLangPair> val ) {
   formats = val;
 }

  /**  Sound's identifier.  */
  private String id;
 public String getId() {
   return id;
 }

 public void setId(String val ) {
   id = val;
 }

  /**  Text description of the sound, usually the words spoken.  */
  private String text;
 public String getText() {
   return text;
 }

 public void setText(String val ) {
   text = val;
 }

}

