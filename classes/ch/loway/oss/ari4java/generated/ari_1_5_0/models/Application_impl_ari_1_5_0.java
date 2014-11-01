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
 * Details of a Stasis application
 * 
 * Defined in file: applications.json
 *********************************************************/

public class Application_impl_ari_1_5_0 implements Application, java.io.Serializable {
private static final long serialVersionUID = 1L;
  /**  Id's for bridges subscribed to.  */
  private List<? extends String> bridge_ids;
 public List<? extends String> getBridge_ids() {
   return bridge_ids;
 }

 @JsonDeserialize( contentAs=String.class )
 public void setBridge_ids(List<? extends String> val ) {
   bridge_ids = val;
 }

  /**  Id's for channels subscribed to.  */
  private List<? extends String> channel_ids;
 public List<? extends String> getChannel_ids() {
   return channel_ids;
 }

 @JsonDeserialize( contentAs=String.class )
 public void setChannel_ids(List<? extends String> val ) {
   channel_ids = val;
 }

  /**  Names of the devices subscribed to.  */
  private List<? extends String> device_names;
 public List<? extends String> getDevice_names() {
   return device_names;
 }

 @JsonDeserialize( contentAs=String.class )
 public void setDevice_names(List<? extends String> val ) {
   device_names = val;
 }

  /**  {tech}/{resource} for endpoints subscribed to.  */
  private List<? extends String> endpoint_ids;
 public List<? extends String> getEndpoint_ids() {
   return endpoint_ids;
 }

 @JsonDeserialize( contentAs=String.class )
 public void setEndpoint_ids(List<? extends String> val ) {
   endpoint_ids = val;
 }

  /**  Name of this application  */
  private String name;
 public String getName() {
   return name;
 }

 @JsonDeserialize( as=String.class )
 public void setName(String val ) {
   name = val;
 }

/** No missing signatures from interface */
}

