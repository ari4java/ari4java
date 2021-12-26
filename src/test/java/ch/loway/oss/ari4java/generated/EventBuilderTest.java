package ch.loway.oss.ari4java.generated;

import ch.loway.oss.ari4java.generated.ari_0_0_1.models.Message_impl_ari_0_0_1;
import ch.loway.oss.ari4java.generated.models.Message;
import ch.loway.oss.ari4java.generated.models.StasisStart;
import ch.loway.oss.ari4java.tools.BaseAriAction;
import ch.loway.oss.ari4java.tools.RestException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author lenz
 */
public class EventBuilderTest extends BaseActionTest<Message> {

    @Override
    protected String getJson() {
        return requoteString(""
                + " {"
                + "   'application': 'hello',"
                + "   'args': ["
                + "     'world'"
                + "   ],"
                + "   'channel': {"
                + "     'accountcode': '',"
                + "     'caller': {"
                + "       'name': 'blink',"
                + "       'number': 'blink'"
                + "     },"
                + "     'connected': {"
                + "       'name': '',"
                + "       'number': ''"
                + "     },"
                + "     'creationtime': '2013-10-15T15:54:12.625-0500',"
                + "     'dialplan': {"
                + "       'context': 'default',"
                + "       'exten': '7000',"
                + "       'priority': 2"
                + "     },"
                + "     'id': '1381870452.0',"
                + "     'name': 'SIP/blink-00000000',"
                + "     'state': 'Ring'"
                + "   },"
                + "   'timestamp': '2013-10-15T15:54:12.626-0500',"
                + "   'type': 'StasisStart'"
                + " }");
    }

    @Override
    protected Message getInstance() {
        try {
            return BaseAriAction.deserializeEvent(getJson(), Message_impl_ari_0_0_1.class);
        } catch (RestException e) {
            fail("Error deserializing event", e);
            return null;
        }
    }

    protected Message createWForcedResponse() {
        return getInstance();
    }

    @Override
    protected void ariActionTest(Message msg) throws RestException {
        assertInstanceOf(StasisStart.class, msg);
        StasisStart ss = (StasisStart) msg;
        assertEquals("blink", ss.getChannel().getCaller().getName());
    }
}
