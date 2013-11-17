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

public interface ActionAsterisk {

// void getAsteriskInfo String AriCallback<AsteriskInfo> callback
/**********************************************************
 * 
 *********************************************************/
public void getAsteriskInfo(String only, AriCallback<AsteriskInfo> callback);



// void getGlobalVar String AriCallback<Variable> callback
/**********************************************************
 * 
 *********************************************************/
public void getGlobalVar(String variable, AriCallback<Variable> callback);



// void setGlobalVar String String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void setGlobalVar(String variable, String value, AriCallback<Void> callback);



// AsteriskInfo getAsteriskInfo String
/**********************************************************
 * Gets Asterisk system information.
 *********************************************************/
public AsteriskInfo getAsteriskInfo(String only) throws RestException;



// Variable getGlobalVar String
/**********************************************************
 * Get the value of a global variable.
 *********************************************************/
public Variable getGlobalVar(String variable) throws RestException;



// void setGlobalVar String String
/**********************************************************
 * Set the value of a global variable.
 *********************************************************/
public void setGlobalVar(String variable, String value) throws RestException;


}
;
