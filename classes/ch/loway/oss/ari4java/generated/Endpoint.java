package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;

public interface Endpoint {

// List<String> getChannel_ids
/** =====================================================
 * Id's of channels associated with this endpoint
 * ====================================================== */
 public List<String> getChannel_ids();



// void setState String
/** =====================================================
 * Endpoint's state
 * ====================================================== */
 public void setState(String val );



// void setChannel_ids List<String>
/** =====================================================
 * Id's of channels associated with this endpoint
 * ====================================================== */
 public void setChannel_ids(List<String> val );



// String getResource
/** =====================================================
 * Identifier of the endpoint, specific to the given technology.
 * ====================================================== */
 public String getResource();



// void setTechnology String
/** =====================================================
 * Technology of the endpoint
 * ====================================================== */
 public void setTechnology(String val );



// String getTechnology
/** =====================================================
 * Technology of the endpoint
 * ====================================================== */
 public String getTechnology();



// void setResource String
/** =====================================================
 * Identifier of the endpoint, specific to the given technology.
 * ====================================================== */
 public void setResource(String val );



// String getState
/** =====================================================
 * Endpoint's state
 * ====================================================== */
 public String getState();


}
;
