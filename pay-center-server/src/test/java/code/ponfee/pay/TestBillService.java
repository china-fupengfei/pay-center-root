package code.ponfee.pay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import code.ponfee.commons.json.Jsons;
import code.ponfee.commons.model.Pagination;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.util.Dates;
import code.ponfee.pay.dto.RefundApplyResult;
import code.ponfee.pay.model.OrderBill;
import code.ponfee.pay.model.Refund;
import code.ponfee.pay.service.IPayService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:dubbo-consumer.xml" })
public class TestBillService {

    @Resource(name = "dubboPayService")
    private IPayService dubboPayService;

    @Before
    public void setup() {}

    @After
    public void teardown() {

    }

    @Test
    public void testOrderBill() {
        Result<List<OrderBill>> result = dubboPayService.orderBill("201609281101000010035312", "201609291061000010080310", "201609281101000010035312");
        result = dubboPayService.orderBill("201609281101000010035312", "201609291061000010080310", "201609281101000010035312");
        result = dubboPayService.orderBill("201609281101000010035312", "201609291061000010080310", "201609281101000010035312");
        System.out.println(Jsons.NORMAL.stringify(result));
    }

    @Test
    public void testQueryRefundsForPage() {
        Map<String, Object> params = new HashMap<>();
        params.put("beginTime", Dates.toDate("20160701", "yyyyMMdd"));
        params.put("endTime", Dates.toDate("20161001", "yyyyMMdd"));
        params.put("refundType", "1");
        //params.put("pageSize", 1);
        //params.put("pageNum", 2);
        Result<Pagination<Refund>> result = dubboPayService.queryRefundsForPage(params);
        System.out.println(Jsons.NORMAL.stringify(result));
    }

    //@Test
    //public void toRefund4TestPay() {
    //    List<Payment> payments = billDao.listPayment4testPay("2016-01-01 00:00:00", "2016-10-12 23:59:59");
    //    for (Payment payment : payments) {
    //        try {
    //            List<Refund> refunds = billDao.listRefund4testPay(payment.getPayId());
    //            int shouldRefundAmt = payment.getOrderAmt(); 
    //            for (Refund refund : refunds) {
    //                shouldRefundAmt -= refund.getRefundAmt();
    //            }
    //            if (shouldRefundAmt < 1) continue;
    //            
    //            Map<String, String> params = new HashMap<>();
    //            params.put("payId", payment.getPayId());
    //            params.put("refundType", "0");
    //            params.put("refundNo", payment.getOrderNo());
    //            params.put("refundAmt", String.valueOf(shouldRefundAmt));
    //            params.put("refundRate", "100");
    //            params.put("refundReason", "用于手动退测试支付时的钱款");
    //            params.put("remark", "用于手动退测试支付时的钱款");
    //            Result<RefundApplyResult> result = dubboPayService.refund(params);
    //            logger.info(Jsons.DEFAULT.stringify(result));
    //        } catch(Exception e) {
    //            logger.warn("退款失败[{}]", payment.getPayId());
    //        }
    //    }
    //}

    @Test
    public void toRefund4TestPay2() {
        Map<String, String> params = new HashMap<>();
        params.put("payId", "201610111031000001003508");
        params.put("refundNo", "20161013" + System.nanoTime());
        params.put("refundAmt", "1500");

        params.put("refundType", "1");
        params.put("refundRate", "100");
        params.put("refundReason", "用于手动退测试支付时的钱款");
        params.put("remark", "用于手动退测试支付时的钱款");
        Result<RefundApplyResult> result = dubboPayService.refund(params);
        System.out.println(Jsons.NORMAL.stringify(result));
    }

}
