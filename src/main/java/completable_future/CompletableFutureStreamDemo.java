package completable_future;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CompletableFutureStreamDemo {
    /**
     * 介绍需求,我们希望知道一个尚品"mysql",在不同厂商的售价,每一次查询一个厂商的售价会花费1s,我们已知一个厂商的NetMallList
     * 最终我们希望得到一个List<String>,其中的String的格式类似: mysql in jd price is 210;
     */
    static List<NetMall> NetMallList = Arrays.asList(new NetMall("jd"),
            new NetMall("dangdang"), new NetMall("taobao"));

    public static String getPrice(String produceName) {
        try{
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format("mysql in %s price is %s", produceName, new Random().nextInt(100));
    }

    public static void main(String[] args) {
        Long startTime = System.currentTimeMillis();
        /**
         * 同步方式去查找,比较耗时;
         */
        List<String> priceList = NetMallList.stream().map(netMall -> (getPrice(netMall.getProduceName()))).collect(Collectors.toList());
        System.out.println(priceList);
        Long endTime = System.currentTimeMillis();
        System.out.println("normal执行一共花费了" + (endTime - startTime) + "ms");

        startTime = System.currentTimeMillis();

        List<String> finalList = NetMallList.stream().map
                (netMall ->
                        (CompletableFuture.supplyAsync(() ->
                                (getPrice(netMall.getProduceName()))))).collect(Collectors.toList()).stream().map
                (CompletableFuture::join).collect(Collectors.toList());

        /**
         * 这种写法每创建一个CompletableFuture就会往下走,去调用join,join又需要等待结果,实际上就变成了同步,这个还是需要注意,
         * 要向上面那样写,先创建三个CompletableFuture,创建完了以后再去join就是多线程执行了;
         */
        List<String> FakeList = NetMallList.stream().map
                (netMall ->
                        (CompletableFuture.supplyAsync(() ->
                                (getPrice(netMall.getProduceName()))))).map(CompletableFuture::join)
                .collect(Collectors.toList());
        System.out.println("看看咯 " + finalList);
        endTime = System.currentTimeMillis();
        System.out.println("niubi执行一共花费了" + (endTime - startTime) + "ms" );
    }

}



@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
class NetMall {
    private String produceName;
}
