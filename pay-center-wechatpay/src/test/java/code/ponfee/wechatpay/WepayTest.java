package code.ponfee.wechatpay;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import code.ponfee.commons.util.Dates;
import code.ponfee.wechatpay.core.Wechatpay;
import code.ponfee.wechatpay.core.WechatpayBuilder;
import code.ponfee.wechatpay.model.bill.Bill;
import code.ponfee.wechatpay.model.bill.BillDetail;
import code.ponfee.wechatpay.model.bill.CommonBill;
import code.ponfee.wechatpay.model.bill.RefundBill;
import code.ponfee.wechatpay.model.order.WechatpayOrder;
import code.ponfee.wechatpay.model.pay.AppPayResponse;
import code.ponfee.wechatpay.model.pay.JsPayRequest;
import code.ponfee.wechatpay.model.pay.JsPayResponse;
import code.ponfee.wechatpay.model.pay.PayRequest;
import code.ponfee.wechatpay.model.refund.RefundApplyRequest;
import code.ponfee.wechatpay.model.refund.RefundApplyResponse;

/**
 */
public class WepayTest {

    private Wechatpay wepay;

    @Before
    public void init() throws IOException {
        wepay = WechatpayBuilder.newBuilder(
                WechatpayConfig.appId,
                WechatpayConfig.appKey,
                WechatpayConfig.mchId,
                WechatpayConfig.sslContext).build();
    }

    @Test
    public void testJsPay(){
        JsPayRequest request = new JsPayRequest();
        request.setBody("test");
        request.setClientIp("127.0.0.1");
        request.setTotalFee(1);
        request.setNotifyUrl("http://www.xxx.com/notify");
        request.setOpenId("onN_8trIW7PSoXLMzMSWySb5jfdY");
//        request.setOpenId("oC2u8uKC1OSSzwbMuMEGmFgvoEDE");
        request.setOutTradeNo("JSAPI_TEST12345671");
        request.setTimeStart(Dates.now("yyyyMMddHHmmss"));
        JsPayResponse resp = wepay.pay().jsPay(request);
        assertNotNull(resp);
        System.out.println(resp);
    }
    
    @Test
    public void testWapPay(){
        PayRequest request = new PayRequest();
        request.setBody("test");
        request.setClientIp("127.0.0.1");
        request.setTotalFee(1);
        request.setNotifyUrl("http://www.xxx.com/notify");
        request.setOutTradeNo("WAP_TEST12345671");
        request.setTimeStart(Dates.now("yyyyMMddHHmmss"));
        String str = wepay.pay().wapPay(request);
        assertNotNull(str);
        System.out.println(str);
    }

    @Test
    public void testQrPay(){
        PayRequest request = new PayRequest();
        request.setBody("test");
        request.setClientIp("127.0.0.1");
        request.setTotalFee(1);
        request.setNotifyUrl("http://www.xxx.com/notify");
        request.setOutTradeNo("NATIVE_TEST12345671");
        request.setTimeStart(Dates.now("yyyyMMddHHmmss"));
        String resp = wepay.pay().qrPay(request, Boolean.TRUE); // TODO
        assertNotNull(resp);
        System.out.println(resp);
    }

    @Test
    public void testAppPay(){
        PayRequest request = new PayRequest();
        request.setBody("test");
        request.setClientIp("127.0.0.1");
        request.setTotalFee(1);
        request.setNotifyUrl("http://www.xxx.com/notify");
        request.setOutTradeNo("APP_TEST12345671");
        request.setTimeStart(Dates.now("yyyyMMddHHmmss"));
        AppPayResponse resp = wepay.pay().appPay(request);
        assertNotNull(resp);
        System.out.println(resp);
    }

    @Test
    public void testQueryOrderByOutTradeNo(){
        WechatpayOrder order = wepay.order().queryOrder("TEST3344520", "1000530784201510111158030445");
        assertNotNull(order);
        System.out.println(order);
    }

    @Test
    public void testCloseOrder(){
        assertTrue(wepay.order().closeOrder("TEST12345671"));
    }

    @Test
    public void testRefundApply(){
        RefundApplyRequest req = new RefundApplyRequest();
        req.setTransactionId("1003110578201512021860142525");
        req.setOutRefundNo("TEST3344520");
        req.setTotalFee(1);
        req.setRefundFee(1);
        req.setOpUserId(wepay.getMchId());

        RefundApplyResponse resp = wepay.refund().apply(req);
        assertNotNull(resp);
        System.out.println(resp);
    }

    @Test
    public void testRefundQuery(){
        Map<String, String> map = new HashMap<>();
        map.put("out_trade_no", "TEST3344556677");
        map.put("transaction_id", "1003110578201511281803217943");
        map.put("refund_id", "2003110578201512010090200506");
        map.put("out_refund_no", "TEST3344556677");
        wepay.refund().queryRefund(map);
        
    }

    @Test
    public void testQueryBill(){
        BillDetail<CommonBill> allBill = wepay.bill().queryAll(null, "20160503");
        assertNotNull(allBill);
        assertEquals(allBill.getBills().size(), allBill.getCount().getTradeTotalCount().intValue());
        System.out.println(allBill.getBills().get(0));
        System.out.println(allBill.getCount());

        BillDetail<Bill> successBill = wepay.bill().querySuccess(null, "20160504");
        assertNotNull(successBill);
        assertEquals(successBill.getBills().size(), successBill.getCount().getTradeTotalCount().intValue());
        System.out.println(successBill.getBills().get(0));
        System.out.println(successBill.getCount());

        BillDetail<RefundBill> refundBill = wepay.bill().queryRefund(null, "20160505");
        assertNotNull(refundBill);
        assertEquals(refundBill.getBills().size(), refundBill.getCount().getTradeTotalCount().intValue());
        System.out.println(refundBill.getBills().get(0));
        System.out.println(refundBill.getCount());
    }
}
