package ch.loway.oss.ari4java.generated.ari_1_9_0.models;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
//    Generated on: Wed Mar 30 16:50:37 COT 2016
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**********************************************************
 * Detailed information about a remote peer that communicates with Asterisk.
 * 
 * Defined in file: events.json
 * Generated by: Model
 *********************************************************/

public class Peer_impl_ari_1_9_0 implements Peer, java.io.Serializable {
private static final long serialVersionUID = 1L;
  /**  The IP address of the peer.  */
  private String address;
 public String getAddress() {
   return address;
 }

 @JsonDeserialize( as=String.class )
 public void setAddress(String val ) {
   address = val;
 }

  /**  An optional reason associated with the change in peer_status.  */
  private String cause;
 public String getCause() {
   return cause;
 }

 @JsonDeserialize( as=String.class )
 public void setCause(String val ) {
   cause = val;
 }

  /**  The current state of the peer. Note that the values of the status are dependent on the underlying peer technology.  */
  private String peer_status;
 public String getPeer_status() {
   return peer_status;
 }

 @JsonDeserialize( as=String.class )
 public void setPeer_status(String val ) {
   peer_status = val;
 }

  /**  The port of the peer.  */
  private String port;
 public String getPort() {
   return port;
 }

 @JsonDeserialize( as=String.class )
 public void setPort(String val ) {
   port = val;
 }

  /**  The last known time the peer was contacted.  */
  private String time;
 public String getTime() {
   return time;
 }

 @JsonDeserialize( as=String.class )
 public void setTime(String val ) {
   time = val;
 }

/** No missing signatures from interface */
}

