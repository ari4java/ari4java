package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;

public interface AsteriskInfo {

// void setConfig ConfigInfo
/** =====================================================
 * Info about Asterisk configuration
 * ====================================================== */
 public void setConfig(ConfigInfo val );



// void setStatus StatusInfo
/** =====================================================
 * Info about Asterisk status
 * ====================================================== */
 public void setStatus(StatusInfo val );



// StatusInfo getStatus
/** =====================================================
 * Info about Asterisk status
 * ====================================================== */
 public StatusInfo getStatus();



// void setBuild BuildInfo
/** =====================================================
 * Info about how Asterisk was built
 * ====================================================== */
 public void setBuild(BuildInfo val );



// BuildInfo getBuild
/** =====================================================
 * Info about how Asterisk was built
 * ====================================================== */
 public BuildInfo getBuild();



// ConfigInfo getConfig
/** =====================================================
 * Info about Asterisk configuration
 * ====================================================== */
 public ConfigInfo getConfig();



// void setSystem SystemInfo
/** =====================================================
 * Info about the system running Asterisk
 * ====================================================== */
 public void setSystem(SystemInfo val );



// SystemInfo getSystem
/** =====================================================
 * Info about the system running Asterisk
 * ====================================================== */
 public SystemInfo getSystem();


}
;
