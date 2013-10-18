package ch.loway.oss.ari4java.generated;

import java.util.Date;
import java.util.List;

/** ----------------------------------------------------
  * Notification that another WebSocket has taken over for an application.

An application may only be subscribed to by a single WebSocket at a time. If multiple WebSockets attempt to subscribe to the same application, the newer WebSocket wins, and the older one receives this event.
  * Defined in file: events.json
  * ------------------------------------------------- */

public class ApplicationReplaced extends Event implements java.io.Serializable {
}

