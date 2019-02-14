package com.meteorice.devilfish.web.websocket.handler;

import com.jcraft.jsch.JSchException;
import com.meteorice.devilfish.util.StrUtils;
import com.meteorice.devilfish.util.ssh.SshManager;
import com.meteorice.devilfish.web.config.WebSocketConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.net.URI;
import java.util.Map;

/**
 * The type Ssh handler.
 */
public class SshHandler extends TextWebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger(SshHandler.class);

    /**
     * 执行ssh命令
     *
     * @param session websocket 会话
     * @param message 入参
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        URI uri = session.getUri();
        Map<String, String> params = StrUtils.analysisUrl(uri.getQuery());
        logger.debug("websocket token = {}", params.get("token"));
        PipedOutputStream writeStream = null;
        try {
            writeStream = SshManager.getwriteStream(params.get("token"), session, params.get("ip"));
            writeStream.write(payload.getBytes());
        } catch (JSchException e) {
            try {
                session.sendMessage(new TextMessage(e.getMessage() + "\n"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        WebSocketConfig.addOnlineCount();
        logger.info("xxx用户进入系统。。。");
        logger.info("用户信息:" + session.getAttributes());

        Map<String, Object> map = session.getAttributes();
        for (String key : map.keySet()) {
            logger.info("key:" + key + " and value:" + map.get(key));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable ex) {
        try {
            session.close(CloseStatus.SERVER_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("Error {} WebSocket connection closed.{}", ex.getMessage(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        WebSocketConfig.subOnlineCount();
        URI uri = session.getUri();
        Map<String, String> params = StrUtils.analysisUrl(uri.getQuery());
        SshManager.clearPipeline(params.get("token"));
        logger.debug("WebSocket connection closed. {} ", session);
    }


}
