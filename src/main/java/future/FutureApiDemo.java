package future;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 实现一个简单的案例,首先创建一个Callable的实现类,将他作为参数传入FutureTask的构造方法,最后再用这个FutureTask去创建一个Thread对象来执行线程
 * 现在这个thread对象就拥有了FutureTask提供的多个功能,我们也对部分api进行了演示;
 */
public class FutureApiDemo {
    public static void main(String[] args) {
        FutureTask futureTask = new FutureTask(new MyThread());
        Thread t1 = new Thread(futureTask);
        System.out.println("什么情况");
        t1.start();
        System.out.println("结束了吗" + futureTask.isDone());
        futureTask.cancel(false);
        System.out.println("被打断了吗" + futureTask.isCancelled());
        try {
            String threadResult = (String) futureTask.get();
            System.out.println("看一下线程返回结果" + threadResult);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

class MyThread implements Callable {

    public String call() throws Exception {
        Thread.sleep(5000);
        System.out.println("sa");
        return "woo";
    }
}
