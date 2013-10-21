package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;

public interface Channel {

// void setState String
/**********************************************************
 * 
 *********************************************************/
 public void setState(String val );



// String getId
/**********************************************************
 * Unique identifier of the channel.
 * 
 * This is the same as the Uniqueid field in AMI.
 *********************************************************/
 public String getId();



// CallerID getConnected
/**********************************************************
 * 
 *********************************************************/
 public CallerID getConnected();



// void setAccountcode String
/**********************************************************
 * 
 *********************************************************/
 public void setAccountcode(String val );



// String getAccountcode
/**********************************************************
 * 
 *********************************************************/
 public String getAccountcode();



// void setId String
/**********************************************************
 * Unique identifier of the channel.
 * 
 * This is the same as the Uniqueid field in AMI.
 *********************************************************/
 public void setId(String val );



// CallerID getCaller
/**********************************************************
 * 
 *********************************************************/
 public CallerID getCaller();



// DialplanCEP getDialplan
/**********************************************************
 * Current location in the dialplan
 *********************************************************/
 public DialplanCEP getDialplan();



// void setConnected CallerID
/**********************************************************
 * 
 *********************************************************/
 public void setConnected(CallerID val );



// String getState
/**********************************************************
 * 
 *********************************************************/
 public String getState();



// void setName String
/**********************************************************
 * Name of the channel (i.e. SIP/foo-0000a7e3)
 *********************************************************/
 public void setName(String val );



// void setCaller CallerID
/**********************************************************
 * 
 *********************************************************/
 public void setCaller(CallerID val );



// Date getCreationtime
/**********************************************************
 * Timestamp when channel was created
 *********************************************************/
 public Date getCreationtime();



// String getName
/**********************************************************
 * Name of the channel (i.e. SIP/foo-0000a7e3)
 *********************************************************/
 public String getName();



// void setCreationtime Date
/**********************************************************
 * Timestamp when channel was created
 *********************************************************/
 public void setCreationtime(Date val );



// void setDialplan DialplanCEP
/**********************************************************
 * Current location in the dialplan
 *********************************************************/
 public void setDialplan(DialplanCEP val );


}
;
