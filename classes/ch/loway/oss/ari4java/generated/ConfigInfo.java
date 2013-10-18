package ch.loway.oss.ari4java.generated;

import java.util.Date;
import java.util.List;

/** ----------------------------------------------------
  * Info about Asterisk configuration
  * Defined in file: asterisk.json
  * ------------------------------------------------- */

public class ConfigInfo implements java.io.Serializable {
  /**  Default language for media playback.  */
  public String default_language;
  /**  Maximum number of simultaneous channels.  */
  public int max_channels;
  /**  Maximum load avg on system.  */
  public double max_load;
  /**  Maximum number of open file handles (files, sockets).  */
  public int max_open_files;
  /**  Asterisk system name.  */
  public String name;
  /**  Effective user/group id for running Asterisk.  */
  public SetId setid;
}

