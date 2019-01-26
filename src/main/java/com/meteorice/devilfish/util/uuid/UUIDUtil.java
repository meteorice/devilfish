package com.meteorice.devilfish.util.uuid;

import com.meteorice.devilfish.util.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class UUIDUtil {

    private static Logger logger = LoggerFactory.getLogger(UUIDUtil.class);
    /**
     * 格式化日期
     */
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYYMMdd");

    /**
     * 按天重置的序号
     */
    private static Integer tokenId = 0;

    /**
     * 存放token
     */
    private static final BlockingQueue<String> queue = new ArrayBlockingQueue(2048);

    static {
        ThreadUtil.permanentTask(new TokenProducer(queue), "tokenQueue");
    }

    /**
     * 获取uuid
     *
     * @return
     */
    public static String getUUID() {
        return String.valueOf(UUID.randomUUID()).replace("-", "");
    }

    /**
     * 获取token
     *
     * @return
     * @throws InterruptedException
     */
    public static String getToken() throws InterruptedException {
        String id = queue.poll(15, TimeUnit.SECONDS);
        LocalDate now = LocalDate.now();
        String date = now.format(dateTimeFormatter);
        if (!id.startsWith(date)) {
            synchronized (queue) {
                //清零
                tokenId = 0;
                queue.clear();
            }
        }
        return id;
    }

    /**
     * 生产令牌
     */
    public static class TokenProducer implements Runnable {

        protected BlockingQueue queue = null;

        public TokenProducer(BlockingQueue queue) {
            this.queue = queue;
        }

        public void run() {
            LocalDate now = LocalDate.now();
            String date = now.format(dateTimeFormatter);
            long start = new Date().getTime();
            try {
                while (true) {
                    synchronized (queue) {
                        queue.put(String.format("%s_%06d", date, tokenId++));
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.debug("loaded queueToken spend : {}", new Date().getTime() - start);
        }
    }
}
