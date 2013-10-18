package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;

public interface ChannelDtmfReceived {

// String getDigit
/** =====================================================
 * DTMF digit received (0-9, A-E, # or *)
 * ====================================================== */
 public String getDigit();



// void setDuration_ms int
/** =====================================================
 * Number of milliseconds DTMF was received
 * ====================================================== */
 public void setDuration_ms(int val );



// int getDuration_ms
/** =====================================================
 * Number of milliseconds DTMF was received
 * ====================================================== */
 public int getDuration_ms();



// Channel getChannel
/** =====================================================
 * The channel on which DTMF was received
 * ====================================================== */
 public Channel getChannel();



// void setDigit String
/** =====================================================
 * DTMF digit received (0-9, A-E, # or *)
 * ====================================================== */
 public void setDigit(String val );



// void setChannel Channel
/** =====================================================
 * The channel on which DTMF was received
 * ====================================================== */
 public void setChannel(Channel val );


}
;
