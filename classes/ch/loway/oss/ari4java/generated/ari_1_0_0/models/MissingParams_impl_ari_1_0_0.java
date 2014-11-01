package ch.loway.oss.ari4java.generated.ari_1_0_0.models;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
//    Generated on: Sat Nov 01 15:52:13 CET 2014
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**********************************************************
 * Error event sent when required params are missing.
 * 
 * Defined in file: events.json
 *********************************************************/

public class MissingParams_impl_ari_1_0_0 extends Message_impl_ari_1_0_0 implements MissingParams, java.io.Serializable {
private static final long serialVersionUID = 1L;
  /**  A list of the missing parameters  */
  private List<? extends String> params;
 public List<? extends String> getParams() {
   return params;
 }

 @JsonDeserialize( contentAs=String.class )
 public void setParams(List<? extends String> val ) {
   params = val;
 }

/** No missing signatures from interface */
}

