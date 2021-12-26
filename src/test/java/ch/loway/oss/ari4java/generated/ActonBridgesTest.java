package ch.loway.oss.ari4java.generated;

import ch.loway.oss.ari4java.generated.actions.ActionBridges;
import ch.loway.oss.ari4java.generated.ari_0_0_1.actions.ActionBridges_impl_ari_0_0_1;
import ch.loway.oss.ari4java.generated.models.Bridge;
import ch.loway.oss.ari4java.tools.RestException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author lenz
 */
public class ActonBridgesTest extends BaseActionTest<ActionBridges> {

    @Override
    protected String getJson() {
        return requoteString(""
                + "{"
                + " 'id': 'aaa',"
                + " 'technology': 'xxx',"
                + " 'bridge_type': 'mixing',"
                + " 'bridge_class': 'aaa',"
                + " 'channels': ['a', 'b', 'c' ]"
                + " }");
    }

    @Override
    protected ActionBridges getInstance() {
        return new ActionBridges_impl_ari_0_0_1();
    }

    @Override
    protected void ariActionTest(ActionBridges a) throws RestException {
        Bridge b = a.get("abcd").execute();
        assertEquals("aaa", b.getId());
        assertEquals(3, b.getChannels().size());

        boolean exceptionRaised = false;
        try {
            a.play("aaa", "sss").execute();
        } catch (RestException e) {
            exceptionRaised = true;
        }
        assertTrue(exceptionRaised, "Exception triggered");
    }
}
