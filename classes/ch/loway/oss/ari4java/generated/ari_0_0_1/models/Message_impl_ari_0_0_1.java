package ch.loway.oss.ari4java.generated.ari_0_0_1.models;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import java.util.List;

/** =====================================================
 * Base type for errors and events
 * 
 * Defined in file :events.json
 * ====================================================== */
public class Message_impl_ari_0_0_1 implements Message, java.io.Serializable {
  /**  Indicates the type of this message.  */
  private String type;
 public String getType() {
   return type;
 }

 public void setType(String val ) {
   type = val;
 }

}

