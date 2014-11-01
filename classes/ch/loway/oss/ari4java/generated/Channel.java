package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
//    Generated on: Sat Nov 01 15:52:13 CET 2014
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;
import ch.loway.oss.ari4java.tools.AriCallback;

public interface Channel {

// void setState String
/**********************************************************
 * 
 * 
 * @since ari_0_0_1
 *********************************************************/
 public void setState(String val );



// String getId
/**********************************************************
 * Unique identifier of the channel.
 * 
 * This is the same as the Uniqueid field in AMI.
 * 
 * @since ari_0_0_1
 *********************************************************/
 public String getId();



// void setAccountcode String
/**********************************************************
 * 
 * 
 * @since ari_0_0_1
 *********************************************************/
 public void setAccountcode(String val );



// CallerID getConnected
/**********************************************************
 * 
 * 
 * @since ari_0_0_1
 *********************************************************/
 public CallerID getConnected();



// String getAccountcode
/**********************************************************
 * 
 * 
 * @since ari_0_0_1
 *********************************************************/
 public String getAccountcode();



// void setConnected CallerID
/**********************************************************
 * 
 * 
 * @since ari_0_0_1
 *********************************************************/
 public void setConnected(CallerID val );



// CallerID getCaller
/**********************************************************
 * 
 * 
 * @since ari_0_0_1
 *********************************************************/
 public CallerID getCaller();



// DialplanCEP getDialplan
/**********************************************************
 * Current location in the dialplan
 * 
 * @since ari_0_0_1
 *********************************************************/
 public DialplanCEP getDialplan();



// void setId String
/**********************************************************
 * Unique identifier of the channel.
 * 
 * This is the same as the Uniqueid field in AMI.
 * 
 * @since ari_0_0_1
 *********************************************************/
 public void setId(String val );



// String getState
/**********************************************************
 * 
 * 
 * @since ari_0_0_1
 *********************************************************/
 public String getState();



// void setName String
/**********************************************************
 * Name of the channel (i.e. SIP/foo-0000a7e3)
 * 
 * @since ari_0_0_1
 *********************************************************/
 public void setName(String val );



// void setCaller CallerID
/**********************************************************
 * 
 * 
 * @since ari_0_0_1
 *********************************************************/
 public void setCaller(CallerID val );



// Date getCreationtime
/**********************************************************
 * Timestamp when channel was created
 * 
 * @since ari_0_0_1
 *********************************************************/
 public Date getCreationtime();



// String getName
/**********************************************************
 * Name of the channel (i.e. SIP/foo-0000a7e3)
 * 
 * @since ari_0_0_1
 *********************************************************/
 public String getName();



// void setCreationtime Date
/**********************************************************
 * Timestamp when channel was created
 * 
 * @since ari_0_0_1
 *********************************************************/
 public void setCreationtime(Date val );



// void setDialplan DialplanCEP
/**********************************************************
 * Current location in the dialplan
 * 
 * @since ari_0_0_1
 *********************************************************/
 public void setDialplan(DialplanCEP val );


}
;
