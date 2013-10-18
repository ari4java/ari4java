package ch.loway.oss.ari4java.generated.ari_0_0_1.models;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import java.util.Date;
import java.util.List;

/** =====================================================
 * Notification that a channel has entered a bridge.
 * 
 * Defined in file :events.json
 * ====================================================== */
public class ChannelEnteredBridge_impl_ari_0_0_1 extends Event_impl_ari_0_0_1 implements ChannelEnteredBridge, java.io.Serializable {
  /**    */
  private Bridge bridge;
 public Bridge getBridge() {
   return bridge;
 }

 public void setBridge(Bridge val ) {
   bridge = val;
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

