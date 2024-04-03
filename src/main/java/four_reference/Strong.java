package four_reference;

public class Strong {
    public static void main(String[] args) {
        Strong strong = new Strong();
        System.out.println("gc之前" + strong);
        strong = null;
        System.gc();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("gc之后" + strong);
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("执行垃圾回收了");
    }
}
