package syn;

/**
 * 锁粗化案例
 */
public class LockCoarsening {
    static Object theLock = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (theLock) {
                System.out.println("1111111");
            }
            synchronized (theLock) {
                System.out.println("2222222");
            }
            synchronized (theLock) {
                System.out.println("3333333");
            }
            synchronized (theLock) {
                System.out.println("4444444");
            }
        },"t1").start();
    }
}
