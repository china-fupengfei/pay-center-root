package code.ponfee.pay.dao.mapper;

import java.util.List;
import java.util.Map;

import code.ponfee.pay.model.OrderBill;
import code.ponfee.pay.model.Refund;

public interface BillMapper {

    OrderBill getPayBill(String payId);

    List<OrderBill> listRefundBill(String payId);
    
    List<Refund> queryRefundsForList(Map<String, ?> params);
    
    /*用于手动退测试支付时的钱款*/
    //List<Payment> listPayment4testPay(@Param("beginDate") String b, @Param("endDate") String e);
    //List<Refund> listRefund4testPay(String payId);
}