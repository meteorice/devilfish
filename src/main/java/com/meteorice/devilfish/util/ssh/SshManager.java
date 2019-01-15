package com.meteorice.devilfish.util.ssh;

import com.jcraft.jsch.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SshManager {
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final String HOST = "10.211.55.14";
    private static final int DEFAULT_SSH_PORT = 22;
    /**
     * 执行命令IO管道的池
     */
    private static final ConcurrentMap<String, PipedOutputStream> sshWritePool = new ConcurrentHashMap();

    /**
     * 按天重置的序号
     */
    private static final AtomicInteger tokenIds = new AtomicInteger(0);

    /**
     * 格式化日期
     */
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYYMMdd");

    /**
     * 获取令牌号
     * @return
     */
    public static String getToken() {
        LocalDate now = LocalDate.now();
        String format = now.format(dateTimeFormatter);
        return String.format("%s_%04d",format, tokenIds.addAndGet(1));
    }

    /**
     * 获取当前
     *
     * @param sessionId        会话id
     * @param webSocketSession
     * @return
     */
    public static PipedOutputStream getwriteStream(String sessionId, WebSocketSession webSocketSession) {
        if (sshWritePool.containsKey(sessionId)) {
            return sshWritePool.get(sessionId);
        }
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(USER, HOST, DEFAULT_SSH_PORT);
            session.setPassword(PASSWORD);
            UserInfo userInfo = new User();
            session.setUserInfo(userInfo);
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect();
//            session.connect(30000);   // making a connection with timeout.

            Channel channel = session.openChannel("shell");

            // Enable agent-forwarding.
            ((ChannelShell) channel).setAgentForwarding(true);

            PipedInputStream pipeIn = new PipedInputStream();
            PipedOutputStream pipeOut = new PipedOutputStream(pipeIn);
            sshWritePool.put(sessionId, pipeOut);
            channel.setInputStream(pipeIn);

            PipedInputStream readStream = new PipedInputStream();
            PipedOutputStream writeStream = new PipedOutputStream(readStream);
            channel.setOutputStream(writeStream);

            Thread t2 = new Thread(() -> {
                try {
                    byte[] bytes = new byte[128];
                    int data = readStream.read();
                    while (data != -1) {
                        if (webSocketSession != null && webSocketSession.isOpen()) {
                            webSocketSession.sendMessage(new TextMessage(String.valueOf((char) data)));
                        } else {
                            //删除池中的IO通道
                            sshWritePool.remove(sessionId);
                            return;
                        }
                        data = readStream.read();
                    }

                } catch (IOException e) {
                }
            });
            t2.start();

            channel.connect();
            //channel.connect(30 * 1000);
            return pipeOut;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    static class User implements UserInfo {
        @Override
        public String getPassphrase() {
            System.out.println("getPassphrase");
            return null;
        }

        @Override
        public String getPassword() {
            System.out.println("getPassword");
            return null;
        }

        @Override
        public boolean promptPassword(String s) {
            System.out.println("promptPassword:" + s);
            return false;
        }

        @Override
        public boolean promptPassphrase(String s) {
            System.out.println("promptPassphrase:" + s);
            return false;
        }

        @Override
        public boolean promptYesNo(String s) {
            System.out.println("promptYesNo:" + s);
            return true;//notice here!
        }

        @Override
        public void showMessage(String s) {
            System.out.println("showMessage:" + s);
        }
    }
}
