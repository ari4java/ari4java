package ch.loway.oss.ari4java.generated.ari_0_0_1.models;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import java.util.Date;
import java.util.List;

/** =====================================================
 * An external device that may offer/accept calls to/from Asterisk.
 * 
 * Unlike most resources, which have a single unique identifier, an endpoint is uniquely identified by the technology/resource pair.
 * 
 * Defined in file :endpoints.json
 * ====================================================== */
public class Endpoint_impl_ari_0_0_1 implements Endpoint, java.io.Serializable {
  /**  Id's of channels associated with this endpoint  */
  private List<String> channel_ids;
 public List<String> getChannel_ids() {
   return channel_ids;
 }

 public void setChannel_ids(List<String> val ) {
   channel_ids = val;
 }

  /**  Identifier of the endpoint, specific to the given technology.  */
  private String resource;
 public String getResource() {
   return resource;
 }

 public void setResource(String val ) {
   resource = val;
 }

  /**  Endpoint's state  */
  private String state;
 public String getState() {
   return state;
 }

 public void setState(String val ) {
   state = val;
 }

  /**  Technology of the endpoint  */
  private String technology;
 public String getTechnology() {
   return technology;
 }

 public void setTechnology(String val ) {
   technology = val;
 }

}

