package ch.loway.oss.ari4java.generated.ari_1_5_0.models;

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
 * An external device that may offer/accept calls to/from Asterisk.
 * 
 * Unlike most resources, which have a single unique identifier, an endpoint is uniquely identified by the technology/resource pair.
 * 
 * Defined in file: endpoints.json
 *********************************************************/

public class Endpoint_impl_ari_1_5_0 implements Endpoint, java.io.Serializable {
private static final long serialVersionUID = 1L;
  /**  Id's of channels associated with this endpoint  */
  private List<? extends String> channel_ids;
 public List<? extends String> getChannel_ids() {
   return channel_ids;
 }

 @JsonDeserialize( contentAs=String.class )
 public void setChannel_ids(List<? extends String> val ) {
   channel_ids = val;
 }

  /**  Identifier of the endpoint, specific to the given technology.  */
  private String resource;
 public String getResource() {
   return resource;
 }

 @JsonDeserialize( as=String.class )
 public void setResource(String val ) {
   resource = val;
 }

  /**  Endpoint's state  */
  private String state;
 public String getState() {
   return state;
 }

 @JsonDeserialize( as=String.class )
 public void setState(String val ) {
   state = val;
 }

  /**  Technology of the endpoint  */
  private String technology;
 public String getTechnology() {
   return technology;
 }

 @JsonDeserialize( as=String.class )
 public void setTechnology(String val ) {
   technology = val;
 }

/** No missing signatures from interface */
}

