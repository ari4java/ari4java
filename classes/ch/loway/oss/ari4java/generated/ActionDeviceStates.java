package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;
import ch.loway.oss.ari4java.tools.AriCallback;

public interface ActionDeviceStates {

// void list AriCallback<List<? extends DeviceState>> callback
/**********************************************************
 * 
 *********************************************************/
public void list(AriCallback<List<? extends DeviceState>> callback);



// void get String AriCallback<DeviceState> callback
/**********************************************************
 * 
 *********************************************************/
public void get(String deviceName, AriCallback<DeviceState> callback);



// void delete String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void delete(String deviceName, AriCallback<Void> callback);



// void delete String
/**********************************************************
 * Destroy a device-state controlled by ARI.
 *********************************************************/
public void delete(String deviceName) throws RestException;



// List<? extends DeviceState> list
/**********************************************************
 * List all ARI controlled device states.
 *********************************************************/
public List<? extends DeviceState> list() throws RestException;



// void update String String AriCallback<Void> callback
/**********************************************************
 * 
 *********************************************************/
public void update(String deviceName, String deviceState, AriCallback<Void> callback);



// void update String String
/**********************************************************
 * Change the state of a device controlled by ARI. (Note - implicitly creates the device state).
 *********************************************************/
public void update(String deviceName, String deviceState) throws RestException;



// DeviceState get String
/**********************************************************
 * Retrieve the current state of a device.
 *********************************************************/
public DeviceState get(String deviceName) throws RestException;


}
;
