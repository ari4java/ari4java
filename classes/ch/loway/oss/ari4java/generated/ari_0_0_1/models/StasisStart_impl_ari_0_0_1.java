package ch.loway.oss.ari4java.generated.ari_0_0_1.models;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import java.util.Date;
import java.util.List;

/** =====================================================
 * Notification that a channel has entered a Stasis appliction.
 * 
 * Defined in file :events.json
 * ====================================================== */
public class StasisStart_impl_ari_0_0_1 extends Event_impl_ari_0_0_1 implements StasisStart, java.io.Serializable {
  /**  Arguments to the application  */
  private List<String> args;
 public List<String> getArgs() {
   return args;
 }

 public void setArgs(List<String> val ) {
   args = val;
 }

  /**    */
  private Channel channel;
 public Channel getChannel() {
   return channel;
 }

 public void setChannel(Channel val ) {
   channel = val;
 }

}

