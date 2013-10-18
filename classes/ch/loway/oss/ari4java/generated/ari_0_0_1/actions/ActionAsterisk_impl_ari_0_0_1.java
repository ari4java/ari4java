package ch.loway.oss.ari4java.generated.ari_0_0_1.actions;

import ch.loway.oss.ari4java.generated.*;
import ch.loway.oss.ari4java.generated.ari_0_0_1.models.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.*;

public class ActionAsterisk_impl_ari_0_0_1 extends BaseAriAction implements ActionAsterisk {

    /** =====================================================
     * Asterisk system information (similar to core show settings)
     *
     * Gets Asterisk system information.
     * ====================================================== */
    public AsteriskInfo getAsteriskInfo(String only) throws RestException {
        String url = "/asterisk/info";
        List<BaseAriAction.HttpParam> lP = new ArrayList<BaseAriAction.HttpParam>();
        List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
        lP.add(BaseAriAction.HttpParam.build("only", only));
        String json = httpAction(url, "GET", lP, lE);
        return (AsteriskInfo) deserializeJson(json, AsteriskInfo.class);
    }

    /** =====================================================
     * Global variables
     *
     * Get the value of a global variable.
     * ====================================================== */
    public Variable getGlobalVar(String variable) throws RestException {
        String url = "/asterisk/variable";
        List<BaseAriAction.HttpParam> lP = new ArrayList<BaseAriAction.HttpParam>();
        List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
        lP.add(BaseAriAction.HttpParam.build("variable", variable));
        lE.add(BaseAriAction.HttpResponse.build(400, "Missing variable parameter."));
        String json = httpAction(url, "GET", lP, lE);
        return (Variable) deserializeJson(json, Variable.class);
    }

    /** =====================================================
     * Global variables
     *
     * Set the value of a global variable.
     * ====================================================== */
    public void setGlobalVar(String variable, String value) throws RestException {
        String url = "/asterisk/variable";
        List<BaseAriAction.HttpParam> lP = new ArrayList<BaseAriAction.HttpParam>();
        List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();
        lP.add(BaseAriAction.HttpParam.build("variable", variable));
        lP.add(BaseAriAction.HttpParam.build("value", value));
        lE.add(BaseAriAction.HttpResponse.build(400, "Missing variable parameter."));
        String json = httpAction(url, "POST", lP, lE);
    }
};
