package ch.loway.oss.ari4java.generated.ari_1_0_0.models;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
//    Generated on: Sat Nov 01 19:27:12 CET 2014
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**********************************************************
 * A hangup was requested on the channel.
 * 
 * Defined in file: events.json
 *********************************************************/

public class ChannelHangupRequest_impl_ari_1_0_0 extends Event_impl_ari_1_0_0 implements ChannelHangupRequest, java.io.Serializable {
private static final long serialVersionUID = 1L;
  /**  Integer representation of the cause of the hangup.  */
  private int cause;
 public int getCause() {
   return cause;
 }

 @JsonDeserialize( as=int.class )
 public void setCause(int val ) {
   cause = val;
 }

  /**  The channel on which the hangup was requested.  */
  private Channel channel;
 public Channel getChannel() {
   return channel;
 }

 @JsonDeserialize( as=Channel_impl_ari_1_0_0.class )
 public void setChannel(Channel val ) {
   channel = val;
 }

  /**  Whether the hangup request was a soft hangup request.  */
  private boolean soft;
 public boolean getSoft() {
   return soft;
 }

 @JsonDeserialize( as=boolean.class )
 public void setSoft(boolean val ) {
   soft = val;
 }

/** No missing signatures from interface */
}

