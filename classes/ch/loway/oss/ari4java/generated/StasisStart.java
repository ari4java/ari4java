package ch.loway.oss.ari4java.generated;

import java.util.Date;
import java.util.List;

/** ----------------------------------------------------
  * Notification that a channel has entered a Stasis appliction.
  * Defined in file: events.json
  * ------------------------------------------------- */

public class StasisStart extends Event implements java.io.Serializable {
  /**  Arguments to the application  */
  public List<String> args;
  /**    */
  public Channel channel;
}

