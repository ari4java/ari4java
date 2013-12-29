
package ch.loway.oss.ari4java.connector;

import ch.loway.oss.ari4java.generated.ActionApplications;
import ch.loway.oss.ari4java.generated.ActionAsterisk;
import ch.loway.oss.ari4java.generated.AsteriskInfo;
import ch.loway.oss.ari4java.tools.BaseAriAction.HttpParam;
import ch.loway.oss.ari4java.tools.BaseAriAction.HttpResponse;
import ch.loway.oss.ari4java.tools.RestException;
import java.util.List;

/**
 * The ARI connector
 * 
 * @author lenz
 */
public abstract class AriConnector {

    ConnectionCredentials credentials = null;

    /**
     * Sets the connection credentials.
     * 
     * @param creds
     */

    public void setCredentials(ConnectionCredentials creds) {
        credentials = creds;
    }

    /**
     * Opens up a connection.
     */

    public abstract void openConnection();

    /**
     * Closes a connection.
     */

    public abstract void closeConnection();


    ActionApplications applications = null;
    ActionAsterisk asterisk = null;

    public ActionApplications applications() {
        return applications;
    }

    public ActionAsterisk asterisk() {
        return asterisk;
    }



    public void setup( ActionApplications apps, ActionAsterisk astinfo ) {
        applications = apps;
        asterisk = astinfo;
    }

    /**
     *
     * @param urlFragment
     * @param method
     * @param lParamQuery
     * @param lParamForm
     * @param lErrors
     * @return
     * @throws RestException
     */

    public abstract String performHttp( String urlFragment, String method, List<HttpParam> parametersQuery, List<HttpParam> parametersForm, List<HttpResponse> lErrors) throws RestException;

}

// $Log$
//
