package ch.loway.oss.ari4java.generated.ari_1_9_0.models;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
//    Generated on: Wed Mar 30 16:50:37 COT 2016
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**********************************************************
 * A channel initiated a media unhold.
 * 
 * Defined in file: events.json
 * Generated by: Model
 *********************************************************/

public class ChannelUnhold_impl_ari_1_9_0 extends Event_impl_ari_1_9_0 implements ChannelUnhold, java.io.Serializable {
private static final long serialVersionUID = 1L;
  /**  The channel that initiated the unhold event.  */
  private Channel channel;
 public Channel getChannel() {
   return channel;
 }

 @JsonDeserialize( as=Channel_impl_ari_1_9_0.class )
 public void setChannel(Channel val ) {
   channel = val;
 }

/** No missing signatures from interface */
}

