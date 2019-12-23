package ch.loway.oss.ari4java.generated;

import ch.loway.oss.ari4java.generated.actions.ActionSounds;
import ch.loway.oss.ari4java.generated.ari_0_0_1.actions.ActionSounds_impl_ari_0_0_1;
import ch.loway.oss.ari4java.generated.models.*;
import ch.loway.oss.ari4java.tools.AriCallback;
import ch.loway.oss.ari4java.tools.RestException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author lenz
 */
public class ActionSoundsTest {

    static final String jsonSounds = requoteString(""
            + " {     "
            + " 'id': 'abcde',     "
            + " 'text': 'some text',     "
            + " 'formats': [     "
            + " { 'language': 'en', 'format': 'wav' },     "
            + " { 'language': 'es', 'format': 'wav' },     "
            + " { 'language': 'es', 'format': 'gsm' }     "
            + " ]     "
            + " }     ");

    public ActionSoundsTest() {
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

    public static String requoteString(String s) {
        return s.replace("'", "\"");
    }


    private ActionSounds createWForcedResponse(String response) {
        ActionSounds_impl_ari_0_0_1 a = new ActionSounds_impl_ari_0_0_1();
        a.setForcedResponse(response);

        ActionSounds aa = (ActionSounds) a;
        return aa;
    }


    /**
     * Tries generating a bridge.
     */

    @Test
    public void generateSound() throws RestException {
        ActionSounds aa = createWForcedResponse(jsonSounds);

        Sound s = aa.get("abcde").execute();

        assertEquals("Id", "abcde", s.getId());
        assertEquals("Formats", 3, s.getFormats().size());
        assertEquals("Language of format #2", "es", s.getFormats().get(1).getLanguage());

        aa.get("abcde").execute(new AriCallback<Sound>() {

            @Override
            public void onSuccess(Sound result) {
                assertEquals("Id", "abcde", result.getId());
                assertEquals("Formats", 3, result.getFormats().size());
                assertEquals("Language of format #2", "es", result.getFormats().get(1).getLanguage());
            }

            @Override
            public void onFailure(RestException e) {
                e.printStackTrace();
            }
        });

    }


}
