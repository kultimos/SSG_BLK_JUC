package future;

import java.sql.Time;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class FutureIsDoneDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask futureTask = new FutureTask(() ->{
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "it's over";
        });
        Thread thread = new Thread(futureTask);
        thread.start();

        /**
         * 通过延迟轮询isDone来获取get结果
         */
        while(true) {
            if(futureTask.isDone()) {
                System.out.println(futureTask.get());
                break;
            } else {
                // 延迟500ms后再去查询futureTask已经完成
                System.out.println("延迟轮询");
              TimeUnit.MILLISECONDS.sleep(500);
            }
        }
        System.out.println("主线程来了");
    }
}
