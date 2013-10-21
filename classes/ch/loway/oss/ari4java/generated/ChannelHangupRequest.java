package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;

public interface ChannelHangupRequest {

// void setCause int
/**********************************************************
 * Integer representation of the cause of the hangup.
 *********************************************************/
 public void setCause(int val );



// int getCause
/**********************************************************
 * Integer representation of the cause of the hangup.
 *********************************************************/
 public int getCause();



// void setSoft boolean
/**********************************************************
 * Whether the hangup request was a soft hangup request.
 *********************************************************/
 public void setSoft(boolean val );



// Channel getChannel
/**********************************************************
 * The channel on which the hangup was requested.
 *********************************************************/
 public Channel getChannel();



// void setChannel Channel
/**********************************************************
 * The channel on which the hangup was requested.
 *********************************************************/
 public void setChannel(Channel val );



// boolean getSoft
/**********************************************************
 * Whether the hangup request was a soft hangup request.
 *********************************************************/
 public boolean getSoft();


}
;
