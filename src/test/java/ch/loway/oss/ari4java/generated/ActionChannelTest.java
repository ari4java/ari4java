package ch.loway.oss.ari4java.generated;

import ch.loway.oss.ari4java.generated.actions.ActionChannels;
import ch.loway.oss.ari4java.generated.ari_6_0_0.actions.ActionChannels_impl_ari_6_0_0;
import ch.loway.oss.ari4java.generated.models.RTPstat;
import ch.loway.oss.ari4java.tools.RestException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * @author lenz
 */
public class ActionChannelTest extends BaseActionTest<ActionChannels> {

    @Override
    protected String getJson() {
        return requoteString(""
                + "{"
                + " 'local_ssrc': 1176001751,"
                + " 'remote_ssrc': 3175369733"
                + " }");
    }

    @Override
    protected ActionChannels getInstance() {
        return new ActionChannels_impl_ari_6_0_0();
    }

    @Override
    protected void ariActionTest(ActionChannels a) throws RestException {
        RTPstat stat = a.rtpstatistics("abcde").execute();
        // check the generator overwrote the int definition to a long as values larger than 2147483647 are set
        assertInstanceOf(Long.class, stat.getLocal_ssrc());
        assertInstanceOf(Long.class, stat.getRemote_ssrc());
        assertEquals(1176001751L, stat.getLocal_ssrc());
        assertEquals(3175369733L, stat.getRemote_ssrc());
    }

}
