package com.beasttamer.redis.util.threadutil;

import com.beasttamer.redis.util.RedisLockUtil;
import redis.clients.jedis.Jedis;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author a small asshole
 * @version 1.0
 * @description TODO
 * @date in 17:10 2020/1/13
 * @since TODO
 */
public class MyThread extends Thread {
    public static final CountDownLatch cdOrder = new CountDownLatch(1);


    @Override
    public void run() {
        try{
            System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + "等待抢锁");
            cdOrder.await();
            Thread.sleep(2000);
            System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + "(自定义)正在请求加锁");
            boolean myLock = RedisLockUtil.tryGetDistributedLock(new Jedis("127.0.0.1", 6379), "myLock", Thread.currentThread().getName(), 1000);
            if (myLock) {
                System.err.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + "抢到锁");
                try {
                    System.out.println("抢到锁的线程沉睡2s");
                    //获得锁期间执行业务操作
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    //业务结束后释放锁
                    System.err.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + "######释放锁####:"
                            + RedisLockUtil.releaseDistributedLock(new Jedis("127.0.0.1", 6379), "myLock", Thread.currentThread().getName()));
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //未获取到锁的递归循环调用
            run();
        }

    }
}
