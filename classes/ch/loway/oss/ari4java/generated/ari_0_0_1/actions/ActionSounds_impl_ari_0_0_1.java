package ch.loway.oss.ari4java.generated.ari_0_0_1.actions;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import ch.loway.oss.ari4java.generated.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.BaseAriAction;
import ch.loway.oss.ari4java.tools.RestException;
import ch.loway.oss.ari4java.tools.AriCallback;
import com.fasterxml.jackson.core.type.TypeReference;
import ch.loway.oss.ari4java.generated.ari_0_0_1.models.*;

public class ActionSounds_impl_ari_0_0_1 extends BaseAriAction  implements ActionSounds {
/**********************************************************
 * Sounds
 * 
 * List all sounds.
 *********************************************************/
private void buildGetSounds(String lang, String format) {
reset();
url = "/sounds";
lParamQuery.add( BaseAriAction.HttpParam.build( "lang", lang) );
lParamQuery.add( BaseAriAction.HttpParam.build( "format", format) );
}

@Override
public List<? extends Sound> getSounds(String lang, String format) throws RestException {
buildGetSounds(lang, format);
String json = httpActionSync();
return deserializeJson( json, new TypeReference<List<Sound_impl_ari_0_0_1>>() {} ); 
}

@Override
public void getSounds(String lang, String format, AriCallback<List<? extends Sound>> callback) {
buildGetSounds(lang, format);
httpActionAsync(callback, new TypeReference<List<Sound_impl_ari_0_0_1>>() {});
}

/**********************************************************
 * Individual sound
 * 
 * Get a sound's details.
 *********************************************************/
private void buildGetStoredSound(String soundId) {
reset();
url = "/sounds/" + soundId + "";
}

@Override
public Sound getStoredSound(String soundId) throws RestException {
buildGetStoredSound(soundId);
String json = httpActionSync();
return deserializeJson( json, Sound_impl_ari_0_0_1.class ); 
}

@Override
public void getStoredSound(String soundId, AriCallback<Sound> callback) {
buildGetStoredSound(soundId);
httpActionAsync(callback, Sound_impl_ari_0_0_1.class);
}

};

