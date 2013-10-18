package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;

public interface ActionAsterisk {

// AsteriskInfo getAsteriskInfo String
/** =====================================================
 * Gets Asterisk system information.
 * ====================================================== */
public AsteriskInfo getAsteriskInfo(String only) throws RestException;



// Variable getGlobalVar String
/** =====================================================
 * Get the value of a global variable.
 * ====================================================== */
public Variable getGlobalVar(String variable) throws RestException;



// void setGlobalVar String String
/** =====================================================
 * Set the value of a global variable.
 * ====================================================== */
public void setGlobalVar(String variable, String value) throws RestException;


}
;
