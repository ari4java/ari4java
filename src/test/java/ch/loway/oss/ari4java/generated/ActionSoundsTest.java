package ch.loway.oss.ari4java.generated;

import ch.loway.oss.ari4java.generated.actions.ActionSounds;
import ch.loway.oss.ari4java.generated.ari_0_0_1.actions.ActionSounds_impl_ari_0_0_1;
import ch.loway.oss.ari4java.generated.models.Sound;
import ch.loway.oss.ari4java.tools.AriCallback;
import ch.loway.oss.ari4java.tools.RestException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author lenz
 */
public class ActionSoundsTest extends BaseActionTest<ActionSounds> {

    @Override
    protected String getJson() {
        return requoteString(""
                + "{"
                + " 'id': 'abcde',"
                + " 'text': 'some text',"
                + " 'formats': ["
                + " { 'language': 'en', 'format': 'wav' },"
                + " { 'language': 'es', 'format': 'wav' },"
                + " { 'language': 'es', 'format': 'gsm' }"
                + " ]"
                + " }");
    }

    @Override
    protected ActionSounds getInstance() {
        return new ActionSounds_impl_ari_0_0_1();
    }

    @Override
    protected void ariActionTest(ActionSounds a) throws RestException {

        Sound s = a.get("abcde").execute();

        assertEquals("abcde", s.getId());
        assertEquals(3, s.getFormats().size());
        assertEquals("es", s.getFormats().get(1).getLanguage());

        a.get("abcde").execute(new AriCallback<Sound>() {

            @Override
            public void onSuccess(Sound result) {
                assertEquals("abcde", result.getId());
                assertEquals(3, result.getFormats().size());
                assertEquals("es", result.getFormats().get(1).getLanguage());
            }

            @Override
            public void onFailure(RestException e) {
                e.printStackTrace();
            }
        });

    }

}
