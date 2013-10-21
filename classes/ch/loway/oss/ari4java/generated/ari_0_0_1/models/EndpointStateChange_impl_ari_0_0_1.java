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
 * Endpoint state changed.
 * 
 * Defined in file: events.json
 *********************************************************/

public class EndpointStateChange_impl_ari_0_0_1 extends Event_impl_ari_0_0_1 implements EndpointStateChange, java.io.Serializable {
  /**    */
  private Endpoint endpoint;
 public Endpoint getEndpoint() {
   return endpoint;
 }

 @JsonDeserialize( as=Endpoint_impl_ari_0_0_1.class )
 public void setEndpoint(Endpoint val ) {
   endpoint = val;
 }

}

