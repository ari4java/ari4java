package ch.loway.oss.ari4java.generated;

import java.util.Date;
import java.util.List;

/** ----------------------------------------------------
  * Error event sent when required params are missing.
  * Defined in file: events.json
  * ------------------------------------------------- */

public class MissingParams extends Message implements java.io.Serializable {
  /**  A list of the missing parameters  */
  public List<String> params;
}

