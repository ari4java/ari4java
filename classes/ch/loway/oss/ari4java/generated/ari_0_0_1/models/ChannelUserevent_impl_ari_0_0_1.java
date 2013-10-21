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
 * User-generated event with additional user-defined fields in the object.
 * 
 * Defined in file: events.json
 *********************************************************/

public class ChannelUserevent_impl_ari_0_0_1 extends Event_impl_ari_0_0_1 implements ChannelUserevent, java.io.Serializable {
  /**  The channel that signaled the user event.  */
  private Channel channel;
 public Channel getChannel() {
   return channel;
 }

 @JsonDeserialize( as=Channel_impl_ari_0_0_1.class )
 public void setChannel(Channel val ) {
   channel = val;
 }

  /**  The name of the user event.  */
  private String eventname;
 public String getEventname() {
   return eventname;
 }

 @JsonDeserialize( as=String.class )
 public void setEventname(String val ) {
   eventname = val;
 }

  /**  Custom Userevent data  */
  private String userevent;
 public String getUserevent() {
   return userevent;
 }

 @JsonDeserialize( as=String.class )
 public void setUserevent(String val ) {
   userevent = val;
 }

}

