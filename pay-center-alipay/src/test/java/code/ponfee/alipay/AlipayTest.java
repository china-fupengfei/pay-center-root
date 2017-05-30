package code.ponfee.alipay;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import code.ponfee.alipay.core.Alipay;
import code.ponfee.alipay.core.AlipayBuilder;
import code.ponfee.alipay.model.pay.AppPayRequest;
import code.ponfee.alipay.model.pay.WapPayRequest;
import code.ponfee.alipay.model.pay.WebPayRequest;

/**
 */
public class AlipayTest {
    private Alipay md5Pay;

    private Alipay rsaPay;

    @Before
    public void init() throws Exception {
        md5Pay = AlipayBuilder.newBuilder(AlipayConfig.partner, AlipayConfig.md5Key).build();
        rsaPay = AlipayBuilder.newBuilder(AlipayConfig.partner, AlipayConfig.ptnPriKey, AlipayConfig.aliPubKey).build();
    }

    @Test
    public void testWebPay() {
        WebPayRequest fields = new WebPayRequest("DM1234567800", "ss", "0.01");
        fields.setExterInvokeIp("222.112.105.129");
        String form = md5Pay.pay().webPay(fields);
        assertNotNull(form);
        System.out.println(form);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWebPayOutTradeNoEmpty() {
        WebPayRequest fields = new WebPayRequest("", "ss", "0.01");
        fields.setExterInvokeIp("222.112.105.129");
        String form = md5Pay.pay().webPay(fields);
        assertNotNull(form);
        System.out.println(form);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWebPayOrderNameNull() {
        WebPayRequest fields = new WebPayRequest("DM1234567800", null, "0.01");
        fields.setExterInvokeIp("222.112.105.129");
        String form = md5Pay.pay().webPay(fields);
        assertNotNull(form);
        System.out.println(form);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWebPayTotalFeeEmpty() {
        WebPayRequest fields = new WebPayRequest("DM1234567800", "ss", "");
        fields.setExterInvokeIp("222.112.105.129");
        String form = md5Pay.pay().webPay(fields);
        assertNotNull(form);
        System.out.println(form);
    }

    @Test
    public void testWapPay() {
        WapPayRequest fields = new WapPayRequest("DM12345678999", "ss", "0.01");
        String form = md5Pay.pay().wapPay(fields);
        assertNotNull(form);
        System.out.println(form);
    }

    @Test
    public void testAppPay() {
        AppPayRequest fields = new AppPayRequest("DM1234567779", "ss", "0.01", "测试商品");
        String payString = rsaPay.pay().appPay(fields);
        assertNotNull(payString);
        System.out.println(payString);
    }

    @Test
    public void testVerifyNotifyId() {
        assertTrue(md5Pay.verify().verifyNotify("3eec9e00f788cfede8f767eb2a22a7ec54"));
        assertFalse(md5Pay.verify().verifyNotify("xxxxx"));
    }
}
