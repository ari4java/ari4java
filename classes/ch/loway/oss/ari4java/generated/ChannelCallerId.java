package ch.loway.oss.ari4java.generated;

import java.util.Date;
import java.util.List;

/** ----------------------------------------------------
  * Channel changed Caller ID.
  * Defined in file: events.json
  * ------------------------------------------------- */

public class ChannelCallerId extends Event implements java.io.Serializable {
  /**  The integer representation of the Caller Presentation value.  */
  public int caller_presentation;
  /**  The text representation of the Caller Presentation value.  */
  public String caller_presentation_txt;
  /**  The channel that changed Caller ID.  */
  public Channel channel;
}

