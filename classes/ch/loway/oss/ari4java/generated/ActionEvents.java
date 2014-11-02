package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
//    Generated on: Sun Nov 02 19:48:29 CET 2014
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;
import ch.loway.oss.ari4java.tools.AriCallback;

public interface ActionEvents {

// void userEvent String String String Map<String,String>
/**********************************************************
 * Generate a user event.
 * 
 * 
 * @since ari_1_5_0
 *********************************************************/
public void userEvent(String eventName, String application, String source, Map<String,String> variables) throws RestException;



// void eventWebsocket String AriCallback<Message> callback
/**********************************************************
 * 
 * 
 * @since ari_0_0_1
 *********************************************************/
public void eventWebsocket(String app, AriCallback<Message> callback);



// Message eventWebsocket String
/**********************************************************
 * WebSocket connection for events.
 * 
 * 
 * @since ari_0_0_1
 *********************************************************/
public Message eventWebsocket(String app) throws RestException;



// void userEvent String String String Map<String,String> AriCallback<Void> callback
/**********************************************************
 * 
 * 
 * @since ari_1_5_0
 *********************************************************/
public void userEvent(String eventName, String application, String source, Map<String,String> variables, AriCallback<Void> callback);


}
;
