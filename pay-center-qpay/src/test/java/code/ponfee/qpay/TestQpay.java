package code.ponfee.qpay;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import code.ponfee.commons.util.Dates;
import code.ponfee.qpay.core.Qpay;
import code.ponfee.qpay.core.QpayBuilder;
import code.ponfee.qpay.model.pay.PrepayRequest;

public class TestQpay {

    private Qpay qpay;
    
    @Before
    public void setup() throws Exception {
        qpay = QpayBuilder.newBuilder(QpayConfig.appid, QpayConfig.mchId, QpayConfig.md5Key,  QpayConfig.appKey, QpayConfig.sslContext, QpayConfig.opUserId, QpayConfig.opUserPwd).build();
    }
    
    @Test
    public void testNativePay() {
        PrepayRequest req = new PrepayRequest();
        req.setBody("body");
        req.setNotifyUrl("http://www.12308.com");
        req.setOutTradeNo(String.valueOf(System.currentTimeMillis()));
        req.setSpbillCreateIp("127.0.0.1");
        req.setTimeExpire(Dates.plusMinutes(new Date(), 30));
        req.setTimeStart(new Date());
        req.setTotalFee(1);
        String s = qpay.pays().nativePay(req);
        System.out.println(s);
    }
    
    @Test
    public void testAppPay() {
        PrepayRequest req = new PrepayRequest();
        req.setBody("body");
        req.setNotifyUrl("http://www.12308.com");
        req.setOutTradeNo(String.valueOf(System.currentTimeMillis()));
        req.setSpbillCreateIp("127.0.0.1");
        req.setTimeExpire(Dates.plusMinutes(new Date(), 30));
        req.setTimeStart(new Date());
        req.setTotalFee(1);
        String s = qpay.pays().appPay(req);
        System.out.println(s);
    }
}
