package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;

public interface Application {

// void setEndpoint_ids List<String>
/**********************************************************
 * {tech}/{resource} for endpoints subscribed to.
 *********************************************************/
 public void setEndpoint_ids(List<String> val );



// void setBridge_ids List<String>
/**********************************************************
 * Id's for bridges subscribed to.
 *********************************************************/
 public void setBridge_ids(List<String> val );



// List<String> getChannel_ids
/**********************************************************
 * Id's for channels subscribed to.
 *********************************************************/
 public List<String> getChannel_ids();



// List<String> getBridge_ids
/**********************************************************
 * Id's for bridges subscribed to.
 *********************************************************/
 public List<String> getBridge_ids();



// void setName String
/**********************************************************
 * Name of this application
 *********************************************************/
 public void setName(String val );



// List<String> getEndpoint_ids
/**********************************************************
 * {tech}/{resource} for endpoints subscribed to.
 *********************************************************/
 public List<String> getEndpoint_ids();



// void setChannel_ids List<String>
/**********************************************************
 * Id's for channels subscribed to.
 *********************************************************/
 public void setChannel_ids(List<String> val );



// String getName
/**********************************************************
 * Name of this application
 *********************************************************/
 public String getName();


}
;
