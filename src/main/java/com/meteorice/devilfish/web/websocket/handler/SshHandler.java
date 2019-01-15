package com.meteorice.devilfish.web.websocket.handler;

import com.meteorice.devilfish.util.json.JsonUtil;
import com.meteorice.devilfish.util.ssh.SshManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.util.HashMap;
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
        try {
            String payload = message.getPayload();
            Map<String, String> req = JsonUtil.getJsonMapper().readValue(payload, HashMap.class);
            if (req.containsKey("sessionId")) {
                String sessionId = req.get("sessionId");
                String cmd = req.get("cmd");
                PipedOutputStream writeStream = SshManager.getwriteStream(sessionId, session);
                if (writeStream != null) {
                    writeStream.write(cmd.getBytes());
                }
            } else {
                session.sendMessage(new TextMessage("reconnection"));
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }
}