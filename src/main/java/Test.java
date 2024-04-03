import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
    public  ThreadLocal threadLocal1 = ThreadLocal.withInitial(() -> 0);
    public  ThreadLocal threadLocal2 = ThreadLocal.withInitial(() -> 0);

    public static void main(String[] args) {
        Test test = new Test();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                test.threadLocal1.set("100");
                test.threadLocal2.set("200");
                try {
                    Thread.sleep(50000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "t1");
        thread.start();
    }
}
