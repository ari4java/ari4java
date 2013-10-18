package ch.loway.oss.ari4java.generated;

import java.util.Date;
import java.util.List;

/** ----------------------------------------------------
  * Info about how Asterisk was built
  * Defined in file: asterisk.json
  * ------------------------------------------------- */

public class BuildInfo implements java.io.Serializable {
  /**  Date and time when Asterisk was built.  */
  public String date;
  /**  Kernel version Asterisk was built on.  */
  public String kernel;
  /**  Machine architecture (x86_64, i686, ppc, etc.)  */
  public String machine;
  /**  Compile time options, or empty string if default.  */
  public String options;
  /**  OS Asterisk was built on.  */
  public String os;
  /**  Username that build Asterisk  */
  public String user;
}

