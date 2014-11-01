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
 * Notification that a channel has entered a Stasis application.
 * 
 * Defined in file: events.json
 *********************************************************/

public class StasisStart_impl_ari_1_0_0 extends Event_impl_ari_1_0_0 implements StasisStart, java.io.Serializable {
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

 @JsonDeserialize( as=Channel_impl_ari_1_0_0.class )
 public void setChannel(Channel val ) {
   channel = val;
 }

/**********************************************************
 * 
 * 
 * @since ari_1_5_0
 *********************************************************/
 public void setReplace_channel(Channel val ){
  throw new UnsupportedOperationException("Method availble from ...");
};

/**********************************************************
 * 
 * 
 * @since ari_1_5_0
 *********************************************************/
 public Channel getReplace_channel(){
  throw new UnsupportedOperationException("Method availble from ...");
};

}

