package ch.loway.oss.ari4java.generated;

import ch.loway.oss.ari4java.generated.actions.ActionBridges;
import ch.loway.oss.ari4java.generated.ari_0_0_1.actions.ActionBridges_impl_ari_0_0_1;
import ch.loway.oss.ari4java.generated.models.*;
import ch.loway.oss.ari4java.tools.RestException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lenz
 */
public class ActonBridgesTest {

    static final String jsonBridge = requoteString( ""
        + "  {   "
        + "  	'id': 'aaa',   "
        + "  	'technology': 'xxx',   "
        + "  	'bridge_type': 'mixing',   "
        + "  	'bridge_class': 'aaa',   "
        + "  	'channels': ['a', 'b', 'c' ]    "
        + "  }   " );


    public ActonBridgesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Strings in a JSON object need the double quotes.
     * Unfortunately using double quotes in Java is a PITA.
     * So...
     * 
     * @param s
     * @return Translating 's to "s
     */

    public static String requoteString( String s ) {
        return s.replace("'", "\"");
    }


    private ActionBridges createWForcedResponse( String response ) {
        ActionBridges_impl_ari_0_0_1 a = new ActionBridges_impl_ari_0_0_1();
        a.setForcedResponse( response );

        ActionBridges aa = (ActionBridges) a;
        return aa;
    }


    /**
     * Tries generating a bridge.
     *
     */

    @Test
    public void generateABridge() throws RestException {
        ActionBridges aa = createWForcedResponse(jsonBridge);

        Bridge b = aa.get("abcd").execute();

        assertEquals("Id", "aaa", b.getId() );
        assertEquals("N channels", 3, b.getChannels().size() );

    }


    /**
     * In this example we force the wrong result.
     * The object cannot then be deserialized and we expect an exception to be thrown.
     *
     * @throws RestException
     */

    @Test
    public void receiveWrongMessage() throws RestException {

        ActionBridges aa = createWForcedResponse(jsonBridge);

        boolean exceptionRaised = false;

        try {
            Playback pb = aa.play("aaa", "sss").execute();
        } catch ( RestException e ) {
            exceptionRaised = true;
        }

        assertTrue("Exception triggered", exceptionRaised);

    }


}
