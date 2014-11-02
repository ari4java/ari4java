/*
 * Hello ARI World!
 */
package helloworld;

import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.AriFactory;
import ch.loway.oss.ari4java.AriVersion;
import ch.loway.oss.ari4java.generated.AsteriskInfo;
import ch.loway.oss.ari4java.generated.Channel;
import java.util.List;

/**
 * This is a sample printig current system information.
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
            
            ARI ari = AriFactory.nettyHttp(
                    "http://10.10.5.41:8088/", 
                    "ari4java", "yothere", 
                    AriVersion.ARI_1_5_0);
            
            AsteriskInfo info = ari.asterisk().getInfo("");
            List<Channel> channels = ari.channels().list();
            
            System.out.println("There are " + channels.size() + " active channels now.");
            System.out.println( "System up since " + info.getStatus().getStartup_time() );
            
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

