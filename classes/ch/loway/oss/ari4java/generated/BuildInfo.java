package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;
import ch.loway.oss.ari4java.tools.AriCallback;

public interface BuildInfo {

// void setUser String
/**********************************************************
 * Username that build Asterisk
 *********************************************************/
 public void setUser(String val );



// void setOptions String
/**********************************************************
 * Compile time options, or empty string if default.
 *********************************************************/
 public void setOptions(String val );



// void setDate String
/**********************************************************
 * Date and time when Asterisk was built.
 *********************************************************/
 public void setDate(String val );



// String getOptions
/**********************************************************
 * Compile time options, or empty string if default.
 *********************************************************/
 public String getOptions();



// void setOs String
/**********************************************************
 * OS Asterisk was built on.
 *********************************************************/
 public void setOs(String val );



// String getDate
/**********************************************************
 * Date and time when Asterisk was built.
 *********************************************************/
 public String getDate();



// String getKernel
/**********************************************************
 * Kernel version Asterisk was built on.
 *********************************************************/
 public String getKernel();



// void setMachine String
/**********************************************************
 * Machine architecture (x86_64, i686, ppc, etc.)
 *********************************************************/
 public void setMachine(String val );



// String getUser
/**********************************************************
 * Username that build Asterisk
 *********************************************************/
 public String getUser();



// String getOs
/**********************************************************
 * OS Asterisk was built on.
 *********************************************************/
 public String getOs();



// String getMachine
/**********************************************************
 * Machine architecture (x86_64, i686, ppc, etc.)
 *********************************************************/
 public String getMachine();



// void setKernel String
/**********************************************************
 * Kernel version Asterisk was built on.
 *********************************************************/
 public void setKernel(String val );


}
;
