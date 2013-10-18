package ch.loway.oss.ari4java.generated.ari_0_0_1.models;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import java.util.Date;
import java.util.List;

/** =====================================================
 * Notification that one bridge has merged into another.
 * 
 * Defined in file :events.json
 * ====================================================== */
public class BridgeMerged_impl_ari_0_0_1 extends Event_impl_ari_0_0_1 implements BridgeMerged, java.io.Serializable {
  /**    */
  private Bridge bridge;
 public Bridge getBridge() {
   return bridge;
 }

 public void setBridge(Bridge val ) {
   bridge = val;
 }

  /**    */
  private Bridge bridge_from;
 public Bridge getBridge_from() {
   return bridge_from;
 }

 public void setBridge_from(Bridge val ) {
   bridge_from = val;
 }

}

