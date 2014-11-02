package ch.loway.oss.ari4java.generated.ari_1_5_0.models;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
//    Generated on: Sun Nov 02 19:48:30 CET 2014
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**********************************************************
 * Caller identification
 * 
 * Defined in file: channels.json
 *********************************************************/

public class CallerID_impl_ari_1_5_0 implements CallerID, java.io.Serializable {
private static final long serialVersionUID = 1L;
  /**    */
  private String name;
 public String getName() {
   return name;
 }

 @JsonDeserialize( as=String.class )
 public void setName(String val ) {
   name = val;
 }

  /**    */
  private String number;
 public String getNumber() {
   return number;
 }

 @JsonDeserialize( as=String.class )
 public void setNumber(String val ) {
   number = val;
 }

/** No missing signatures from interface */
}

