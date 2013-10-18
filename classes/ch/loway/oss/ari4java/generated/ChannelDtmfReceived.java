package ch.loway.oss.ari4java.generated;

import java.util.Date;
import java.util.List;

/** ----------------------------------------------------
  * DTMF received on a channel.

This event is sent when the DTMF ends. There is no notification about the start of DTMF
  * Defined in file: events.json
  * ------------------------------------------------- */

public class ChannelDtmfReceived extends Event implements java.io.Serializable {
  /**  The channel on which DTMF was received  */
  public Channel channel;
  /**  DTMF digit received (0-9, A-E, # or *)  */
  public String digit;
  /**  Number of milliseconds DTMF was received  */
  public int duration_ms;
}

