package com.beasttamer.redis.util.threadutil;

import java.util.concurrent.*;

/**
 * @author a small asshole
 * @version 1.0
 * @description TODO
 * @date in 17:12 2020/1/13
 * @since TODO
 */
public class TestCachedThreadPool {
    public static void main(String[] args) throws InterruptedException {
        //ThreadPoolExecutor测试方法
        System.out.println("ThreadPoolExecutor线程池");
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(5, 10, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(10));
        for (int i = 0; i < 5; i++) {
            Thread t1 = new MyThread();
            tpe.execute(t1);
        }
        Thread.sleep(2000);
        MyThread.cdOrder.countDown();

        tpe.shutdown();//关闭线程池

    }
}
