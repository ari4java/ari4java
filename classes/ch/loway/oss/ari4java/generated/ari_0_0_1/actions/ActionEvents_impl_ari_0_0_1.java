package ch.loway.oss.ari4java.generated.ari_0_0_1.actions;
import ch.loway.oss.ari4java.generated.*;
import ch.loway.oss.ari4java.generated.ari_0_0_1.models.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.*;
public class ActionEvents_impl_ari_0_0_1 extends BaseAriAction  implements ActionEvents {
/** =====================================================
 * Events from Asterisk to applications
 * 
 * WebSocket connection for events.
 * ====================================================== */
public Message eventWebsocket(String app) throws RestException {
String url = "/events";
List<BaseAriAction.HttpParam> lP = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lP.add( BaseAriAction.HttpParam.build( "app", app) );
String json = httpAction( url, "GET", lP, lE);
return (Message) deserializeJson( json, Message.class); 
}

};

