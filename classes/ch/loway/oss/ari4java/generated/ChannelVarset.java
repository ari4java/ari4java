package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;

public interface ChannelVarset {

// void setValue String
/** =====================================================
 * The new value of the variable.
 * ====================================================== */
 public void setValue(String val );



// void setVariable String
/** =====================================================
 * The variable that changed.
 * ====================================================== */
 public void setVariable(String val );



// Channel getChannel
/** =====================================================
 * The channel on which the variable was set.
 * 
 * If missing, the variable is a global variable.
 * ====================================================== */
 public Channel getChannel();



// String getVariable
/** =====================================================
 * The variable that changed.
 * ====================================================== */
 public String getVariable();



// String getValue
/** =====================================================
 * The new value of the variable.
 * ====================================================== */
 public String getValue();



// void setChannel Channel
/** =====================================================
 * The channel on which the variable was set.
 * 
 * If missing, the variable is a global variable.
 * ====================================================== */
 public void setChannel(Channel val );


}
;
