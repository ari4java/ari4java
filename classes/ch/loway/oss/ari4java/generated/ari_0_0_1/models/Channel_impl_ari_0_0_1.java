package ch.loway.oss.ari4java.generated.ari_0_0_1.models;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import java.util.Date;
import java.util.List;

/** =====================================================
 * A specific communication connection between Asterisk and an Endpoint.
 * 
 * Defined in file :channels.json
 * ====================================================== */
public class Channel_impl_ari_0_0_1 implements Channel, java.io.Serializable {
  /**    */
  private String accountcode;
 public String getAccountcode() {
   return accountcode;
 }

 public void setAccountcode(String val ) {
   accountcode = val;
 }

  /**    */
  private CallerID caller;
 public CallerID getCaller() {
   return caller;
 }

 public void setCaller(CallerID val ) {
   caller = val;
 }

  /**    */
  private CallerID connected;
 public CallerID getConnected() {
   return connected;
 }

 public void setConnected(CallerID val ) {
   connected = val;
 }

  /**  Timestamp when channel was created  */
  private Date creationtime;
 public Date getCreationtime() {
   return creationtime;
 }

 public void setCreationtime(Date val ) {
   creationtime = val;
 }

  /**  Current location in the dialplan  */
  private DialplanCEP dialplan;
 public DialplanCEP getDialplan() {
   return dialplan;
 }

 public void setDialplan(DialplanCEP val ) {
   dialplan = val;
 }

  /**  Unique identifier of the channel.

This is the same as the Uniqueid field in AMI.  */
  private String id;
 public String getId() {
   return id;
 }

 public void setId(String val ) {
   id = val;
 }

  /**  Name of the channel (i.e. SIP/foo-0000a7e3)  */
  private String name;
 public String getName() {
   return name;
 }

 public void setName(String val ) {
   name = val;
 }

  /**    */
  private String state;
 public String getState() {
   return state;
 }

 public void setState(String val ) {
   state = val;
 }

}

