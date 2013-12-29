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
 * The merging of media from one or more channels.
 * 
 * Everyone on the bridge receives the same audio.
 * 
 * Defined in file: bridges.json
 *********************************************************/

public class Bridge_impl_ari_0_0_1 implements Bridge, java.io.Serializable {
private static final long serialVersionUID = 1L;
  /**  Bridging class  */
  private String bridge_class;
 public String getBridge_class() {
   return bridge_class;
 }

 @JsonDeserialize( as=String.class )
 public void setBridge_class(String val ) {
   bridge_class = val;
 }

  /**  Type of bridge technology  */
  private String bridge_type;
 public String getBridge_type() {
   return bridge_type;
 }

 @JsonDeserialize( as=String.class )
 public void setBridge_type(String val ) {
   bridge_type = val;
 }

  /**  Ids of channels participating in this bridge  */
  private List<? extends String> channels;
 public List<? extends String> getChannels() {
   return channels;
 }

 @JsonDeserialize( contentAs=String.class )
 public void setChannels(List<? extends String> val ) {
   channels = val;
 }

  /**  Unique identifier for this bridge  */
  private String id;
 public String getId() {
   return id;
 }

 @JsonDeserialize( as=String.class )
 public void setId(String val ) {
   id = val;
 }

  /**  Name of the current bridging technology  */
  private String technology;
 public String getTechnology() {
   return technology;
 }

 @JsonDeserialize( as=String.class )
 public void setTechnology(String val ) {
   technology = val;
 }

}

