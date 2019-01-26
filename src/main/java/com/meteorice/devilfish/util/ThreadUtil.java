package com.meteorice.devilfish.util;

public class ThreadUtil {

    /**
     * 常住线程组
     */
    private static ThreadGroup permanentGroup = new ThreadGroup("PERMANENT");

    /**
     * 常住任务
     * @param task
     */
    public static void permanentTask(Runnable task, String name) {
        Thread thread = new Thread(permanentGroup, task, name);
        thread.start();
    }
}
