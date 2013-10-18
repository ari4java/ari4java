package ch.loway.oss.ari4java.generated;

import java.util.Date;
import java.util.List;

/** ----------------------------------------------------
  * Channel variable changed.
  * Defined in file: events.json
  * ------------------------------------------------- */

public class ChannelVarset extends Event implements java.io.Serializable {
  /**  The channel on which the variable was set.

If missing, the variable is a global variable.  */
  public Channel channel;
  /**  The new value of the variable.  */
  public String value;
  /**  The variable that changed.  */
  public String variable;
}

