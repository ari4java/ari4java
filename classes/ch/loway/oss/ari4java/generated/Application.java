package ch.loway.oss.ari4java.generated;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;

/*** ====================================================
 * Interface for ch.loway.oss.ari4java.generated
 * Please do not edit.
 * ================================================= */
public interface Application {

// List<String> getChannel_ids
    /*** ====================================================
     * Id's for channels subscribed to.
     * ================================================= */
    public void setChannel_ids(List<String> val);

// List<String> getBridge_ids
    /*** ====================================================
     * Id's for bridges subscribed to.
     * ================================================= */
    public void setBridge_ids(List<String> val);

// List<String> getEndpoint_ids
    /*** ====================================================
     * {tech}/{resource} for endpoints subscribed to.
     * ================================================= */
    public void setEndpoint_ids(List<String> val);

// String getName
    /*** ====================================================
     * Name of this application
     * ================================================= */
    public void setName(String val);
};
