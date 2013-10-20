package ch.loway.oss.ari4java.generated.ari_0_0_1.models;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import java.util.List;

/** =====================================================
 * Info about how Asterisk was built
 * 
 * Defined in file :asterisk.json
 * ====================================================== */
public class BuildInfo_impl_ari_0_0_1 implements BuildInfo, java.io.Serializable {
  /**  Date and time when Asterisk was built.  */
  private String date;
 public String getDate() {
   return date;
 }

 public void setDate(String val ) {
   date = val;
 }

  /**  Kernel version Asterisk was built on.  */
  private String kernel;
 public String getKernel() {
   return kernel;
 }

 public void setKernel(String val ) {
   kernel = val;
 }

  /**  Machine architecture (x86_64, i686, ppc, etc.)  */
  private String machine;
 public String getMachine() {
   return machine;
 }

 public void setMachine(String val ) {
   machine = val;
 }

  /**  Compile time options, or empty string if default.  */
  private String options;
 public String getOptions() {
   return options;
 }

 public void setOptions(String val ) {
   options = val;
 }

  /**  OS Asterisk was built on.  */
  private String os;
 public String getOs() {
   return os;
 }

 public void setOs(String val ) {
   os = val;
 }

  /**  Username that build Asterisk  */
  private String user;
 public String getUser() {
   return user;
 }

 public void setUser(String val ) {
   user = val;
 }

}

