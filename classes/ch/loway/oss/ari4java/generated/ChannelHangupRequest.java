package ch.loway.oss.ari4java.generated;

import java.util.Date;
import java.util.List;

/** ----------------------------------------------------
  * A hangup was requested on the channel.
  * Defined in file: events.json
  * ------------------------------------------------- */

public class ChannelHangupRequest extends Event implements java.io.Serializable {
  /**  Integer representation of the cause of the hangup.  */
  public int cause;
  /**  The channel on which the hangup was requested.  */
  public Channel channel;
  /**  Whether the hangup request was a soft hangup request.  */
  public boolean soft;
}

