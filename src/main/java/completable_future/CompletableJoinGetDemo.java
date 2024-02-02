package completable_future;

import java.util.concurrent.CompletableFuture;

public class CompletableJoinGetDemo {
    public static void main(String[] args) {
        CompletableFuture completableFuture = CompletableFuture.supplyAsync(() -> {
            String str = "sasa";
            return str;
        });
        /**
         * 二者功能几乎是一样的,区别就是:
         * join在编译期间不会对检查性异常做处理,即我们不需要处理异常;
         * get需要处理异常,不管你是直接抛出还是自己捕获,总要处理;
         */
        System.out.println(completableFuture.join());
    }
}
