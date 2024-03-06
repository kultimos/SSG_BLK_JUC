package syn;

import org.openjdk.jol.info.ClassLayout;

public class SynchronizedUpDemo {
    public static void main(String[] args) {
        Object o = new Object();
        System.out.println("10进制" + o.hashCode());
        System.out.println("16进制" + Integer.toHexString(o.hashCode()));
        System.out.println("2进制" + Integer.toBinaryString(o.hashCode()).length());
        System.out.println(ClassLayout.parseInstance(o).toPrintable().length());
        // 需要注意,这里我们使用了ClassLayout去观察内存结构时,整体上要从右向左,从下向上看,但对于八个连续数字,则从左向右看
    }
}
