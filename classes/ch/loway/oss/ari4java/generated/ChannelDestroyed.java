package ch.loway.oss.ari4java.generated;

import java.util.Date;
import java.util.List;

/** ----------------------------------------------------
  * Notification that a channel has been destroyed.
  * Defined in file: events.json
  * ------------------------------------------------- */

public class ChannelDestroyed extends Event implements java.io.Serializable {
  /**  Integer representation of the cause of the hangup  */
  public int cause;
  /**  Text representation of the cause of the hangup  */
  public String cause_txt;
  /**    */
  public Channel channel;
}

