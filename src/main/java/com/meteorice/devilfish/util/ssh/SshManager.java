package com.meteorice.devilfish.util.ssh;

import com.jcraft.jsch.*;
import com.meteorice.devilfish.pojo.Host;
import com.meteorice.devilfish.pojo.HostConfig;
import com.meteorice.devilfish.service.HostService;
import com.meteorice.devilfish.util.spring.SpringContextHolder;
import com.meteorice.devilfish.util.uuid.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SshManager {

    private static Logger logger = LoggerFactory.getLogger(SshManager.class);

    private static final int DEFAULT_SSH_PORT = 22;
    /**
     * 执行命令IO管道的池
     */
    private static final ConcurrentMap<String, Pipeline> sshWritePool = new ConcurrentHashMap();

    /**
     * 取令牌
     *
     * @return
     */
    public static String getToken() throws InterruptedException {
        return UUIDUtil.getToken();
    }


    /**
     * Gets ssh session.
     *
     * @param userName the user name
     * @param passwd   the passwd
     * @param host     the host
     * @param port     the port
     * @return the ssh session
     */
    public static Session getSshSession(String userName, String passwd, String host, int port, int timeout) throws JSchException {
        JSch jsch = new JSch();
        Session session = null;
        session = jsch.getSession(userName, host, port);
        session.setPassword(passwd);
        UserInfo userInfo = new User();
        session.setUserInfo(userInfo);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(timeout);

        return session;
    }

    /**
     * 获取当前
     *
     * @param sessionId        会话id
     * @param webSocketSession
     * @param ip
     * @return
     */
    public static PipedOutputStream getwriteStream(String sessionId, WebSocketSession webSocketSession, String ip) throws JSchException, IOException {
        HostService hostService = SpringContextHolder.getBean(HostService.class);
        if (sshWritePool.containsKey(sessionId)) {
            Pipeline pipeline = sshWritePool.get(sessionId);
            if (pipeline.getChannel().isClosed()) {
                clearPipeline(sessionId);
                throw new JSchException("channel is closed , confirm host is online please");
            }
            if (pipeline.getWs() != null && !pipeline.getWs().isOpen()) {
                createReadThread(sessionId, webSocketSession, pipeline.getChannel());
            }
            return pipeline.getWrite();
        }
        HostConfig hostConfig = hostService.getHostConfig(ip);
        Host host = hostService.getHost(ip);
        Session session = getSshSession(hostConfig.getLoginname(), hostConfig.getLoginpwd(), ip, host.getPort() != null ? host.getPort() : DEFAULT_SSH_PORT, hostConfig.getTimeout());

//            session.connect(30000);   // making a connection with timeout.

        Channel channel = session.openChannel("shell");

        // Enable agent-forwarding.
        ((ChannelShell) channel).setAgentForwarding(true);

        PipedInputStream pipeIn = new PipedInputStream();
        PipedOutputStream pipeOut = new PipedOutputStream(pipeIn);
        sshWritePool.put(sessionId, new Pipeline(webSocketSession, pipeOut, channel));
        channel.setInputStream(pipeIn);

        createReadThread(sessionId, webSocketSession, channel);

        channel.connect(hostConfig.getTimeout());
        //channel.connect(30 * 1000);
        return pipeOut;
    }

    /**
     * 清除池中的会话
     * @param sessionId
     */
    public static void clearPipeline(String sessionId) {
        logger.info("clear pipeline {} start", sessionId);
        Pipeline pipeline = sshWritePool.remove(sessionId);
        Thread thread = pipeline.getThread();
        Channel channel = pipeline.getChannel();
        PipedOutputStream write = pipeline.getWrite();
        if (thread.isAlive() && !thread.isInterrupted()) {
            thread.interrupt();
        }
        if (channel != null & channel.isConnected()) {
            channel.disconnect();
        }
        if (write != null) {
            try {
                write.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info("clear pipeline {} end", sessionId);
    }


    /**
     * 建立channel和websocket的通道
     *
     * @param sessionId
     * @param webSocketSession
     * @param channel
     */
    public static void createReadThread(String sessionId, WebSocketSession webSocketSession, Channel channel) {

        try {
            Pipeline pipeline = sshWritePool.get(sessionId);
//            pipeline.getWrite().write("\n".getBytes());
            PipedInputStream readStream = new PipedInputStream();
            PipedOutputStream writeStream = new PipedOutputStream(readStream);
            channel.setOutputStream(writeStream);

            Thread last = pipeline.getThread();
            if (last != null && last.isAlive() && !last.isInterrupted()) {
                String name = last.getName();
                //如果上次的线程还在就要中断它
                logger.debug("interrupt thread : ({})", last);
                last.interrupt();
            }

            Thread t2 = new Thread(() -> {
                try {
                    //byte[] bytes = new byte[128];
                    int data = readStream.read();
                    while (data != -1) {
                        if (webSocketSession != null && webSocketSession.isOpen()) {
                            webSocketSession.sendMessage(new TextMessage(String.valueOf((char) data)));
                        } else {
                            //删除池中的IO通道
//                            sshWritePool.remove(sessionId);
                            logger.warn("tokenId : {} closed", sessionId);
                            return;
                        }
                        data = readStream.read();
                    }

                } catch (IOException e) {
                    if (e instanceof InterruptedIOException) {
                        logger.info("interrupt thread : {} probably refresh page", e.getStackTrace());
                    } else {
                        e.printStackTrace();
                    }
                }
//                catch (JSchException e) {
//                    e.printStackTrace();
//                }
            }, String.format("WS_%s_%s", sessionId, webSocketSession.getId()));
            t2.start();
            pipeline.setThread(t2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Pipeline {
        private WebSocketSession ws;
        private PipedOutputStream write;
        private Channel channel;
        //在后台传输内容到websocket的线程只能有一个
        private Thread thread = null;

        public Pipeline(WebSocketSession ws, PipedOutputStream write, Channel channel) {
            this.ws = ws;
            this.write = write;
            this.channel = channel;
        }

        public Thread getThread() {
            return thread;
        }

        public void setThread(Thread thread) {
            this.thread = thread;
        }

        public WebSocketSession getWs() {
            return ws;
        }

        public void setWs(WebSocketSession ws) {
            this.ws = ws;
        }

        public PipedOutputStream getWrite() {
            return write;
        }

        public void setWrite(PipedOutputStream write) {
            this.write = write;
        }

        public Channel getChannel() {
            return channel;
        }

        public void setChannel(Channel channel) {
            this.channel = channel;
        }
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
