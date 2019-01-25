package com.meteorice.devilfish.web.config;

import com.meteorice.devilfish.web.interceptor.WSInterceptor;
import com.meteorice.devilfish.web.websocket.handler.SshHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.concurrent.atomic.LongAdder;

/**
 * The type Web socket config.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    /**
     * 总共websocket数量
     */
    private static LongAdder count = new LongAdder();

    /**
     * 在线数量
     *
     * @return
     */
    public static int getOnlineCount() {
        return Long.valueOf(count.sum()).intValue();
    }

    public static void addOnlineCount() {
        count.increment();
    }

    public static void subOnlineCount() {
        count.decrement();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(connectHandler(), "sshconnect/token={ip}")
                .setAllowedOrigins("*")
                .addInterceptors(new WSInterceptor());
    }


    @Bean
    public WebSocketHandler connectHandler() {
        return new SshHandler();
    }
}
