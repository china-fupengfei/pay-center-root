package code.ponfee.pay.dao;

import java.util.List;
import java.util.Map;

import code.ponfee.pay.model.PayChannel;
import code.ponfee.pay.model.Payment;
import code.ponfee.pay.model.Refund;

public interface IPayDao {

    List<PayChannel> listBySource(String source);

    int insertPayment(Payment payment);

    int updatePayment(Payment payment);

    Payment getPaymentByPayId(String payId);

    boolean checkIsPayed(int orderType, String orderNo);

    Refund getRefundByRefundId(String refundId);
    
    Refund getRefundByRefundNo(int refundType, String refundNo);

    int insertRefund(Refund refund);

    int updateRefund(Refund refund);

    /**
     * 定时支付扫描
     * @param params
     * @return
     */
    List<Payment> listPaymentTaskScan(Map<String, Object> params);

    /**
     * 定时支付扫描更新
     * @param params
     * @return
     */
    int updatePaymentTaskScan(Map<String, Object> params);
    
    /**
     * 定时退款扫描
     * @param params
     * @return
     */
    List<Refund> listRefundTaskScan(Map<String, Object> params);

    /**
     * 定时退款扫描更新
     * @param params
     * @return
     */
    int updateRefundTaskScan(Map<String, Object> params);
    
    /**
     * 清理超时支付数据
     * @return
     */
    int clearTimeoutPayment();
}