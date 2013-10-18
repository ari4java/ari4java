package ch.loway.oss.ari4java.generated;

import java.util.Date;
import java.util.List;

/** ----------------------------------------------------
  * Base type for asynchronous events from Asterisk.
  * Defined in file: events.json
  * ------------------------------------------------- */

public class Event extends Message implements java.io.Serializable {
  /**  Name of the application receiving the event.  */
  public String application;
  /**  Time at which this event was created.  */
  public Date timestamp;
}

