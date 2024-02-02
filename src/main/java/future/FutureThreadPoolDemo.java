package future;

import java.util.concurrent.*;
/**
 * 结合线程池实现通过FutureTask开启异步多线程任务,显然要比一个个执行三个任务要来的快;
 */
public class FutureThreadPoolDemo {
    public static void main(String[] args) throws Exception {
        method();
    }

    private static void method(){
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        long startTime = System.currentTimeMillis();
        FutureTask futureTask1 = new FutureTask(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(5000);
                System.out.println("okle1");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return "task1 over";
        });
        threadPool.submit(futureTask1);

        FutureTask futureTask2 = new FutureTask(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(3000);
                System.out.println("okle2");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return "task2 over";
        });
        threadPool.submit(futureTask2);

        try {
            TimeUnit.MILLISECONDS.sleep(3000);
            System.out.println("okle3");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("---costTime " + (endTime - startTime) + "毫秒");
        System.out.println("it's end");
        threadPool.shutdown();
    }

}
