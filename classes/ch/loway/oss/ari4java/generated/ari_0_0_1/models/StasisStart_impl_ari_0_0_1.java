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
 * Notification that a channel has entered a Stasis application.
 * 
 * Defined in file: events.json
 *********************************************************/

public class StasisStart_impl_ari_0_0_1 extends Event_impl_ari_0_0_1 implements StasisStart, java.io.Serializable {
private static final long serialVersionUID = 1L;
  /**  Arguments to the application  */
  private List<? extends String> args;
 public List<? extends String> getArgs() {
   return args;
 }

 @JsonDeserialize( contentAs=String.class )
 public void setArgs(List<? extends String> val ) {
   args = val;
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

