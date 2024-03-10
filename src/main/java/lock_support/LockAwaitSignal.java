package lock_support;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockAwaitSignal {
    public static void main(String[] args) {
//        normal();
//        exceptionOne();
        exceptionTwo();
    }


    public static void normal() {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " ---- come in");
                condition.await();
                System.out.println(Thread.currentThread().getName() + " ---- 被唤醒");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        },"t1").start();

        try { TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}

        new Thread(() -> {
            lock.lock();
            try {
                condition.signal();
                System.out.println(Thread.currentThread().getName() + "--- 发出通知");
            } finally {
                System.out.println("先完成锁的释放,那边才能被唤醒 对吧");
                lock.unlock();
//                try { TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
                System.out.println("这行代码");
            };
        },"t2").start();
    }

    /**
     * 未持有锁时,调用await和signal,会抛出异常
     */
    public static void exceptionOne() {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();
        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " ---- come in");
                condition.await();
                System.out.println(Thread.currentThread().getName() + " ---- 被唤醒");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        },"t1").start();

        try { TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}

        new Thread(() -> {
            try {
                condition.signal();
                System.out.println(Thread.currentThread().getName() + "--- 发出通知");
            } finally {
            };
        },"t2").start();
    }

    /**
     * 先signal,后wait,则wait线程会永久进入阻塞状态
     */
    public static void exceptionTwo() {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();
        new Thread(() -> {
            try { TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
            reentrantLock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " ---- come in");
                condition.await();
                System.out.println(Thread.currentThread().getName() + " ---- 被唤醒");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                reentrantLock.unlock();
            }
        },"t1").start();


        new Thread(() -> {
            try {
                reentrantLock.lock();
                condition.signal();
                System.out.println(Thread.currentThread().getName() + "--- 发出通知");
            } finally {
                reentrantLock.unlock();
            };
        },"t2").start();
    }
}
