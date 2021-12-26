package ch.loway.oss.ari4java;

import ch.loway.oss.ari4java.tools.http.NettyHttpClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AriFactoryTest {

    static NettyHttpClient httpClient = mock(NettyHttpClient.class);

    private static void setHttpClient() {
        AriFactory.nettyHttpClient = httpClient;
    }

    @Test
    public void testDetectAriVersion() throws Exception {
        try {
            AriFactory.nettyHttp("test://local:8088/", "", "", AriVersion.IM_FEELING_LUCKY);
            fail("Expected an exception");
        } catch (Exception e) {
            assertEquals("Unsupported protocol: test", e.getMessage());
        }
        setHttpClient();
        when(httpClient.httpActionSync(
                eq("/api-docs/resources.json"), eq("GET"),
                eq(null), eq(null), eq(null))).thenReturn("{}");
        try {
            AriFactory.nettyHttp("http://localhost", "", "", AriVersion.IM_FEELING_LUCKY);
            fail("Expected exception");
        } catch (Exception e) {
            assertEquals("Could find apiVersion", e.getMessage());
        }
        reset(httpClient);
        when(httpClient.httpActionSync(
                eq("/api-docs/resources.json"), eq("GET"),
                eq(null), eq(null), eq(null))).thenReturn("{\"apiVersion\": \"0.0.1\"}");
        AriVersion ver = AriFactory.nettyHttp("http://localhost", "", "", AriVersion.IM_FEELING_LUCKY).getVersion();
        assertEquals(AriVersion.ARI_0_0_1, ver);
    }

    @AfterAll
    public static void end() {
        AriFactory.nettyHttpClient = null;
    }

}
