package completable_future;

import java.sql.Time;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CompletableFutureMethodDemo {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        try {
//            showGet(threadPool);
//            showComplete(threadPool);
//            showThenApply(threadPool);
//            showHandle(threadPool);
//            compareAcceptApplyRun(threadPool);
//            showApplyToEither(threadPool);
            showThenCombine(threadPool);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }

    /**
     * @apiNote 等两个future任务结束后,可以最终将两个任务的结果一起交给thenCombine来处理
     * @param threadPool
     */
    public static void showThenCombine(ExecutorService threadPool) {
        CompletableFuture<Integer> CA = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("A is begin");
                TimeUnit.SECONDS.sleep(2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 10;
        });
        CompletableFuture<Integer> CB = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("B is begin");
                TimeUnit.SECONDS.sleep(4);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 14;
        });
        CompletableFuture<Integer> integerCompletableFuture = CA.thenCombine(CB, (x, y) -> {
            return x + y;
        });
        System.out.println(integerCompletableFuture.join());
    }

    /**
     * @apiNote 可以获取两个异步任务里运行速度更快的那个任务信息
     * @param threadPool
     */
    public static void showApplyToEither(ExecutorService threadPool) {
        CompletableFuture CA = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return " A ";
        });
        CompletableFuture CB = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return " B ";
        });
        CompletableFuture compare = CA.applyToEither(CB, (f) -> {
            System.out.println("获取更快结束的future的返回值: " + f);
            return "winner is " + f;
        });
        try {
            TimeUnit.SECONDS.sleep(4);
            System.out.println(compare.join());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * @apiNote
     * thenApply()    任务A执行完再执行任务B,B需要A的结果,同时任务B有返回值
     * thenAccept()    任务A执行完再执行任务B,B需要A的结果,任务B没有返回值
     * thenRun()    任务A执行完再执行任务B,B不需要A的结果,任务B没有返回值
     * @param threadPool
     */
    public static void compareAcceptApplyRun(ExecutorService threadPool) {
        System.out.println(CompletableFuture.supplyAsync(() -> "result").thenApply((v) -> "apply" + v).join());
        System.out.println(CompletableFuture.supplyAsync(() -> "result").thenAccept((v) -> System.out.println("内部打印" + v)).join());
        System.out.println(CompletableFuture.supplyAsync(() -> "result").thenRun(() -> {}).join());
    }

    /**
     * @apiNote 相比于thenApply(),handle多了对于异常的处理,即使前一个链中出现异常,程序依然会正常往下走,只不过当前异常链的返回值是null;
     * 至此,异常对于整体结构的影响只限于异常的那条链,后续无论你是否对异常进行处理,程序都会正常往下走,一切如旧,
     * 只是过程中出现了一个小异常,一个链的return变为了null,而已;
     * @param threadPool
     */
    public static void showHandle(ExecutorService threadPool) {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            int a = 100 / 0;
            System.out.println("一切如旧,异常并不会影响程序继续执行");
            return 35;
        }, threadPool).handle((v,e) -> {
            System.out.println("拿到异常链传递下来的value :  " + v);
            System.out.println(e);
            if(e != null) {
                v = 300;
            }
            return v;
        });
        System.out.println(completableFuture.join());
    }

    /**
     * @apiNote  可以看到出现异常后,输出也不会打印,也不会有return链存在,都已经被异常打断了,future的join()也拿不到结果,因为在发生异常时,
     * 一起都已经结束了;
     * @param threadPool
     */
    public static void showThenApply(ExecutorService threadPool) {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            int a = 100 / 0;
            System.out.println("是否会打印");
            return a;
        }, threadPool).thenApply(f -> {
            System.out.println("程序实际已经终止" + f);
            return f;
        });
        System.out.println("是否能拿到future的值: "+completableFuture.join());
    }


    /**
     * @apiNote
     * complete用于判断当前future是否结束,
     * 若未结束则打断,并将参数作为future的返回值,返回ture;
     * 若结束,则返回false
     * 注意理解,这里complete的打断并不是直接打断future内部的代码块,如果你在代码块中写了其他内容这些内容仍然会被执行;
     * 这里complete打断的是future的链式结构,即打断的只有最后一句即return语句,所以我们后续再去调用join看到的都是complete的参数
     * 此外,也可以看到后续我们想去用链式结构thenApply拿到上一part的值也是不可以的,他也没有拿到0,也没用拿到100,而是完全拿不到,因为return语句被打断了;
     * @param threadPool
     */
    public static void showComplete(ExecutorService threadPool) {
        AtomicInteger test = new AtomicInteger();
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            int a = 100;
            test.set(300);
            System.out.println("会执行吗");
            return a;
        }, threadPool).thenApply(f -> {
            System.out.println("呵呵" + f);
            return f;
        });

        try {
            TimeUnit.SECONDS.sleep(1);
//            System.out.println("结果" + completableFuture.complete(0) + "\t" + completableFuture.join());
            TimeUnit.SECONDS.sleep(5);
            int a = completableFuture.join();
            System.out.println(a);
            System.out.println("看看test" + test);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * @apiNote
     * getNow()方法同样是要求获取CompletableFuture的值,但如果此时程序还在运行,无法返回结果,getNow方法就将返回传入的参数值;
     * 如果程序已经结束,则正常返回线程内部的返回值
     * @param threadPool
     */
    public static void showGet(ExecutorService threadPool) {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("开始执行");
            int a = 0;
            return a;
        }, threadPool);

        try {
            TimeUnit.SECONDS.sleep(3);
            Integer now = completableFuture.getNow(10);
            System.out.println("最终结果" + now);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
