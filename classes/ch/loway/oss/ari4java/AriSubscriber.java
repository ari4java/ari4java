package ch.loway.oss.ari4java;

import ch.loway.oss.ari4java.generated.Application;
import ch.loway.oss.ari4java.generated.Bridge;
import ch.loway.oss.ari4java.generated.Channel;
import ch.loway.oss.ari4java.generated.DeviceState;
import ch.loway.oss.ari4java.generated.Endpoint;
import ch.loway.oss.ari4java.tools.RestException;
import ch.loway.oss.ari4java.tools.tags.EventSource;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lenz
 */
public class AriSubscriber {

    List<String> subscriptions = new ArrayList();
    
    public Application subscribe( ARI ari, EventSource m ) throws RestException {
        
        String model = toModelName(m);
        Application a = ari.applications().subscribe( ari.getAppName(), model );
        subscriptions.add(model);
        return a;
    }
    
    public void unsubscribe( ARI ari, EventSource m )  throws RestException {
        String model = toModelName(m);
        ari.applications().unsubscribe( ari.getAppName(), model);
        subscriptions.remove(model);
    }
    
    public void unsubscribeAll( ARI ari)  throws RestException {
        for ( String model: subscriptions ) {
            ari.applications().unsubscribe( ari.getAppName(), model);
        }
        subscriptions.clear();
    }
    
    /**
     * Return the correct string "modeltype:id".
     * 
     * See 
     * channel:{channelId}, 
     * bridge:{bridgeId}, 
     * endpoint:{tech}[/{resource}, 
     * deviceState:{deviceName}
     * 
     * @param m
     * @return a string representation, e.g. "channel:1234"
     */
    
    
    public String toModelName( EventSource m ) {
        
        if ( m instanceof Bridge ) {
            Bridge b = (Bridge) m;
            return "bridge:" + b.getId();
        } else
        if ( m instanceof Channel ) {
            Channel b = (Channel) m;
            return "channel:" + b.getId();
        } else
        if ( m instanceof Endpoint ) {
            Endpoint b = (Endpoint) m;
            return "endpoint:" + b.getTechnology() + "/" + b.getResource();
        } else
        if ( m instanceof DeviceState ) {
            DeviceState b = (DeviceState) m;
            return "deviceState:" + b.getName();
        } else
                
        {
            throw new IllegalArgumentException("Cannot subscribe model " + m.getClass().getName() );
        }
        
        
    }
    
}
