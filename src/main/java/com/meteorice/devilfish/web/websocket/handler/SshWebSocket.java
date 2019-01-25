package com.meteorice.devilfish.web.websocket.handler;

import com.meteorice.devilfish.web.config.WebSocketConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws/ssh/{ip}/{token}")
@Component
public class SshWebSocket {

    private static Logger logger = LoggerFactory.getLogger(SshWebSocket.class);


    @OnOpen
    public void onOpen(@PathParam("ip") String ip, @PathParam("token") String token, Session session) {
        logger.debug("ip:{},token:{}", ip, token);
        WebSocketConfig.addOnlineCount();
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info(message);
        logger.debug("来自客户端的消息:{}", message);
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.debug("发生错误:{}", error.getMessage());
        error.printStackTrace();
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        WebSocketConfig.subOnlineCount();
        logger.debug("有一连接关闭");
    }
}
