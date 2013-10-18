package ch.loway.oss.ari4java.generated;

import java.util.Date;
import java.util.List;

/** ----------------------------------------------------
  * A media file that may be played back.
  * Defined in file: sounds.json
  * ------------------------------------------------- */

public class Sound implements java.io.Serializable {
  /**  The formats and languages in which this sound is available.  */
  public List<FormatLangPair> formats;
  /**  Sound's identifier.  */
  public String id;
  /**  Text description of the sound, usually the words spoken.  */
  public String text;
}

