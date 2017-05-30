//package code.ponfee.pay.pursepay;
//
//import static code.ponfee.pay.center.common.Constants.DEFAULT_DATE_FORMAT;
//import static code.ponfee.pay.center.common.Constants.PARAM_BIZ_TYPE;
//import static code.ponfee.pay.center.common.Constants.PARAM_CHANNEL_TYPE;
//import static code.ponfee.pay.center.common.Constants.PARAM_CLIENT_IP;
//import static code.ponfee.pay.center.common.Constants.PARAM_EXPIRE_TIME;
//import static code.ponfee.pay.center.common.Constants.PARAM_GOODS_BODY;
//import static code.ponfee.pay.center.common.Constants.PARAM_HANDLE_FEE;
//import static code.ponfee.pay.center.common.Constants.PARAM_ORDER_AMT;
//import static code.ponfee.pay.center.common.Constants.PARAM_ORDER_NO;
//import static code.ponfee.pay.center.common.Constants.PARAM_ORDER_TYPE;
//import static code.ponfee.pay.center.common.Constants.PARAM_PAY_ID;
//import static code.ponfee.pay.center.common.Constants.PARAM_REFUND_AMT;
//import static code.ponfee.pay.center.common.Constants.PARAM_REFUND_ID;
//import static code.ponfee.pay.center.common.Constants.PARAM_REFUND_REASON;
//import static code.ponfee.pay.center.common.Constants.PARAM_USER_ID;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang3.math.NumberUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeanUtils;
//
//import cn.commons.lang.spring.SpringBeanUtils;
//import cn.purse.req.PursePayRequest;
//import cn.purse.req.PurseRefundRequest;
//import cn.purse.service.IPurseService;
//import code.ponfee.commons.model.Result;
//import code.ponfee.commons.model.ResultCode;
//import code.ponfee.commons.util.Dates;
//import code.ponfee.pay.alipay.model.pay.PayQueryResponse;
//import code.ponfee.pay.center.dao.IPayDao;
//import code.ponfee.pay.center.service.impl.PayServiceAdapter;
//import code.ponfee.pay.dto.PayApplyResult;
//import code.ponfee.pay.dto.PayQueryResult;
//import code.ponfee.pay.dto.RefundApplyResult;
//import code.ponfee.pay.dto.RefundQueryResult;
//import code.ponfee.pay.dto.RefundQueryResult.RefundDetail;
//import code.ponfee.pay.enums.PaymentStatus;
//import code.ponfee.pay.enums.RefundStatus;
//import code.ponfee.pay.qpay.model.refund.RefundQueryResponse;
//import code.ponfee.pay.qpay.model.refund.RefundQueryResponse.RefundItem;
//
///**
// * 钱包支付
// * @author fupf
// */
//public class PursepayService extends PayServiceAdapter {
//    private static Logger logger = LoggerFactory.getLogger(PursepayService.class);
//
//    public @Override Result<PayApplyResult> pay(Map<String, String> params) {
//        int orderType = NumberUtils.toInt(params.get(PARAM_ORDER_TYPE));
//        String orderNo = params.get(PARAM_ORDER_NO);
//        if (SpringBeanUtils.getBean(IPayDao.class).checkIsPayed(orderType, orderNo)) {
//            // 已支付
//            logger.error("订单重复付款[{}-{}]", orderType, orderNo);
//            return Result.failure(ResultCode.PAY_REPEAT_ERROR);
//        }
//
//        PursePayRequest req = new PursePayRequest();
//        req.setUserId(NumberUtils.toInt(params.get(PARAM_USER_ID)));
//        req.setBizType(NumberUtils.toInt(params.get(PARAM_BIZ_TYPE)));
//        req.setOrderAmt(NumberUtils.toInt(params.get(PARAM_ORDER_AMT)));
//        req.setPayId(params.get(PARAM_PAY_ID));
//        req.setRemark(params.get(PARAM_GOODS_BODY));
//        req.setClientIp(params.get(PARAM_CLIENT_IP));
//        req.setTimeExpire(Dates.toDate(params.get(PARAM_EXPIRE_TIME), DEFAULT_DATE_FORMAT));
//        Result<String> _result = SpringBeanUtils.getBean(IPurseService.class).pay(req);
//        if (_result.isSuccess()) {
//            PayApplyResult rs = new PayApplyResult();
//            rs.setChannelType(params.get(PARAM_CHANNEL_TYPE));
//            rs.setPayId(params.get(PARAM_PAY_ID));
//            rs.setStatus(PaymentStatus.SUCCESS); // 支付成功
//            rs.setThirdTradeNo(_result.getResult());
//            rs.setResponse("SUCCESS");
//            return new Result<>(_result.getCode(), _result.getMsg(), rs);
//        } else {
//            return new Result<>(_result.getCode(), _result.getMsg());
//        }
//    }
//
//    /**
//     * 退款
//     */
//    public @Override Result<RefundApplyResult> refund(Map<String, String> params) {
//        PurseRefundRequest req = new PurseRefundRequest();
//        req.setUserId(NumberUtils.toInt(params.get(PARAM_USER_ID)));
//        req.setHandleFee(NumberUtils.toInt(params.get(PARAM_HANDLE_FEE)));
//        req.setPayId(params.get(PARAM_PAY_ID));
//        req.setRefundAmt(NumberUtils.toInt(params.get(PARAM_REFUND_AMT)));
//        req.setRefundId(params.get(PARAM_REFUND_ID));
//        req.setClientIp(params.get(PARAM_CLIENT_IP));
//        req.setRemark(params.get(PARAM_REFUND_REASON));
//        Result<Void> _result = SpringBeanUtils.getBean(IPurseService.class).refund(req);
//        if (_result.isSuccess()) {
//            RefundApplyResult rs = new RefundApplyResult();
//            rs.setRefundId(params.get(PARAM_REFUND_ID));
//            rs.setTradeTime(new Date());
//            rs.setStatus(RefundStatus.SUCCESS);
//            rs.setThirdRefundNo(null);
//            return new Result<>(_result.getCode(), _result.getMsg(), rs);
//        } else {
//            return new Result<>(_result.getCode(), _result.getMsg());
//        }
//    }
//
//    @Override
//    public Result<PayQueryResult> payQuery(Map<String, String> params) {
//        String payId = params.get(PARAM_PAY_ID);
//        Result<PayQueryResponse> result = SpringBeanUtils.getBean(IPurseService.class).queryPay(payId);
//        if (result.isSuccess()) {
//            PayQueryResult rs = new PayQueryResult();
//            BeanUtils.copyProperties(result.getResult(), rs);
//            return Result.success(rs);
//        } else {
//            return Result.failure(ResultCode.PAY_NOT_FOUND.getCode(), result.getMsg());
//        }
//    }
//
//    @Override
//    public Result<RefundQueryResult> refundQuery(Map<String, String> params) {
//        String refundId = params.get(PARAM_REFUND_ID);
//        Result<RefundQueryResponse> result = SpringBeanUtils.getBean(IPurseService.class).queryRefund(refundId);
//        if (result.isSuccess()) {
//            RefundQueryResult rs = new RefundQueryResult();
//            rs.setOrderAmt(result.getResult().getOrderAmt());
//            rs.setPayId(result.getResult().getPayId());
//            List<RefundDetail> details = new ArrayList<>();
//            for (RefundItem item : result.getResult().getItems()) {
//                RefundDetail detail = new RefundDetail();
//                detail.setRefundAmt(item.getRefundAmt());
//                detail.setRefundId(refundId);
//                detail.setStatus(item.getStatus());
//                detail.setTradeTime(item.getTradeTime());
//                details.add(detail);
//            }
//            rs.setDetails(details);
//            return Result.success(rs);
//        } else {
//            return Result.failure(ResultCode.REFUND_NOT_FOUND.getCode(), result.getMsg());
//        }
//    }
//
//    @Override
//    protected Map<String, String> transformPayParams(Map<String, String> params) {
//        throw new UnsupportedOperationException();
//    }
//
//    public static void main(String[] args) {
//        PayQueryResponse resp = new PayQueryResponse();
//        resp.setPayId("213123");
//        resp.setTradeTime(new Date());
//        PayQueryResult rs = new PayQueryResult();
//        BeanUtils.copyProperties(resp, rs);
//
//        System.out.println(rs.getPayId());
//    }
//
//}
