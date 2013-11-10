package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;

public interface Bridge {

// void setBridge_type String
/**********************************************************
 * Type of bridge technology
 *********************************************************/
 public void setBridge_type(String val );



// void setBridge_class String
/**********************************************************
 * Bridging class
 *********************************************************/
 public void setBridge_class(String val );



// String getId
/**********************************************************
 * Unique identifier for this bridge
 *********************************************************/
 public String getId();



// List<? extends String> getChannels
/**********************************************************
 * Ids of channels participating in this bridge
 *********************************************************/
 public List<? extends String> getChannels();



// void setChannels List<? extends String>
/**********************************************************
 * Ids of channels participating in this bridge
 *********************************************************/
 public void setChannels(List<? extends String> val );



// void setId String
/**********************************************************
 * Unique identifier for this bridge
 *********************************************************/
 public void setId(String val );



// String getBridge_type
/**********************************************************
 * Type of bridge technology
 *********************************************************/
 public String getBridge_type();



// void setTechnology String
/**********************************************************
 * Name of the current bridging technology
 *********************************************************/
 public void setTechnology(String val );



// String getBridge_class
/**********************************************************
 * Bridging class
 *********************************************************/
 public String getBridge_class();



// String getTechnology
/**********************************************************
 * Name of the current bridging technology
 *********************************************************/
 public String getTechnology();


}
;
