package aqs;

/**
 * 中文注释版AQS源码介绍学习
 */
public class AQSProperty {
    /**
     * 表示锁或同步组件的状态,0表示空闲,1表示已有线程成功获取锁
     */
    private volatile int state;

    static final class Node {
        /**
         * 表示线程以共享的模式等待锁
         */
        static final Node SHARED = new Node();

        /**
         * 表示线程以独占的方式等待锁
         */
        static final Node EXCLUSIVE = null;

        /**
         * 表示当前节点在队列中的状态,具体的状态由下面集中
         */
        volatile int waitStatus;

        /**
         * waitStatus=1,表示线程获取锁的请求已经取消
         */
        static final int CANCELLED =  1;

        /**
         * waitStatus=-1,表示线程已经准备就绪,就等锁被释放
         */
        static final int SIGNAL    = -1;

        /**
         * waitStatus=-2,表示当有一个线程调用方法ReentrantLock的condition的await()方法时,该线程变为节点进入队列后,
         * waitStatus状态就会被设置为-2,用于标识是condition的方式阻塞的;
         * 说点个人理解,其实就是在底层进行了区分,当你使用了
         * condition使线程进入阻塞状态就会被标记为-2,我不知道是不是跟synchronized本身不是公平锁而Lock是公平锁的问题
         */
        static final int CONDITION = -2;

        /**
         * waitStatus=-3,看下AI的解释吧
         * 具体来说，当一个线程释放锁的时候,会根据一定的条件来决定是否需要唤醒后续的等待线程;
         * 当需要唤醒后续线程时,原先等待节点会将状态设为 PROPAGATE，以便后续节点可被正确唤醒。
         * 在 AQS中,PROPAGATE被用于控制锁状态的传播,它是一个常量,用来表示状态传播的特殊情况;
         * 通过将某个节点的状态设置为 PROPAGATE,在释放锁的时候可以触发对后续节点的状态传播,从而达到高效并发控制的目的。
         * 因此，PROPAGATE的赋值为-3是为了在AQS中清晰的标识状态传播的特殊情况,并在释放锁时正确处理后续线程的唤醒操作。
         */
        static final int PROPAGATE = -3;

        /**
         * 前驱指针
         */
        volatile Node prev;

        /**
         * 后继指针
         */
        volatile Node next;

        /**
         * 同步队列的头指针
         */
        private transient volatile Node head;

        /**
         * 同步队列的尾指针
         */
        private transient volatile Node tail;

        /**
         * 线程尝试获取锁时,就会被封装为一个node对象来参与锁的竞争和等待,这个thread就用来接收线程对象
         */
        volatile Thread thread;
    }
}
