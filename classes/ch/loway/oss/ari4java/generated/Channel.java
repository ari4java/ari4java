package ch.loway.oss.ari4java.generated;

import java.util.Date;
import java.util.List;

/** ----------------------------------------------------
  * A specific communication connection between Asterisk and an Endpoint.
  * Defined in file: channels.json
  * ------------------------------------------------- */

public class Channel implements java.io.Serializable {
  /**    */
  public String accountcode;
  /**    */
  public CallerID caller;
  /**    */
  public CallerID connected;
  /**  Timestamp when channel was created  */
  public Date creationtime;
  /**  Current location in the dialplan  */
  public DialplanCEP dialplan;
  /**  Unique identifier of the channel.

This is the same as the Uniqueid field in AMI.  */
  public String id;
  /**  Name of the channel (i.e. SIP/foo-0000a7e3)  */
  public String name;
  /**    */
  public String state;
}

