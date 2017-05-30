package code.ponfee.pay.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import code.ponfee.commons.model.PageHandler;
import code.ponfee.commons.model.Pager;
import code.ponfee.pay.dao.IBillDao;
import code.ponfee.pay.dao.mapper.BillMapper;
import code.ponfee.pay.model.OrderBill;
import code.ponfee.pay.model.Refund;

@Repository("billDao")
public class BillDaoImpl implements IBillDao {

    @Resource
    private BillMapper billMapper;

    @Override
    public OrderBill getPayBill(String payId) {
        return billMapper.getPayBill(payId);
    }

    @Override
    public List<OrderBill> listRefundBill(String payId) {
        return billMapper.listRefundBill(payId);
    }

    @Override
    public Pager<Refund> queryRefundsForPage(Map<String, ?> params) {
        PageHandler.NORMAL.handle(params);
        return new Pager<>(billMapper.queryRefundsForList(params));
    }

    /*用于手动退测试支付时的钱款*/
    //public List<Payment> listPayment4testPay(String beginDate, String endDate){
    //    return billMapper.listPayment4testPay(beginDate, endDate);
    //}
    //public List<Refund> listRefund4testPay(String payId){
    //    return billMapper.listRefund4testPay(payId);
    //}
}