package completable_future;

import java.util.Random;
import java.util.concurrent.*;

public class CompletableDemo {
    public static void main(String[] args) {
        /**
         * 演示了CompletableFuture想必Future的几个优点:
         *  异步任务结束后,会自动回调某个对象的方法;
         *  主线程设置好回调后,不再关心异步任务的执行,异步任务之间可以顺序执行
         *  异步任务出错时,会自动回调某个对象的方法;
         */

        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        try {
            CompletableFuture.supplyAsync(() -> {
                int result = 0;
                try {
                    result = new Random().nextInt(10);
                    System.out.println("生成一个数" + result);
                    TimeUnit.SECONDS.sleep(2);
                    if(result>2) {
                        int i = 10/0;
                    }
                    /**
                     * 关于这里有个tips,我们的异常情况抛出的异常是ArithmeticException,而我们这里捕获的异常是InterruptedException
                     * 并不相同,所以无法捕获,所以后续whenComplete(v,e)中的e就是ArithmeticException
                     * 但如果我们捕获的异常是Exception,那样就会把我们的ArithmeticException成功捕获,这样再执行到下面的whenComplete(v,e)时,
                     * e就是null了,这个需要留意;
                     */
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return result;
            }, threadPool).whenComplete((v,e) -> {
                if(e == null ) {
                    System.out.println("得到结果" + v);
                }
            }).exceptionally(e -> {
                System.out.println("出现异常了"  + e.getMessage());
                return null;
            });

            System.out.println("主线程独自美丽");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}
