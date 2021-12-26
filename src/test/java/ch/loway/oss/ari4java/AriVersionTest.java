package ch.loway.oss.ari4java;

import ch.loway.oss.ari4java.generated.ari_0_0_1.AriBuilder_impl_ari_0_0_1;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AriVersionTest {

    @Test
    public void builder() {
        try {
            AriVersion.IM_FEELING_LUCKY.builder();
            fail("Expecting an exception");
        } catch (Exception e) {
            assertEquals("This version has no builder. Library error for IM_FEELING_LUCKY", e.getMessage());
        }
        assertEquals(AriBuilder_impl_ari_0_0_1.class.toString(), AriVersion.ARI_0_0_1.builder().getClass().toString());
    }

    @Test
    public void version() {
        assertEquals("0.0.1", AriVersion.ARI_0_0_1.version());
    }

    @Test
    public void fromVersionString() throws Exception {
        assertEquals(AriVersion.ARI_0_0_1, AriVersion.fromVersionString("0.0.1"));
        try {
            AriVersion.fromVersionString("0");
            fail("Expecting an exception");
        } catch (Exception e) {
            assertEquals("Unknown ARI Version object for 0", e.getMessage());
        }
    }
}
