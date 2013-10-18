package ch.loway.oss.ari4java.generated.ari_0_0_1.models;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import java.util.Date;
import java.util.List;

/** =====================================================
 * Event showing the completion of a media playback operation.
 * 
 * Defined in file :events.json
 * ====================================================== */
public class PlaybackFinished_impl_ari_0_0_1 extends Event_impl_ari_0_0_1 implements PlaybackFinished, java.io.Serializable {
  /**  Playback control object  */
  private Playback playback;
 public Playback getPlayback() {
   return playback;
 }

 public void setPlayback(Playback val ) {
   playback = val;
 }

}

