package code.ponfee.pay.dao;

import java.util.List;
import java.util.Map;

import code.ponfee.commons.model.Pager;
import code.ponfee.pay.model.OrderBill;
import code.ponfee.pay.model.Refund;

public interface IBillDao {

    OrderBill getPayBill(String payId);
    
    List<OrderBill> listRefundBill(String payId);
    
    Pager<Refund> queryRefundsForPage(Map<String, ?> params);
    
    /*用于手动退测试支付时的钱款*/
    //List<Payment> listPayment4testPay(String beginDate, String endDate);
    //List<Refund> listRefund4testPay(String payId);
}