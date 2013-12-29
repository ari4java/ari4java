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

public interface Event {

// Date getTimestamp
/**********************************************************
 * Time at which this event was created.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public Date getTimestamp();



// void setTimestamp Date
/**********************************************************
 * Time at which this event was created.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public void setTimestamp(Date val );



// String getApplication
/**********************************************************
 * Name of the application receiving the event.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public String getApplication();



// void setApplication String
/**********************************************************
 * Name of the application receiving the event.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public void setApplication(String val );


}
;
