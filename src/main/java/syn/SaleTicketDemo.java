package syn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SaleTicketDemo {
    public static void main(String[] args) {
        Ticket ticket = new Ticket();
        new Thread(() -> {for(int i=0;i<55;i++) {ticket.sale();}},"a").start();
        new Thread(() -> {for(int i=0;i<55;i++) {ticket.sale();}},"b").start();
        new Thread(() -> {for(int i=0;i<55;i++) {ticket.sale();}},"c").start();
    }
}

@Data
class Ticket {
    private int number = 50;
    Object lockObject = new Object();

    public void sale() {
        synchronized (lockObject) {
            if(number > 0) {
                System.out.print(Thread.currentThread().getName() + "卖出第" + number +"张票,");
                System.out.println("还剩" + (--number) + "张票");

            }
        }
    }
}
