package ch.loway.oss.ari4java.generated.ari_0_0_1.models;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import java.util.List;

/**********************************************************
 * Base type for asynchronous events from Asterisk.
 * 
 * Defined in file: events.json
 *********************************************************/

public class Event_impl_ari_0_0_1 extends Message_impl_ari_0_0_1 implements Event, java.io.Serializable {
private static final long serialVersionUID = 1L;
  /**  Name of the application receiving the event.  */
  private String application;
 public String getApplication() {
   return application;
 }

 @JsonDeserialize( as=String.class )
 public void setApplication(String val ) {
   application = val;
 }

  /**  Time at which this event was created.  */
  private Date timestamp;
 public Date getTimestamp() {
   return timestamp;
 }

 @JsonDeserialize( as=Date.class )
 public void setTimestamp(Date val ) {
   timestamp = val;
 }

}

