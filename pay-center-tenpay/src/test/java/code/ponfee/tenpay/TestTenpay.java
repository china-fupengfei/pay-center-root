package code.ponfee.tenpay;

import static code.ponfee.tenpay.TenpayConfig.appId;
import static code.ponfee.tenpay.TenpayConfig.appSecret;
import static code.ponfee.tenpay.TenpayConfig.md5Key;
import static code.ponfee.tenpay.TenpayConfig.opUserPwd;
import static code.ponfee.tenpay.TenpayConfig.partner;
import static code.ponfee.tenpay.TenpayConfig.sslContext;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import code.ponfee.commons.constrain.FieldConstraint;
import code.ponfee.commons.json.Jsons;
import code.ponfee.pay.service.IPayService;
import code.ponfee.tenpay.core.Tenpay;
import code.ponfee.tenpay.core.TenpayBuilder;
import code.ponfee.tenpay.model.pay.AppPayRequest;
import code.ponfee.tenpay.model.pay.AppPayResponse;
import code.ponfee.tenpay.model.pay.WebPayRequest;

public class TestTenpay {

    private Tenpay tenpay;

    @Resource
    private IPayService payService;

    @Before
    public void init() throws Exception {
        tenpay = TenpayBuilder.newBuilder(partner, md5Key, sslContext, appId, appSecret, opUserPwd).build();
    }

    @Test
    public void testWebPay() {
        WebPayRequest request = new WebPayRequest();
        request.setBody("测试");
        request.setReturnUrl("test");
        request.setNotifyUrl("test");
        request.setOutTradeNo("201605260001");
        request.setTotalFee(1);
        request.setSpbillCreateIp("183.16.194.159");
        System.out.println(tenpay.pay().webPay(request));

    }

    @Test
    public void testAppPay() throws UnsupportedEncodingException {
        AppPayRequest request = new AppPayRequest();
        request.setDesc("12308汽车票");
        //request.setPurchaserId("395191357");
        request.setSpBillno("201605270008");
        request.setTotalFee(100);

        Calendar c = Calendar.getInstance();
        request.setTimeStart(c.getTime());
        c.add(Calendar.MINUTE, 10);
        request.setTimeExpire(c.getTime());

        AppPayResponse resp = tenpay.pay().appPay(request);
        System.out.println(Jsons.NORMAL.stringify(resp));
    }

    @Test
    public void testCst() throws Exception {
        for (int i = 0; i < 1; i++) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    WebPayRequest request = new WebPayRequest();
                    request.setBody("test");
                    request.setReturnUrl("returnUrl");
                    request.setNotifyUrl("notifyUrl");
                    request.setOutTradeNo("outTradeNo");
                    request.setTotalFee(1);
                    request.setSpbillCreateIp("spbillCreateIp");
                    request.setPartner("312312");
                    FieldConstraint.newInstance().constrain(request);
                }
            }).start();
        }

        Thread.sleep(100000);
    }

}
