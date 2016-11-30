package ch.loway.oss.ari4java.tools.http;

import ch.loway.oss.ari4java.tools.HttpResponseHandler;
import ch.loway.oss.ari4java.tools.WsClientAutoReconnect;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

/**
 * NettyWSClientHandler handles the transactions with the remote
 * WebSocket, forwarding to the client HttpResponseHandler interface.
 *
 * @author mwalton
 *
 */
@ChannelHandler.Sharable
public class NettyWSClientHandler extends NettyHttpClientHandler {
    
    final WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture;
    final HttpResponseHandler wsCallback;
    private WsClientAutoReconnect wsClient = null;
    private boolean shuttingDown = false;

    public NettyWSClientHandler(WebSocketClientHandshaker handshaker, HttpResponseHandler wsCallback, WsClientAutoReconnect wsClient) {
        this(handshaker, wsCallback);
        this.wsClient = wsClient;
    }

    public NettyWSClientHandler(WebSocketClientHandshaker handshaker, HttpResponseHandler wsCallback) {
        this.handshaker = handshaker;
        this.wsCallback = wsCallback;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (!shuttingDown) {
            if (this.wsClient != null) {
                wsClient.reconnectWs();
            } else {
                wsCallback.onDisconnect();
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        
        if (!handshaker.isHandshakeComplete()) {
            handshaker.finishHandshake(ch, (FullHttpResponse) msg);
            handshakeFuture.setSuccess();
            wsCallback.onChReadyToWrite();
            return;
        }
        
        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            String error = "Unexpected FullHttpResponse (getStatus=" + response.getStatus() + ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')';
            System.err.println(error);
            throw new Exception(error);
        }

        // call this so we can set the last received time
        wsCallback.onResponseReceived();
        
        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            responseText = textFrame.text();
            wsCallback.onSuccess(textFrame.text());
        } else if (frame instanceof CloseWebSocketFrame) {
            ch.close();
            if (!shuttingDown) {
                if (this.wsClient != null) {
                    wsClient.reconnectWs();
                } else {
                    wsCallback.onDisconnect();
                }
            }
        }
        
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (!shuttingDown)
            return;
        cause.printStackTrace();
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
        wsCallback.onFailure(cause);
    }

    public boolean isShuttingDown() {
        return shuttingDown;
    }

    public void setShuttingDown(boolean shuttingDown) {
        this.shuttingDown = shuttingDown;
    }
}

