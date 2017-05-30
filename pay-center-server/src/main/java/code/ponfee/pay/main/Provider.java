package code.ponfee.pay.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * main
 * @author fupf
 */
public class Provider {

    private static ClassPathXmlApplicationContext context;

    public static void main(String[] args) {
        context = new ClassPathXmlApplicationContext(new String[] { "META-INF/spring/pay-center-provider.xml" });
        context.start();
        System.out.println("==========================支付中心已启动=========================");
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
