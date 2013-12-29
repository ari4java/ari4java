/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.loway.oss.ari4java.connector.netty;

import ch.loway.oss.ari4java.connector.netty.BlockingHttpClient.HttpClientHandler;
import ch.loway.oss.ari4java.tools.BaseAriAction.HttpParam;
import io.netty.handler.codec.http.HttpMethod;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author lenz
 */
public class BlockingHttpClientTest {

    public BlockingHttpClientTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testRunClient() throws InterruptedException, URISyntaxException {

//        BlockingHttpClient client = new BlockingHttpClient();
//        HttpClientHandler out = client.httpRequestViaNetty( new URI("http://10.10.5.49:8088/ari/channels/1234/answer"), HttpMethod.POST,  "lenz", "pippo", new ArrayList<HttpParam>() );
//
//        System.out.println( "--------" );
//        System.out.println( out.sb.toString() );
//
//        System.out.println( "Received: " + out.sb.length() + " chars");

        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

}