package ch.loway.oss.ari4java.generated.ari_0_0_1.models;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import java.util.List;

/**********************************************************
 * Asterisk system information
 * 
 * Defined in file: asterisk.json
 *********************************************************/

public class AsteriskInfo_impl_ari_0_0_1 implements AsteriskInfo, java.io.Serializable {
private static final long serialVersionUID = 1L;
  /**  Info about how Asterisk was built  */
  private BuildInfo build;
 public BuildInfo getBuild() {
   return build;
 }

 @JsonDeserialize( as=BuildInfo_impl_ari_0_0_1.class )
 public void setBuild(BuildInfo val ) {
   build = val;
 }

  /**  Info about Asterisk configuration  */
  private ConfigInfo config;
 public ConfigInfo getConfig() {
   return config;
 }

 @JsonDeserialize( as=ConfigInfo_impl_ari_0_0_1.class )
 public void setConfig(ConfigInfo val ) {
   config = val;
 }

  /**  Info about Asterisk status  */
  private StatusInfo status;
 public StatusInfo getStatus() {
   return status;
 }

 @JsonDeserialize( as=StatusInfo_impl_ari_0_0_1.class )
 public void setStatus(StatusInfo val ) {
   status = val;
 }

  /**  Info about the system running Asterisk  */
  private SystemInfo system;
 public SystemInfo getSystem() {
   return system;
 }

 @JsonDeserialize( as=SystemInfo_impl_ari_0_0_1.class )
 public void setSystem(SystemInfo val ) {
   system = val;
 }

/** No missing signatures from interface */
}

