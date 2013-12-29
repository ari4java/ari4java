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

// void setDevice_names List<? extends String>
/**********************************************************
 * Names of the devices subscribed to.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public void setDevice_names(List<? extends String> val );



// void setName String
/**********************************************************
 * Name of this application
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public void setName(String val );



// void setBridge_ids List<? extends String>
/**********************************************************
 * Id's for bridges subscribed to.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public void setBridge_ids(List<? extends String> val );



// List<? extends String> getDevice_names
/**********************************************************
 * Names of the devices subscribed to.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public List<? extends String> getDevice_names();



// List<? extends String> getEndpoint_ids
/**********************************************************
 * {tech}/{resource} for endpoints subscribed to.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public List<? extends String> getEndpoint_ids();



// void setChannel_ids List<? extends String>
/**********************************************************
 * Id's for channels subscribed to.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public void setChannel_ids(List<? extends String> val );



// List<? extends String> getBridge_ids
/**********************************************************
 * Id's for bridges subscribed to.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public List<? extends String> getBridge_ids();



// String getName
/**********************************************************
 * Name of this application
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public String getName();



// void setEndpoint_ids List<? extends String>
/**********************************************************
 * {tech}/{resource} for endpoints subscribed to.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public void setEndpoint_ids(List<? extends String> val );



// List<? extends String> getChannel_ids
/**********************************************************
 * Id's for channels subscribed to.
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public List<? extends String> getChannel_ids();


}
;
