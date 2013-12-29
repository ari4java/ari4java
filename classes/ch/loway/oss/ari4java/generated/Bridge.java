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

public interface Bridge {

// void setBridge_class String
/**********************************************************
 * Bridging class
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public void setBridge_class(String val );



// String getId
/**********************************************************
 * Unique identifier for this bridge
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public String getId();



// void setChannels List<? extends String>
/**********************************************************
 * Ids of channels participating in this bridge
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public void setChannels(List<? extends String> val );



// String getBridge_type
/**********************************************************
 * Type of bridge technology
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public String getBridge_type();



// void setId String
/**********************************************************
 * Unique identifier for this bridge
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public void setId(String val );



// String getBridge_class
/**********************************************************
 * Bridging class
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public String getBridge_class();



// void setTechnology String
/**********************************************************
 * Name of the current bridging technology
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public void setTechnology(String val );



// String getCreator
/**********************************************************
 * Entity that created the bridge
 * 
 * @since: ari_1_0_0
 *********************************************************/
 public String getCreator();



// String getTechnology
/**********************************************************
 * Name of the current bridging technology
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public String getTechnology();



// void setBridge_type String
/**********************************************************
 * Type of bridge technology
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public void setBridge_type(String val );



// void setName String
/**********************************************************
 * Name the creator gave the bridge
 * 
 * @since: ari_1_0_0
 *********************************************************/
 public void setName(String val );



// List<? extends String> getChannels
/**********************************************************
 * Ids of channels participating in this bridge
 * 
 * @since: ari_0_0_1
 *********************************************************/
 public List<? extends String> getChannels();



// String getName
/**********************************************************
 * Name the creator gave the bridge
 * 
 * @since: ari_1_0_0
 *********************************************************/
 public String getName();



// void setCreator String
/**********************************************************
 * Entity that created the bridge
 * 
 * @since: ari_1_0_0
 *********************************************************/
 public void setCreator(String val );


}
;
