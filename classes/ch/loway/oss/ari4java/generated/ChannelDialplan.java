package ch.loway.oss.ari4java.generated;

import java.util.Date;
import java.util.List;

/** ----------------------------------------------------
  * Channel changed location in the dialplan.
  * Defined in file: events.json
  * ------------------------------------------------- */

public class ChannelDialplan extends Event implements java.io.Serializable {
  /**  The channel that changed dialplan location.  */
  public Channel channel;
  /**  The application about to be executed.  */
  public String dialplan_app;
  /**  The data to be passed to the application.  */
  public String dialplan_app_data;
}

