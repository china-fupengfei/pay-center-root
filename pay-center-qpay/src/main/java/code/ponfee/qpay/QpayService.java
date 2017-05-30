package code.ponfee.qpay;

import static code.ponfee.pay.common.Constants.DEFAULT_DATE_FORMAT;
import static code.ponfee.pay.common.Constants.PARAM_EXPIRE_TIME;
import static code.ponfee.qpay.QpayConfig.appKey;
import static code.ponfee.qpay.QpayConfig.appid;
import static code.ponfee.qpay.QpayConfig.mchId;
import static code.ponfee.qpay.QpayConfig.md5Key;
import static code.ponfee.qpay.QpayConfig.opUserId;
import static code.ponfee.qpay.QpayConfig.opUserPwd;
import static code.ponfee.qpay.QpayConfig.sslContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import code.ponfee.commons.json.Jsons;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.util.Dates;
import code.ponfee.pay.common.Constants;
import code.ponfee.pay.common.PayResultCode;
import code.ponfee.pay.dto.PayApplyResult;
import code.ponfee.pay.dto.PayNotifyResult;
import code.ponfee.pay.dto.PayQueryResult;
import code.ponfee.pay.dto.PayReturnResult;
import code.ponfee.pay.dto.RefundApplyResult;
import code.ponfee.pay.dto.RefundNotifyResult;
import code.ponfee.pay.dto.RefundQueryResult;
import code.ponfee.pay.dto.RefundQueryResult.RefundDetail;
import code.ponfee.pay.enums.ChannelType;
import code.ponfee.pay.enums.PaymentStatus;
import code.ponfee.pay.enums.RefundStatus;
import code.ponfee.pay.service.PayServiceAdapter;
import code.ponfee.qpay.core.Qpay;
import code.ponfee.qpay.core.QpayBuilder;
import code.ponfee.qpay.model.QpayFields;
import code.ponfee.qpay.model.pay.PayQueryRequest;
import code.ponfee.qpay.model.pay.PayQueryResponse;
import code.ponfee.qpay.model.pay.PrepayRequest;
import code.ponfee.qpay.model.refund.RefundApplyRequest;
import code.ponfee.qpay.model.refund.RefundApplyResponse;
import code.ponfee.qpay.model.refund.RefundQueryRequest;
import code.ponfee.qpay.model.refund.RefundQueryResponse;
import code.ponfee.qpay.model.refund.RefundQueryResponse.RefundItem;

/**
 * QQ钱包支付
 * @author fupf
 */
public class QpayService extends PayServiceAdapter {
    private final Qpay qpay;

    public QpayService() {
        this.qpay = QpayBuilder.newBuilder(appid, mchId, md5Key, appKey, sslContext, opUserId, opUserPwd).build();
    }

    /**
     * 支付
     */
    public @Override Result<PayApplyResult> pay(Map<String, String> params) {
        ChannelType channel = toChannel(params.get(Constants.PARAM_CHANNEL_TYPE), "QPAY");
        String json = Jsons.NORMAL.stringify(transformPayParams(params));
        PrepayRequest req = Jsons.NORMAL.parse(json, PrepayRequest.class);
        String resp;
        switch (channel) {
            case QPAY_NATIVE:
                resp = qpay.pays().nativePay(req);
                break;
            case QPAY_APP:
                resp = qpay.pays().appPay(req);
                break;
            case QPAY_JSAPI:
                resp = qpay.pays().nativePay(req);
                break;
            default:
                throw new UnsupportedOperationException("unsupport pay channel " + channel);
        }
        PayApplyResult result = new PayApplyResult();
        result.setChannelType(params.get(Constants.PARAM_CHANNEL_TYPE));
        result.setPayId(req.getOutTradeNo());
        result.setResponse(resp);
        return Result.success(result);
    }

    /**
     * 同步跳转
     */
    public @Override Result<PayReturnResult> payReturn(Map<String, String> params) {
        throw new UnsupportedOperationException("qpay unsupported frontend return.");
    }

    /**
     * 异步通知
     */
    @SuppressWarnings("unused")
    public @Override Result<PayNotifyResult> payNotify(Map<String, String> params) {
        String channelType = params.remove(Constants.PARAM_CHANNEL_TYPE);
        PayNotifyResult resp = new PayNotifyResult();
        if (qpay.notifies().verifySign(params)) { // 验证签名
            resp.setPayId(params.get(QpayFields.OUT_TRADE_NO));

            // 解析支付状态
            if ("Success".equals(params.get(QpayFields.TRADE_STATE))) { // 支付成功
                resp.setOrderAmt(Integer.parseInt(params.get(QpayFields.TOTAL_FEE)));
                resp.setThirdTradeNo(params.get(QpayFields.TRANSACTION_ID));
                String tradeTime = params.get(QpayFields.TIME_END);
                if (StringUtils.isNotBlank(tradeTime)) {
                    resp.setTradeTime(Dates.toDate(tradeTime, DEFAULT_DATE_FORMAT));
                }
                resp.setStatus(PaymentStatus.SUCCESS);
                resp.setAttachData(params.get(QpayFields.ATTACH));
                resp.setResponse(qpay.notifies().success());
            } else { // 支付失败
                resp.setStatus(PaymentStatus.FAILURE);
                resp.setResponse(qpay.notifies().fail("支付失败"));
            }
            return Result.success(resp);
        } else {
            return Result.failure(PayResultCode.PARAMS_VERIFY_FAILURE.getCode(), qpay.notifies().fail("签名失败"));
        }
    }

    /**
     * 退款
     */
    @SuppressWarnings("unused")
    public @Override Result<RefundApplyResult> refund(Map<String, String> params) {
        String channelType = params.remove(Constants.PARAM_CHANNEL_TYPE);

        try {
            RefundApplyRequest req = new RefundApplyRequest();
            req.setOutTradeNo(params.get(Constants.PARAM_PAY_ID));
            req.setOutRefundNo(params.get(Constants.PARAM_REFUND_ID));
            req.setRefundFee(Integer.parseInt(params.get(Constants.PARAM_REFUND_AMT)));
            req.setTransactionId(params.get(Constants.PARAM_THIRD_TRADE_NO));
            RefundApplyResponse resp = qpay.refunds().apply(req);

            RefundApplyResult result = new RefundApplyResult();
            result.setThirdTradeNo(resp.getTransactionId());
            result.setRefundId(resp.getOutRefundNo());
            result.setThirdRefundNo(resp.getRefundId());
            result.setStatus(RefundStatus.SUCCESS);
            return Result.success(result);
        } catch (Exception e) {
            return Result.failure(PayResultCode.REFUND_TRADE_FAILURE.getCode(), e.getMessage());
        }
    }

    public @Override Result<RefundNotifyResult> refundNotify(Map<String, String> params) {
        throw new UnsupportedOperationException("qpay unsupported refund notify.");
    }

    @SuppressWarnings("unused")
    public @Override Result<PayQueryResult> payQuery(Map<String, String> params) {
        String channelType = params.remove(Constants.PARAM_CHANNEL_TYPE);
        PayQueryRequest req = new PayQueryRequest();
        req.setOutTradeNo(params.get(Constants.PARAM_PAY_ID));
        req.setTransactionId(params.get(Constants.PARAM_THIRD_TRADE_NO));
        PayQueryResponse resp = qpay.pays().query(req);

        PayQueryResult result = new PayQueryResult();
        result.setPayId(resp.getOutTradeNo());
        result.setThirdTradeNo(resp.getTransactionId());
        result.setOrderAmt(resp.getTotalFee());
        result.setTradeTime(resp.getTimeEnd());
        switch (resp.getTradeState()) {
            case SUCCESS:
            case REFUND:
                result.setStatus(PaymentStatus.SUCCESS);
                break;
            case CLOSED:
                result.setStatus(PaymentStatus.CLOSED);
                break;
            case USERPAYING:
                result.setStatus(PaymentStatus.PENDING);
                break;
            default:
                result.setStatus(PaymentStatus.FAILURE);
                break;
        }
        return Result.success(result);
    }

    public @Override Result<RefundQueryResult> refundQuery(Map<String, String> params) {
        RefundQueryRequest req = new RefundQueryRequest();
        req.setOutTradeNo(params.get(Constants.PARAM_PAY_ID));
        req.setOutRefundNo(params.get(Constants.PARAM_REFUND_ID));
        req.setTransactionId(params.get(Constants.PARAM_THIRD_TRADE_NO));
        req.setRefundId(params.get(Constants.PARAM_THIRD_REFUND_NO));
        RefundQueryResponse resp = qpay.refunds().query(req);

        // 组装退款结果数据
        RefundQueryResult result = new RefundQueryResult();
        result.setPayId(resp.getOutTradeNo());
        result.setThirdTradeNo(resp.getTransactionId());
        result.setOrderAmt(resp.getTotalFee());

        // 组装退款明细数据
        List<RefundDetail> details = new ArrayList<>();
        for (RefundItem item : resp.getRefundItems()) {
            RefundDetail detail = new RefundDetail();
            detail.setRefundAmt(item.getRefundFee());
            detail.setRefundId(item.getOutRefundNo());
            detail.setThirdRefundNo(item.getRefundId());
            switch (item.getRefundStatus()) {
                case PROCESSING:
                    detail.setStatus(RefundStatus.HANDLING);
                    break;
                case SUCCESS:
                    detail.setStatus(RefundStatus.SUCCESS);
                    break;
                default:
                    detail.setStatus(RefundStatus.FAILURE);
                    break;
            }
            details.add(detail);
        }
        result.setDetails(details);

        return Result.success(result);
    }

    @Override
    protected Map<String, String> transformPayParams(Map<String, String> params) {
        Date start = new Date();
        Date end = Dates.toDate(params.get(PARAM_EXPIRE_TIME), DEFAULT_DATE_FORMAT);

        Map<String, String> map = new HashMap<>();
        map.put(QpayFields.NOTIFY_URL, params.get(Constants.PARAM_PAYMENT_NOTIFY_URL));
        map.put(QpayFields.OUT_TRADE_NO, params.get(Constants.PARAM_PAY_ID));
        map.put(QpayFields.TOTAL_FEE, params.get(Constants.PARAM_ORDER_AMT));
        map.put(QpayFields.SPBILL_CREATE_IP, params.get(Constants.PARAM_CLIENT_IP));
        map.put(QpayFields.BODY, params.get(Constants.PARAM_GOODS_BODY));
        map.put(QpayFields.TIME_START, Dates.format(start, DEFAULT_DATE_FORMAT));
        map.put(QpayFields.TIME_EXPIRE, Dates.format(end, DEFAULT_DATE_FORMAT));
        map.put(QpayFields.DEVICE_INFO, params.get(QpayFields.DEVICE_INFO));
        map.put(QpayFields.ATTACH, params.get(Constants.PARAM_ATTACH_DATA));
        return map;
    }

}
