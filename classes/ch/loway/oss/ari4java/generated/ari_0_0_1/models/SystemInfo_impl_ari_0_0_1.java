package ch.loway.oss.ari4java.generated.ari_0_0_1.models;

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
 * Info about Asterisk
 * 
 * Defined in file: asterisk.json
 *********************************************************/

public class SystemInfo_impl_ari_0_0_1 implements SystemInfo, java.io.Serializable {
private static final long serialVersionUID = 1L;
  /**    */
  private String entity_id;
 public String getEntity_id() {
   return entity_id;
 }

 @JsonDeserialize( as=String.class )
 public void setEntity_id(String val ) {
   entity_id = val;
 }

  /**  Asterisk version.  */
  private String version;
 public String getVersion() {
   return version;
 }

 @JsonDeserialize( as=String.class )
 public void setVersion(String val ) {
   version = val;
 }

/** No missing signatures from interface */
}

