package ch.loway.oss.ari4java.generated.ari_0_0_1.models;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import java.util.List;

/** =====================================================
 * Notification that a channel has left a Stasis appliction.
 * 
 * Defined in file :events.json
 * ====================================================== */
public class StasisEnd_impl_ari_0_0_1 extends Event_impl_ari_0_0_1 implements StasisEnd, java.io.Serializable {
  /**    */
  private Channel channel;
 public Channel getChannel() {
   return channel;
 }

 public void setChannel(Channel val ) {
   channel = val;
 }

}

