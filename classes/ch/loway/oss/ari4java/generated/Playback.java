package ch.loway.oss.ari4java.generated;

import java.util.Date;
import java.util.List;

/** ----------------------------------------------------
  * Object representing the playback of media to a channel
  * Defined in file: playback.json
  * ------------------------------------------------- */

public class Playback implements java.io.Serializable {
  /**  ID for this playback operation  */
  public String id;
  /**  For media types that support multiple languages, the language requested for playback.  */
  public String language;
  /**  URI for the media to play back.  */
  public String media_uri;
  /**  Current state of the playback operation.  */
  public String state;
  /**  URI for the channel or bridge to play the media on  */
  public String target_uri;
}

