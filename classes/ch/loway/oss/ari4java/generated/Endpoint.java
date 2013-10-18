package ch.loway.oss.ari4java.generated;

import java.util.Date;
import java.util.List;

/** ----------------------------------------------------
  * An external device that may offer/accept calls to/from Asterisk.

Unlike most resources, which have a single unique identifier, an endpoint is uniquely identified by the technology/resource pair.
  * Defined in file: endpoints.json
  * ------------------------------------------------- */

public class Endpoint implements java.io.Serializable {
  /**  Id's of channels associated with this endpoint  */
  public List<String> channel_ids;
  /**  Identifier of the endpoint, specific to the given technology.  */
  public String resource;
  /**  Endpoint's state  */
  public String state;
  /**  Technology of the endpoint  */
  public String technology;
}

