package ch.loway.oss.ari4java;

import ch.loway.oss.ari4java.generated.actions.ActionAsterisk;
import ch.loway.oss.ari4java.generated.actions.ActionBridges;
import ch.loway.oss.ari4java.generated.ari_0_0_1.actions.ActionAsterisk_impl_ari_0_0_1;
import ch.loway.oss.ari4java.generated.ari_0_0_1.actions.ActionBridges_impl_ari_0_0_1;
import ch.loway.oss.ari4java.generated.ari_1_0_0.actions.ActionAsterisk_impl_ari_1_0_0;
import ch.loway.oss.ari4java.tools.ARIException;
import ch.loway.oss.ari4java.tools.HttpClient;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * @author lenz
 */
public class ARITest {

    @Test
    public void testImplementationFactory() throws Exception {
        ARI ari = new ARI();
        ari.setVersion(AriVersion.ARI_0_0_1);
        assertEquals(ActionBridges_impl_ari_0_0_1.class, ari.getModelImpl(ActionBridges.class).getClass());
        boolean exception = false;
        try {
            ari.getModelImpl(String.class);
        } catch (ARIException e) {
            exception = e.getMessage().contains("No concrete implementation");
        }
        assertTrue("Expected 'No concrete implementation' exception for getModelImpl(String.class)", exception);
        exception = false;
        try {
            ari.getActionImpl(String.class);
        } catch (ARIException e) {
            exception = e.getMessage().contains("Invalid Class");
        }
        assertTrue("Expected 'Invalid Class' exception for getModelImpl(String.class)", exception);
        assertNotNull("Expected Action", ari.getActionImpl(ActionAsterisk.class));
    }

    @Test
    public void testBuildAction() {
        ARI ari = new ARI();
        ari.setVersion(AriVersion.ARI_0_0_1);
        ari.setHttpClient(Mockito.mock(HttpClient.class));

        ActionAsterisk asterisk = ari.asterisk();
        assertEquals(ActionAsterisk_impl_ari_0_0_1.class.toString(), asterisk.getClass().toString());

        ari.setVersion(AriVersion.ARI_1_0_0);
        asterisk = ari.asterisk();
        assertEquals(ActionAsterisk_impl_ari_1_0_0.class.toString(), asterisk.getClass().toString());
        assertNotNull("Expecting HttpClient to be set", ((ActionAsterisk_impl_ari_1_0_0)asterisk).getHttpClient());
    }

    @Test
    public void testCreateUid() {
        String v = ARI.getUID();
        assertTrue("UID created", v.length() > 0);
    }

}
