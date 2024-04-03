package four_reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;

public class Phantom {
    public static void main(String[] args) {
        ReferenceQueue<Phantom> queue = new ReferenceQueue<>();
        PhantomReference phantomReference = new PhantomReference<>(new Phantom(), queue);
        System.out.println("虚引用" + phantomReference.get()); // 他还是个null,虚引用的get方法都是null

        List<byte[]> list = new ArrayList<>();
//        new Thread(() ->{
//             while (true) {
//                 list.add(new byte[1 * 1024*1024]);
//                 try {
//                     Thread.sleep(1000);
//                 } catch (Exception e) {
//                     e.printStackTrace();
//                 }
//                 System.out.println("list新增成功");
//             }
//        },"t1").start();

        new Thread(() -> {
            while (true) {
                Reference<? extends Phantom> reference = queue.poll();
                if(reference != null) {
                    System.out.println("虚引用加入了队列");
                    break;
                }
            }
        },"t2").start();
    }

}
