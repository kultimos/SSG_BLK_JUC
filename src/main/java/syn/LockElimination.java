package syn;

/**
 * 锁消除案例
 */
public class LockElimination {
    private static Object theLock = new Object();

    /**
     * 正确的加锁,即多个线程去强制一个锁
     */
    public void methodHaveLock() {
        synchronized (theLock) {
            System.out.println("静态代码块" + theLock.hashCode());
        }
    }

    /**
     * 会出现锁消除,因为这段代码实际上是每个线程都会new一个对象,然后锁的也是这个new出来的对象,那10个线程实际上是10把锁
     * 此时JIT就会认为此时的锁是没必要的,就会进行锁消除
     */
    public void methodElimination() {
        Object o = new Object();
        synchronized (o) {
            System.out.println("静态代码块" + o.hashCode());
        }
    }

    public static void main(String[] args) {
        LockElimination lockElimination = new LockElimination();
        for(int i=0;i<10;i++) {
            new Thread(() -> {
                lockElimination.methodElimination();
            }).start();
        }
    }
}
