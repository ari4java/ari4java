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

public interface ActionEvents {

// void eventWebsocket String AriCallback<Message> callback
/**********************************************************
 * 
 * 
 * @since: ari_0_0_1
 *********************************************************/
public void eventWebsocket(String app, AriCallback<Message> callback);



// Message eventWebsocket String
/**********************************************************
 * WebSocket connection for events.
 * 
 * 
 * @since: ari_0_0_1
 *********************************************************/
public Message eventWebsocket(String app) throws RestException;


}
;
