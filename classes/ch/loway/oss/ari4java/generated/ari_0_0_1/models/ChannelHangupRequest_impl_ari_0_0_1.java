package ch.loway.oss.ari4java.generated.ari_0_0_1.models;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import java.util.Date;
import java.util.List;

/** =====================================================
 * A hangup was requested on the channel.
 * 
 * Defined in file :events.json
 * ====================================================== */
public class ChannelHangupRequest_impl_ari_0_0_1 extends Event_impl_ari_0_0_1 implements ChannelHangupRequest, java.io.Serializable {
  /**  Integer representation of the cause of the hangup.  */
  private int cause;
 public int getCause() {
   return cause;
 }

 public void setCause(int val ) {
   cause = val;
 }

  /**  The channel on which the hangup was requested.  */
  private Channel channel;
 public Channel getChannel() {
   return channel;
 }

 public void setChannel(Channel val ) {
   channel = val;
 }

  /**  Whether the hangup request was a soft hangup request.  */
  private boolean soft;
 public boolean getSoft() {
   return soft;
 }

 public void setSoft(boolean val ) {
   soft = val;
 }

}

