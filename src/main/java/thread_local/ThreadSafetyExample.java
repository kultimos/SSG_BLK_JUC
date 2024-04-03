package thread_local;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSafetyExample {
    private static ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 0);
    private static Integer sharedCounter = 0;

    public static void main(String[] args) {
        // 创建并行线程
        Thread thread1 = new Thread(() -> {
            // 修改线程本地变量
            int localValue = threadLocal.get();
            threadLocal.set(localValue + 1);
            System.out.println("Thread 1 - ThreadLocal Value: " + threadLocal.get());

            // 修改共享变量
            sharedCounter = 100;
            System.out.println("Thread 1 - 立刻查看值的修改 " + sharedCounter);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Thread 1 - 阻塞后查看值的修改 " + sharedCounter);
        });

        Thread thread2 = new Thread(() -> {
            // 修改线程本地变量
            int localValue = threadLocal.get();
            threadLocal.set(localValue + 1);
            System.out.println("Thread 2 - ThreadLocal Value: " + threadLocal.get());

            // 修改共享变量
            sharedCounter = 50;
            System.out.println("Thread 2 - Shared Counter: " + sharedCounter);
        });

        // 启动线程
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 打印最终结果
        System.out.println("Final ThreadLocal Value: " + threadLocal.get());
        System.out.println("Final Shared Counter: " + sharedCounter);
    }
}
