/*
 * Hello ARI World!
 */
package helloworld;

import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.AriFactory;
import ch.loway.oss.ari4java.AriVersion;
import ch.loway.oss.ari4java.generated.Channel;
import ch.loway.oss.ari4java.tools.http.NettyHttpClient;
import java.util.List;

/**
 *
 * @author lenz
 */
public class HelloAriWorld {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println("Hello ARI world!");

        try {
            
            ARI ari = AriFactory.nettyHttp("http://10.10.5.41:8088/", "ari4java", "yothere", 
                    AriVersion.ARI_1_5_0);
            
            List<? extends Channel> channels = ari.channels().list();

            System.out.println("There are " + channels.size() + " active channels now.");

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

