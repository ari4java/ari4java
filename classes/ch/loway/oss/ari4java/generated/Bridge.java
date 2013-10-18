package ch.loway.oss.ari4java.generated;

import java.util.Date;
import java.util.List;

/** ----------------------------------------------------
  * The merging of media from one or more channels.

Everyone on the bridge receives the same audio.
  * Defined in file: bridges.json
  * ------------------------------------------------- */

public class Bridge implements java.io.Serializable {
  /**  Bridging class  */
  public String bridge_class;
  /**  Type of bridge technology  */
  public String bridge_type;
  /**  Ids of channels participating in this bridge  */
  public List<String> channels;
  /**  Unique identifier for this bridge  */
  public String id;
  /**  Name of the current bridging technology  */
  public String technology;
}

