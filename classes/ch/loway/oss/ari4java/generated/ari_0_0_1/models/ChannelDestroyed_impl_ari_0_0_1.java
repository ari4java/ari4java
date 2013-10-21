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
 * Notification that a channel has been destroyed.
 * 
 * Defined in file: events.json
 *********************************************************/

public class ChannelDestroyed_impl_ari_0_0_1 extends Event_impl_ari_0_0_1 implements ChannelDestroyed, java.io.Serializable {
  /**  Integer representation of the cause of the hangup  */
  private int cause;
 public int getCause() {
   return cause;
 }

 @JsonDeserialize( as=int.class )
 public void setCause(int val ) {
   cause = val;
 }

  /**  Text representation of the cause of the hangup  */
  private String cause_txt;
 public String getCause_txt() {
   return cause_txt;
 }

 @JsonDeserialize( as=String.class )
 public void setCause_txt(String val ) {
   cause_txt = val;
 }

  /**    */
  private Channel channel;
 public Channel getChannel() {
   return channel;
 }

 @JsonDeserialize( as=Channel_impl_ari_0_0_1.class )
 public void setChannel(Channel val ) {
   channel = val;
 }

}

