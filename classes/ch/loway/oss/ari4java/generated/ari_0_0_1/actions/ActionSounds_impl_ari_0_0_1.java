package ch.loway.oss.ari4java.generated.ari_0_0_1.actions;
import ch.loway.oss.ari4java.generated.*;
import ch.loway.oss.ari4java.generated.ari_0_0_1.models.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.*;
public class ActionSounds_impl_ari_0_0_1 extends BaseAriAction  implements ActionSounds {
/** =====================================================
 * Sounds
 * 
 * List all sounds.
 * ====================================================== */
public List<Sound> getSounds(String lang, String format) throws RestException {
String url = "/sounds";
List<BaseAriAction.HttpParam> lP = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lP.add( BaseAriAction.HttpParam.build( "lang", lang) );
lP.add( BaseAriAction.HttpParam.build( "format", format) );
String json = httpAction( url, "GET", lP, lE);
return (List<Sound>) deserializeJson( json, List.class); 
}

/** =====================================================
 * Individual sound
 * 
 * Get a sound's details.
 * ====================================================== */
public Sound getStoredSound(String soundId) throws RestException {
String url = "/sounds/" + soundId + "";
List<BaseAriAction.HttpParam> lP = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
String json = httpAction( url, "GET", lP, lE);
return (Sound) deserializeJson( json, Sound.class); 
}

};

