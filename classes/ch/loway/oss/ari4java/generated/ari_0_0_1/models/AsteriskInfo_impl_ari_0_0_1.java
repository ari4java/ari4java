package ch.loway.oss.ari4java.generated.ari_0_0_1.models;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import java.util.Date;
import java.util.List;

/** =====================================================
 * Asterisk system information
 * 
 * Defined in file :asterisk.json
 * ====================================================== */
public class AsteriskInfo_impl_ari_0_0_1 implements AsteriskInfo, java.io.Serializable {
  /**  Info about how Asterisk was built  */
  private BuildInfo build;
 public BuildInfo getBuild() {
   return build;
 }

 public void setBuild(BuildInfo val ) {
   build = val;
 }

  /**  Info about Asterisk configuration  */
  private ConfigInfo config;
 public ConfigInfo getConfig() {
   return config;
 }

 public void setConfig(ConfigInfo val ) {
   config = val;
 }

  /**  Info about Asterisk status  */
  private StatusInfo status;
 public StatusInfo getStatus() {
   return status;
 }

 public void setStatus(StatusInfo val ) {
   status = val;
 }

  /**  Info about the system running Asterisk  */
  private SystemInfo system;
 public SystemInfo getSystem() {
   return system;
 }

 public void setSystem(SystemInfo val ) {
   system = val;
 }

}

