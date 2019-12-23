/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.loway.oss.ari4java;

import ch.loway.oss.ari4java.generated.actions.ActionAsterisk;
import ch.loway.oss.ari4java.generated.actions.ActionBridges;
import ch.loway.oss.ari4java.generated.ari_0_0_1.actions.ActionAsterisk_impl_ari_0_0_1;
import ch.loway.oss.ari4java.generated.ari_0_0_1.actions.ActionBridges_impl_ari_0_0_1;
import ch.loway.oss.ari4java.tools.ARIException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author lenz
 */
public class ARITest {

    public ARITest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


    /**
     * An example abstract to concrete builder.
     */

    public static class SampleClassFactory implements ARI.ClassFactory {

        @Override
        public Class getImplementationFor(Class interfaceClass) {

            if (interfaceClass.equals(ActionBridges.class)) {
                return ActionBridges_impl_ari_0_0_1.class;
            } else {
                return null;
            }
        }
    }

    @Test
    public void testImplementationFactory() {
        ARI.ClassFactory factory = new SampleClassFactory();

        assertEquals("ActionBridges", ActionBridges_impl_ari_0_0_1.class, factory.getImplementationFor(ActionBridges.class));
        assertEquals("Not found", null, factory.getImplementationFor(String.class));
    }

    @Test
    public void testBuildAction() throws ARIException {
        ARI ari = new ARI();
        ari.setVersion(AriVersion.ARI_0_0_1);

        ActionAsterisk asterisk = ari.asterisk();
        assertTrue("Correct type", asterisk instanceof ActionAsterisk_impl_ari_0_0_1);
    }

    @Test
    public void testCreateUid() throws ARIException {
        ARI ari = new ARI();
        String v = ari.getUID();
        System.out.println("UID: " + v);
        assertTrue("UID created", v.length() > 0);
    }


}
