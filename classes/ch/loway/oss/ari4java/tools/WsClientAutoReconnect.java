package ch.loway.oss.ari4java.tools;

/**
 * Interface to pluggable WebSocket reconnect implementation
 * 
 * @author grahambrown11
 *
 */
public interface WsClientAutoReconnect {
	void reconnectWs();
}