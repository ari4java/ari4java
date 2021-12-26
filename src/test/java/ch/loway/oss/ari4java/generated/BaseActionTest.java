package ch.loway.oss.ari4java.generated;

import ch.loway.oss.ari4java.tools.BaseAriAction;
import ch.loway.oss.ari4java.tools.RestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author lenz
 */
public abstract class BaseActionTest<T> {

    abstract protected String getJson();
    abstract protected T getInstance();
    abstract protected void ariActionTest(T a) throws RestException;

      /**
     * Strings in a JSON object need the double quotes.
     * Unfortunately using double quotes in Java is a PITA.
     * So...
     *
     * @param s string
     * @return Translating 's to "s
     */
    protected static String requoteString(String s) {
        return s.replace("'", "\"");
    }

    protected T createWForcedResponse() {
        T a = getInstance();
        assertTrue(a instanceof BaseAriAction);
        ((BaseAriAction)a).setForcedResponse(getJson());
        return a;
    }

    @Test
    public void testAriAction() throws RestException {
        T a = createWForcedResponse();
        ariActionTest(a);
    }

}
