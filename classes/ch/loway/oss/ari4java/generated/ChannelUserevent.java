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

public interface ChannelUserevent {

// void setEventname String
/**********************************************************
 * The name of the user event.
 *********************************************************/
 public void setEventname(String val );



// void setUserevent String
/**********************************************************
 * Custom Userevent data
 *********************************************************/
 public void setUserevent(String val );



// String getEventname
/**********************************************************
 * The name of the user event.
 *********************************************************/
 public String getEventname();



// Channel getChannel
/**********************************************************
 * The channel that signaled the user event.
 *********************************************************/
 public Channel getChannel();



// String getUserevent
/**********************************************************
 * Custom Userevent data
 *********************************************************/
 public String getUserevent();



// void setChannel Channel
/**********************************************************
 * The channel that signaled the user event.
 *********************************************************/
 public void setChannel(Channel val );


}
;
