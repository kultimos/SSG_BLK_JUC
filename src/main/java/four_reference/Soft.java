package four_reference;

import java.lang.ref.SoftReference;

public class Soft {
    public static void main(String[] args) {
        SoftReference<Soft> softSoftReference = new SoftReference<>(new Soft());
        System.out.println("gc之前" + softSoftReference.get());
        System.gc();


        try {
            Thread.sleep(1000);
            byte[] bytes = new byte[20 * 1024 * 1024];
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("gc之后" + softSoftReference.get());
        }

    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("执行垃圾回收了");
    }
}
