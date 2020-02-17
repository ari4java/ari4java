package ch.loway.oss.ari4java;

import ch.loway.oss.ari4java.generated.actions.ActionAsterisk;
import ch.loway.oss.ari4java.generated.actions.ActionBridges;
import ch.loway.oss.ari4java.generated.ari_0_0_1.actions.ActionAsterisk_impl_ari_0_0_1;
import ch.loway.oss.ari4java.generated.ari_0_0_1.actions.ActionBridges_impl_ari_0_0_1;
import ch.loway.oss.ari4java.generated.ari_1_0_0.actions.ActionAsterisk_impl_ari_1_0_0;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author lenz
 */
public class ARITest {

    @Test
    public void testImplementationFactory() {
        ARI.ClassFactory factory = new ARI.ClassFactory() {
            @Override
            public Class getImplementationFor(Class interfaceClass) {
                if (interfaceClass.equals(ActionBridges.class)) {
                    return ActionBridges_impl_ari_0_0_1.class;
                } else {
                    return null;
                }
            }
        };
        assertEquals(ActionBridges_impl_ari_0_0_1.class, factory.getImplementationFor(ActionBridges.class));
        assertNull(factory.getImplementationFor(String.class));
    }

    @Test
    public void testBuildAction() {
        ARI ari = new ARI();
        ari.setVersion(AriVersion.ARI_0_0_1);

        ActionAsterisk asterisk = ari.asterisk();
        assertEquals(ActionAsterisk_impl_ari_0_0_1.class.toString(), asterisk.getClass().toString());

        ari.setVersion(AriVersion.ARI_1_0_0);
        asterisk = ari.asterisk();
        assertEquals(ActionAsterisk_impl_ari_1_0_0.class.toString(), asterisk.getClass().toString());
    }

    @Test
    public void testCreateUid() {
        String v = ARI.getUID();
        assertTrue("UID created", v.length() > 0);
    }

}
