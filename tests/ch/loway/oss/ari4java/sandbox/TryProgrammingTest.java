/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.loway.oss.ari4java.sandbox;

import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.cfg.AriConnectorMode;
import ch.loway.oss.ari4java.cfg.AriVersion;
import ch.loway.oss.ari4java.connector.AriConnector;
import ch.loway.oss.ari4java.connector.ConnectionCredentials;
import ch.loway.oss.ari4java.generated.Application;
import ch.loway.oss.ari4java.generated.AsteriskInfo;
import ch.loway.oss.ari4java.tools.RestException;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author lenz
 */
public class TryProgrammingTest {

    public TryProgrammingTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @Test
    public void testConnectingAndGetListOfChaannels() throws RestException {

        AriConnector conn = ARI.build(AriConnectorMode.HTTP_NETTY, 
                AriVersion.ARI_0_0_1,
                ConnectionCredentials.build("http://10.10.5.49", 8088, "lenz", "pippo"));

//        List<Application> apps = conn.applications().getApplications();

//        AsteriskInfo info = conn.asterisk().getAsteriskInfo("");

        conn.closeConnection();

    }

}