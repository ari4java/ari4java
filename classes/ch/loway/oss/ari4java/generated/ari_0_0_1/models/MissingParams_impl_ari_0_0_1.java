package ch.loway.oss.ari4java.generated.ari_0_0_1.models;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import java.util.Date;
import java.util.List;

/** =====================================================
 * Error event sent when required params are missing.
 * 
 * Defined in file :events.json
 * ====================================================== */
public class MissingParams_impl_ari_0_0_1 extends Message_impl_ari_0_0_1 implements MissingParams, java.io.Serializable {
  /**  A list of the missing parameters  */
  private List<String> params;
 public List<String> getParams() {
   return params;
 }

 public void setParams(List<String> val ) {
   params = val;
 }

}

