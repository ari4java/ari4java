package ch.loway.oss.ari4java.generated;


import ch.loway.oss.ari4java.generated.models.Message;
import ch.loway.oss.ari4java.generated.models.StasisStart;
import ch.loway.oss.ari4java.generated.ari_0_0_1.models.Message_impl_ari_0_0_1;
import ch.loway.oss.ari4java.tools.BaseAriAction;
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
public class EventBuilderTest {

    static final String jsonStasisStartEvent = requoteString( ""
+ " {   "
+ "   'application': 'hello',   "
+ "   'args': [   "
+ "     'world'   "
+ "   ],   "
+ "   'channel': {   "
+ "     'accountcode': '',   "
+ "     'caller': {   "
+ "       'name': 'blink',   "
+ "       'number': 'blink'   "
+ "     },   "
+ "     'connected': {   "
+ "       'name': '',   "
+ "       'number': ''   "
+ "     },   "
+ "     'creationtime': '2013-10-15T15:54:12.625-0500',   "
+ "     'dialplan': {   "
+ "       'context': 'default',   "
+ "       'exten': '7000',   "
+ "       'priority': 2   "
+ "     },   "
+ "     'id': '1381870452.0',   "
+ "     'name': 'SIP/blink-00000000',   "
+ "     'state': 'Ring'   "
+ "   },   "
+ "   'timestamp': '2013-10-15T15:54:12.626-0500',   "
+ "   'type': 'StasisStart'   "
+ " }   " );

    public EventBuilderTest() {
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



    /**
     * Tries creating an event out of the response.
     *
     */

    @Test
    public void generateStatsiStartOutOfData() throws RestException {

        BaseAriAction action = new BaseAriAction();
        Message msg = action.deserializeEvent(jsonStasisStartEvent, Message_impl_ari_0_0_1.class );

        assertTrue("Type: StasisStart", msg instanceof StasisStart );

        StasisStart ss = (StasisStart) msg;

        assertEquals( "Caller ID", "blink", ss.getChannel().getCaller().getName() );

    }




}
