package code.ponfee.pay.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import code.ponfee.pay.model.Payment;

public interface PaymentMapper {

    int insert(Payment payment);

    int updateStatus(Payment payment);

    Payment getByPayId(String payId);

    /**
     * 校验是否已完成支付（钱包支付使用）
     * @param a
     * @param b
     * @return
     */
    boolean checkIsPayed(@Param("orderType") int a, @Param("orderNo") String b);

    /**
     * 定时扫描
     * @param params
     * @return
     */
    List<Payment> listPaymentTaskScan(Map<String, Object> params);

    /**
     * 定时扫描更新
     * @param params
     * @return
     */
    int updatePaymentTaskScan(Map<String, Object> params);

    /**
     * 定时清除超时支付数据
     * @return
     */
    int clearTimeoutPayment();
}