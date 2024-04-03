package thread_local;

public class initMethod {
    public static void main(String[] args) {
        /**
         * ThreadLocal初始化旧版写法
         */
        ThreadLocal<Integer> local = new ThreadLocal<Integer>() {
            @Override
            protected Integer initialValue() {
                return 0;
            }
        };

        /**
         * ThreadLocal jdk1.8后的新写法,更清爽
         */
        ThreadLocal<Integer> local1 = ThreadLocal.withInitial(() -> 0);

        /**
         * 初始化为null的情况
         */
        ThreadLocal local2 = new ThreadLocal();
    }
}
