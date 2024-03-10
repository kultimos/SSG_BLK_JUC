package lock_support;

import java.util.concurrent.TimeUnit;

public class ObjectWaitNotify {
    public static void main(String[] args) {
//        normal();
//        exceptionOne();
        exceptionTwo();
    }

    /**
     * 使用wait notify正常情况
     */
    public static void normal() {
        Object objectLock = new Object();
        new Thread(() -> {
            synchronized (objectLock) {
                System.out.println(Thread.currentThread().getName() + "    ------ come in");
                try {
                    objectLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "  ------被 唤醒");
            }
        },"t1").start();

        try { TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}

        new Thread(() -> {
            synchronized (objectLock) {
                objectLock.notify();
                System.out.println(Thread.currentThread().getName() + "   --- 发出通知");
            }
        },"t2").start();
    }

    /**
     * 使用notify的第一种异常情况,即我们未对代码块加锁的情况下使用wait notify
     */
    public static void exceptionOne() {
        Object objectLock = new Object();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "    ------ come in");
            try {
                objectLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "  ------被 唤醒");
        },"t1").start();

        try { TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}

        new Thread(() -> {
            objectLock.notify();
            System.out.println(Thread.currentThread().getName() + "   --- 发出通知");
        },"t2").start();
    }

    /**
     * 使用notify的第二种异常情况,先notify后wait
     */
    public static void exceptionTwo() {
        Object objectLock = new Object();
        new Thread(() -> {
            try { TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
            synchronized (objectLock) {
                System.out.println(Thread.currentThread().getName() + "    ------ come in");
                try {
                    objectLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "  ------被 唤醒");
            }
        },"t1").start();


        new Thread(() -> {
            synchronized (objectLock) {
                objectLock.notify();
                System.out.println(Thread.currentThread().getName() + "   --- 发出通知");
            }
        },"t2").start();
    }
}
