package code.ponfee.pay.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import code.ponfee.pay.cached.IPaymentCached;
import code.ponfee.pay.dao.IPayDao;
import code.ponfee.pay.dao.mapper.PayChannelMapper;
import code.ponfee.pay.dao.mapper.PaymentMapper;
import code.ponfee.pay.dao.mapper.RefundMapper;
import code.ponfee.pay.model.PayChannel;
import code.ponfee.pay.model.Payment;
import code.ponfee.pay.model.Refund;

@Repository("payDao")
public class PayDaoImpl implements IPayDao {

    @Resource
    private PaymentMapper payMapper;

    @Resource
    private RefundMapper refundMapper;

    @Resource
    private PayChannelMapper payChannelMapper;

    @Resource
    private IPaymentCached cached;

    @Override
    public List<PayChannel> listBySource(String source) {
        List<PayChannel> list = cached.getPayChannelBySource(source);
        if (list == null) {
            list = payChannelMapper.listBySource(source);
            if (list != null) {
                cached.cachePayChannelBySource(list, source);
            }
        }
        return list;
    }

    @Override
    public int insertPayment(Payment payment) {
        return payMapper.insert(payment);
    }

    @Override
    public int updatePayment(Payment payment) {
        return payMapper.updateStatus(payment);
    }

    @Override
    public Payment getPaymentByPayId(String payId) {
        return payMapper.getByPayId(payId);
    }

    @Override
    public boolean checkIsPayed(int orderType, String orderNo) {
        return payMapper.checkIsPayed(orderType, orderNo);
    }

    /**
     * 定时支付扫描
     * @param params
     * @return
     */
    @Override
    public List<Payment> listPaymentTaskScan(Map<String, Object> params) {
        return payMapper.listPaymentTaskScan(params);
    }

    /**
     * 定时支付扫描更新
     * @param params
     * @return
     */
    @Override
    public int updatePaymentTaskScan(Map<String, Object> params) {
        return payMapper.updatePaymentTaskScan(params);
    }

    // -------------refund-----------------
    @Override
    public int insertRefund(Refund refund) {
        return refundMapper.insert(refund);
    }

    @Override
    public int updateRefund(Refund refund) {
        return refundMapper.updateStatus(refund);
    }

    @Override
    public Refund getRefundByRefundId(String refundId) {
        return refundMapper.getByRefundId(refundId);
    }
    
    @Override
    public Refund getRefundByRefundNo(int refundType, String refundNo) {
        return refundMapper.getRefundByRefundNo(refundType, refundNo);
    }

    /**
     * 定时退款扫描
     * @param params
     * @return
     */
    @Override
    public List<Refund> listRefundTaskScan(Map<String, Object> params) {
        return refundMapper.listRefundTaskScan(params);
    }

    /**
     * 定时退款扫描更新
     * @param params
     * @return
     */
    @Override
    public int updateRefundTaskScan(Map<String, Object> params) {
        return refundMapper.updateRefundTaskScan(params);
    }

    /**
     * 清理超时支付数据
     */
    @Override
    public int clearTimeoutPayment() {
        return payMapper.clearTimeoutPayment();
    }

}