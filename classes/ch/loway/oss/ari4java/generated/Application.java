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

public interface Application {

// void setName String
/**********************************************************
 * Name of this application
 *********************************************************/
 public void setName(String val );



// void setBridge_ids List<? extends String>
/**********************************************************
 * Id's for bridges subscribed to.
 *********************************************************/
 public void setBridge_ids(List<? extends String> val );



// List<? extends String> getEndpoint_ids
/**********************************************************
 * {tech}/{resource} for endpoints subscribed to.
 *********************************************************/
 public List<? extends String> getEndpoint_ids();



// void setChannel_ids List<? extends String>
/**********************************************************
 * Id's for channels subscribed to.
 *********************************************************/
 public void setChannel_ids(List<? extends String> val );



// List<? extends String> getBridge_ids
/**********************************************************
 * Id's for bridges subscribed to.
 *********************************************************/
 public List<? extends String> getBridge_ids();



// String getName
/**********************************************************
 * Name of this application
 *********************************************************/
 public String getName();



// void setEndpoint_ids List<? extends String>
/**********************************************************
 * {tech}/{resource} for endpoints subscribed to.
 *********************************************************/
 public void setEndpoint_ids(List<? extends String> val );



// List<? extends String> getChannel_ids
/**********************************************************
 * Id's for channels subscribed to.
 *********************************************************/
 public List<? extends String> getChannel_ids();


}
;
