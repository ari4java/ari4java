package ch.loway.oss.ari4java.generated;

import java.util.Date;
import java.util.List;

/** ----------------------------------------------------
  * Details of a Stasis application
  * Defined in file: applications.json
  * ------------------------------------------------- */

public class Application implements java.io.Serializable {
  /**  Id's for bridges subscribed to.  */
  public List<String> bridge_ids;
  /**  Id's for channels subscribed to.  */
  public List<String> channel_ids;
  /**  {tech}/{resource} for endpoints subscribed to.  */
  public List<String> endpoint_ids;
  /**  Name of this application  */
  public String name;
}

