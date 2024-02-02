package future;

import java.util.concurrent.*;

public class FutureGetDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture s = new CompletableFuture();
        FutureTask futureTask = new FutureTask(() -> {
            try {TimeUnit.MILLISECONDS.sleep(5000);} catch (Exception e) { e.printStackTrace();}
            return "it's over";
        });
        Thread t1 = new Thread(futureTask);
        t1.start();
        /**
         * 通过两次打印的位置可以看出,很明显在主线程输出之前调用futureTask.get()会出现阻塞,
         * 其实也很好理解,既然你调用了get()方法,那肯定要给你返回一个结果,可是当前这个futureTask需要执行5000ms才可以返回结果,
         * 那能怎么办,只能等futureTask执行结束才能获取返回结果啊,所以就阻塞了,直到get拿到结果才会继续执行主线程的内容
         * 这就是futureTask的一个缺点: 一旦调用get方法,非要等到结果才会离开,一旦将get()放在代码中部位置,就会造成阻塞;
         */
//        System.out.println(futureTask.get());

        /**
         * 为了避免无休止的等待,我们可以通过限制等待时间的get方法来获取结果,一旦在一定的时间内没有拿到结果,就会抛出异常,我们可以通过
         * 捕获异常处理来保证代码不会长时间阻塞,但是需要注意,我们这里不等了以后,只是在主线程中不在继续等待结果,futureTask的任务并不会
         * 终止,还会继续执行,直到任务执行完毕,整个流程才算真正的结束;
         */
        try {
            System.out.println(futureTask.get(1,TimeUnit.SECONDS));
        } catch (TimeoutException e) {
            System.out.println("ok,爷等够了");
        }
        System.out.println("主线程来也");
//        System.out.println(futureTask.get());
    }
}
