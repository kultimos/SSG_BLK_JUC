package lock_support;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

public class TheParkUnPark {
    public static void main(String[] args) {
//        normalMethod();
        exceptionOnly();
    }


    /**
     * 这里我们只有一种异常,因为我们甚至不需要持有锁,就可以实现线程的阻塞和唤醒
     * 这里我们设置的异常方式是: 先unPark 后park 观察线程状态
     * 结果很明显,因为是通行证机制,所以即使先unPark,也会讲通行证保留一份,下次park则可以直接通过,无需阻塞;
     * 但是需要注意,一个线程最多智能拥有一张通行证,即如果我们对一个线程调用两次unPark,线程依然只会拥有一张通行证
     */
    public static void exceptionOnly() {
        Thread t1 = new Thread(() -> {
            try { TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println(Thread.currentThread().getName() + "    ------ come in");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "    ------ 被唤醒");
//            System.out.println("第二次被阻塞能否直接通过？");
//            LockSupport.park();
//            System.out.println("of course not");
        },"t1");
        t1.start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "   --- 发出通知");
            LockSupport.unpark(t1);
//            LockSupport.unpark(t1);
        },"t2").start();
    }


    /**
     * 正常情况,先park,再由另一个线程去唤醒
     */
    public static void normalMethod() {
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "    ------ come in");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "    ------ 被唤醒");
        },"t1");
        t1.start();

        try { TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "   --- 发出通知");
            LockSupport.unpark(t1);
        },"t2").start();
    }
}
