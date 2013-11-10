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
import com.fasterxml.jackson.core.type.TypeReference;
import ch.loway.oss.ari4java.generated.ari_0_0_1.models.*;

public class ActionSounds_impl_ari_0_0_1 extends BaseAriAction  implements ActionSounds {
/**********************************************************
 * Sounds
 * 
 * List all sounds.
 *********************************************************/
public List<? extends Sound> getSounds(String lang, String format) throws RestException {
String url = "/sounds";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
lParamQuery.add( BaseAriAction.HttpParam.build( "lang", lang) );
lParamQuery.add( BaseAriAction.HttpParam.build( "format", format) );
String json = httpAction( url, "GET", lParamQuery, lParamForm, lE);
return (List<? extends Sound>) deserializeJson( json, new TypeReference<List<Sound_impl_ari_0_0_1>>() {} ); 
}

/**********************************************************
 * Individual sound
 * 
 * Get a sound's details.
 *********************************************************/
public Sound getStoredSound(String soundId) throws RestException {
String url = "/sounds/" + soundId + "";
List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();
List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
String json = httpAction( url, "GET", lParamQuery, lParamForm, lE);
return (Sound) deserializeJson( json, Sound_impl_ari_0_0_1.class ); 
}

};

