
package ch.loway.oss.ari4java;

import ch.loway.oss.ari4java.cfg.AriConnectorMode;
import ch.loway.oss.ari4java.cfg.AriVersion;
import ch.loway.oss.ari4java.connector.AriConnector;
import ch.loway.oss.ari4java.connector.ConnectionCredentials;
import ch.loway.oss.ari4java.connector.NettyConnector;
import ch.loway.oss.ari4java.generated.ari_0_0_1.actions.ActionApplications_impl_ari_0_0_1;
import ch.loway.oss.ari4java.generated.ari_0_0_1.actions.ActionAsterisk_impl_ari_0_0_1;
import ch.loway.oss.ari4java.generated.ari_0_0_1.models.AsteriskInfo_impl_ari_0_0_1;

/**
 *
 * 
 * @author lenz
 */
public class ARI {

    public String baseUrl = "";
    public AriVersion version = AriVersion.ARI_0_0_1;

    /**
     * Gets you an instance of AriConnector.
     * 
     * @param mode Which connection mode (actually connector) you require.
     * @param version The version of ARI you should bind to.
     * @return a permanent connector instance.
     */

    public static AriConnector build( AriConnectorMode mode, AriVersion version, ConnectionCredentials creds ) {
        
        AriConnector c = new NettyConnector();
        c.setCredentials( creds );

        switch ( version ) {
            case ARI_0_0_1:

                ActionApplications_impl_ari_0_0_1 applicationsImpl = new ActionApplications_impl_ari_0_0_1();
                applicationsImpl.configure(c);

                ActionAsterisk_impl_ari_0_0_1 asteriskImpl = new ActionAsterisk_impl_ari_0_0_1();
                asteriskImpl.configure(c);

                c.setup( applicationsImpl, asteriskImpl );
                break;
        }


        return c;

    }

    

}

// $Log$
//
