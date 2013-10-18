package ch.loway.oss.ari4java.generated;

import java.util.Date;
import java.util.List;

/** ----------------------------------------------------
  * User-generated event with additional user-defined fields in the object.
  * Defined in file: events.json
  * ------------------------------------------------- */

public class ChannelUserevent extends Event implements java.io.Serializable {
  /**  The channel that signaled the user event.  */
  public Channel channel;
  /**  The name of the user event.  */
  public String eventname;
  /**  Custom Userevent data  */
  public String userevent;
}

