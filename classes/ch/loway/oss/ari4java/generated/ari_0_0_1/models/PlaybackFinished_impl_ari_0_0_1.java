package ch.loway.oss.ari4java.generated.ari_0_0_1.models;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import java.util.List;

/**********************************************************
 * Event showing the completion of a media playback operation.
 * 
 * Defined in file: events.json
 *********************************************************/

public class PlaybackFinished_impl_ari_0_0_1 extends Event_impl_ari_0_0_1 implements PlaybackFinished, java.io.Serializable {
private static final long serialVersionUID = 1L;
  /**  Playback control object  */
  private Playback playback;
 public Playback getPlayback() {
   return playback;
 }

 @JsonDeserialize( as=Playback_impl_ari_0_0_1.class )
 public void setPlayback(Playback val ) {
   playback = val;
 }

}

