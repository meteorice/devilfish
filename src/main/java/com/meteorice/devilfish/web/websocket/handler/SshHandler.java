package com.meteorice.devilfish.web.websocket.handler;

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
        logger.debug("sessioinid = {}", session.getId());
        PipedOutputStream writeStream = SshManager.getwriteStream(session.getId(), session);
        if (writeStream != null) {
            try {
                writeStream.write(payload.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//
//        try {
//            Map<String, String> req = JsonUtil.getJsonMapper().readValue(payload, HashMap.class);
//            if (req.containsKey("sessionId")) {
//                String sessionId = String.valueOf(req.get("sessionId"));
//                String cmd = req.get("cmd");
//                PipedOutputStream writeStream = SshManager.getwriteStream(sessionId, session);
//                if (writeStream != null) {
//                    writeStream.write(cmd.getBytes());
//                }
//            } else {
//                session.sendMessage(new TextMessage("reconnection"));
//            }
//        } catch (IOException ex) {
//            logger.error(ex.getMessage());
//        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        WebSocketConfig.addOnlineCount();
        logger.info("xxx用户进入系统。。。");
        logger.info("用户信息:" + session.getAttributes());

        Map<String, Object> map = session.getAttributes();
        for (String key : map.keySet()) {
            logger.info("key:" + key + " and value:" + map.get(key));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable ex) throws Exception {
        session.close(CloseStatus.SERVER_ERROR);
        logger.debug("Error {} WebSocket connection closed.{}", ex.getMessage(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        WebSocketConfig.subOnlineCount();
        logger.debug("WebSocket connection closed. {} ", session);
    }


}
