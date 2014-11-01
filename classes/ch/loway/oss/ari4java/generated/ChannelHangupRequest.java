package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
//    Generated on: Sat Nov 01 19:27:12 CET 2014
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;
import ch.loway.oss.ari4java.tools.AriCallback;

public interface ChannelHangupRequest {

// void setCause int
/**********************************************************
 * Integer representation of the cause of the hangup.
 * 
 * @since ari_0_0_1
 *********************************************************/
 public void setCause(int val );



// int getCause
/**********************************************************
 * Integer representation of the cause of the hangup.
 * 
 * @since ari_0_0_1
 *********************************************************/
 public int getCause();



// void setSoft boolean
/**********************************************************
 * Whether the hangup request was a soft hangup request.
 * 
 * @since ari_0_0_1
 *********************************************************/
 public void setSoft(boolean val );



// Channel getChannel
/**********************************************************
 * The channel on which the hangup was requested.
 * 
 * @since ari_0_0_1
 *********************************************************/
 public Channel getChannel();



// void setChannel Channel
/**********************************************************
 * The channel on which the hangup was requested.
 * 
 * @since ari_0_0_1
 *********************************************************/
 public void setChannel(Channel val );



// boolean getSoft
/**********************************************************
 * Whether the hangup request was a soft hangup request.
 * 
 * @since ari_0_0_1
 *********************************************************/
 public boolean getSoft();


}
;
