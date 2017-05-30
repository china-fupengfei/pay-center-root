package code.ponfee.unionpay;

import static code.ponfee.pay.common.Constants.DEFAULT_DATE_FORMAT;
import static code.ponfee.pay.common.Constants.PARAM_EXPIRE_TIME;
import static code.ponfee.unionpay.UnionpayConfig.appMd5Key;
import static code.ponfee.unionpay.UnionpayConfig.appMerId;
import static code.ponfee.unionpay.UnionpayConfig.charset;
import static code.ponfee.unionpay.UnionpayConfig.merAbbr;
import static code.ponfee.unionpay.UnionpayConfig.webMd5Key;
import static code.ponfee.unionpay.UnionpayConfig.webMerId;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
import code.ponfee.unionpay.core.Component;
import code.ponfee.unionpay.core.UnionpayBuilder;
import code.ponfee.unionpay.model.UnionpayField;
import code.ponfee.unionpay.model.order.OrderQueryRequest;
import code.ponfee.unionpay.model.order.OrderQueryResponse;
import code.ponfee.unionpay.model.pay.AppPayRequest;
import code.ponfee.unionpay.model.pay.WebPayRequest;
import code.ponfee.unionpay.model.refund.RefundRequest;
import code.ponfee.unionpay.model.refund.RefundResponse;

/**
 * 银联支付服务类
 * @author fupf
 */
public class UnionpayService extends PayServiceAdapter {
    private final Component appPay; // app支付
    private final Component webPay; // web支付

    public UnionpayService() {
        webPay = UnionpayBuilder.newBuilder(webMerId, webMd5Key, false).charset(charset).build().pay();
        appPay = UnionpayBuilder.newBuilder(appMerId, appMd5Key, true).charset(charset).build().pay();
    }

    // ----------------------支付--------------------
    /**
     * <pre>
     *  {
     *    bizType=0,
     *    channelType=UNIONPAY_WEB,
     *    clientIp=183.16.198.10,
     *    orderAmt=1,
     *    orderNo=bizOrderNo,
     *    orderType=1,
     *    payId=TP6015006706760,
     *    paymentNotifyUrl=http://ba62942c.ngrok.io/pay-center-demo/payment/notify?channelType=UNIONPAY_WEB,
     *    paymentReturnUrl=http://ba62942c.ngrok.io/pay-center-demo/payment/return?channelType=UNIONPAY_WEB,
     *    remark=remark001,
     *    source=IOS,
     *    userId=1
     *  }
     * </pre>
     */
    @Override
    public Result<PayApplyResult> pay(Map<String, String> params) {
        ChannelType channel = super.toChannel(params.get(Constants.PARAM_CHANNEL_TYPE), "UNIONPAY");
        String json = Jsons.NORMAL.stringify(transformPayParams(params));
        switch (channel) {
            case UNIONPAY_WEB:
                WebPayRequest webReq = Jsons.NORMAL.parse(json, WebPayRequest.class);
                return Result.success(new PayApplyResult(webPay.webPay(webReq)));
            case UNIONPAY_APP:
                AppPayRequest appReq = Jsons.NORMAL.parse(json, AppPayRequest.class);
                return Result.success(new PayApplyResult(appPay.appPay(appReq)));
            default:
                return Result.failure(PayResultCode.ILLEGAL_CHANNEL_TYPE.getCode(), "illegal ChannelType " + channel.name());
        }
    }

    /**
     * <pre>
     *  <b>UNIONPAY_WEB</b>
     *  {
     *    channelType=UNIONPAY_WEB,
     *    charset=UTF-8,
     *    cupReserved=,
     *    exchangeDate=,
     *    exchangeRate=,
     *    merAbbr=深圳市一二三零八网络科技有限公司,
     *    merId=898111141120101,
     *    orderAmount=1,
     *    orderCurrency=156,
     *    orderNumber=TP5573109117045,
     *    qid=201608131218040313918,
     *    respCode=00,
     *    respMsg=success,
     *    respTime=20160813122159,
     *    settleAmount=1,
     *    settleCurrency=156,
     *    settleDate=0813,
     *    signMethod=MD5,
     *    signature=c7cc8cd4256f3509a598398677b86d54,
     *    traceNumber=031391,
     *    traceTime=0813121804,
     *    transType=01,
     *    version=1.0.0
     *  }
     *  
     *  <b>UNIONPAY_APP</b>
     *  {
     *    "channelType":"UNIONPAY_APP",
     *    "charset":"UTF-8",
     *    "exchangeRate":"0",
     *    "merId":"898111148990735",
     *    "orderNumber":"TP9934344796340120",
     *    "orderTime":"20160920101742",
     *    "qn":"201609201017429807848",
     *    "respCode":"00",
     *    "settleAmount":"300",
     *    "settleCurrency":"156",
     *    "settleDate":"0920",
     *    "signMethod":"MD5",
     *    "signature":"bf37b692e2b01af4caa757b774704804",
     *    "sysReserved":"{traceTime=0920101742&traceNumber=980784&acqCode=48021000}",
     *    "transStatus":"00",
     *    "transType":"01",
     *    "version":"1.0.0"
     *  }
     * </pre>
     */
    @Override
    public Result<PayReturnResult> payReturn(Map<String, String> params) {
        String channelType = params.get(Constants.PARAM_CHANNEL_TYPE);
        params.remove(Constants.PARAM_CHANNEL_TYPE);
        PayReturnResult result = new PayReturnResult();
        if (getPay(channelType).verifySign(params)) {
            result.setPayId(params.get(UnionpayField.ORDER_NUMBER));
            String tradeTime;
            switch (toChannel(channelType, "UNIONPAY")) {
                case UNIONPAY_WEB:
                    result.setStatus(PaymentStatus.SUCCESS);
                    result.setOrderAmt(Integer.parseInt(params.get(UnionpayField.ORDER_AMOUNT)));
                    result.setThirdTradeNo(params.get(UnionpayField.QID));
                    tradeTime = params.get(UnionpayField.RESP_TIME);
                    break;
                case UNIONPAY_APP:
                    if ("00".equals(params.get(UnionpayField.APP_TRANS_STATUS))) {
                        result.setStatus(PaymentStatus.SUCCESS);
                    } else {
                        result.setStatus(PaymentStatus.FAILURE);
                    }
                    result.setOrderAmt(Integer.parseInt(params.get(UnionpayField.SETTLE_AMOUNT)));
                    result.setThirdTradeNo(params.get(UnionpayField.APP_QN));
                    tradeTime = params.get(UnionpayField.ORDER_TIME);
                    //result.setAttachData(params.get(UnionpayField.REQ_RESERVED));
                    break;
                default:
                    throw new UnsupportedOperationException("unsupport " + channelType);
            }
            if (StringUtils.isNotBlank(tradeTime)) {
                result.setTradeTime(Dates.toDate(tradeTime, "yyyyMMddHHmmss"));
            }
            return Result.success(result);
        } else {
            return Result.failure(PayResultCode.PARAMS_VERIFY_FAILURE);
        }
    }

    /**
     * <pre>
     *  <b>UNIONPAY_WEB</b>
     *  {
     *    channelType=UNIONPAY_WEB,
     *    charset=UTF-8,
     *    cupReserved=,
     *    exchangeDate=,
     *    exchangeRate=,
     *    merAbbr=深圳市一二三零八网络科技有限公司,
     *    merId=898111141120101,
     *    orderAmount=1,
     *    orderCurrency=156,
     *    orderNumber=TP5573109117045,
     *    qid=201608131218040313918,
     *    respCode=00,
     *    respMsg=Success!,
     *    respTime=20160813122159,
     *    settleAmount=1,
     *    settleCurrency=156,
     *    settleDate=0813,
     *    signMethod=MD5,
     *    signature=b74de160d4c89b3258b856723f140b85,
     *    traceNumber=031391,
     *    traceTime=0813121804,
     *    transType=01,
     *    version=1.0.0
     *  }
     *  
     *  <b>UNIONPAY_APP</b>
     *  {
     *    "channelType":"UNIONPAY_APP",
     *    "charset":"UTF-8",
     *    "exchangeRate":"0",
     *    "merId":"898111148990735",
     *    "orderNumber":"TP9934344796340120",
     *    "orderTime":"20160920101742",
     *    "qn":"201609201017429807848",
     *    "respCode":"00",
     *    "settleAmount":"300",
     *    "settleCurrency":"156",
     *    "settleDate":"0920",
     *    "signMethod":"MD5",
     *    "signature":"bf37b692e2b01af4caa757b774704804",
     *    "sysReserved":"{traceTime=0920101742&traceNumber=980784&acqCode=48021000}",
     *    "transStatus":"00",
     *    "transType":"01",
     *    "version":"1.0.0"
     *  }
     * </pre>
     */
    @Override
    public Result<PayNotifyResult> payNotify(Map<String, String> params) {
        String channelType = params.get(Constants.PARAM_CHANNEL_TYPE);
        params.remove(Constants.PARAM_CHANNEL_TYPE);
        PayNotifyResult result = new PayNotifyResult();
        if (getPay(channelType).verifySign(params)) {
            result.setPayId(params.get(UnionpayField.ORDER_NUMBER));
            String tradeTime;
            switch (toChannel(channelType, "UNIONPAY")) {
                case UNIONPAY_WEB:
                    result.setStatus(PaymentStatus.SUCCESS);
                    result.setResponse("success");
                    result.setOrderAmt(Integer.parseInt(params.get(UnionpayField.ORDER_AMOUNT)));
                    result.setThirdTradeNo(params.get(UnionpayField.QID));
                    tradeTime = params.get(UnionpayField.RESP_TIME);
                    break;
                case UNIONPAY_APP:
                    if ("00".equals(params.get(UnionpayField.APP_TRANS_STATUS))) {
                        result.setStatus(PaymentStatus.SUCCESS);
                        result.setResponse("success");
                    } else {
                        result.setStatus(PaymentStatus.FAILURE);
                        result.setResponse("fail");
                    }
                    result.setOrderAmt(Integer.parseInt(params.get(UnionpayField.SETTLE_AMOUNT)));
                    result.setThirdTradeNo(params.get(UnionpayField.APP_QN));
                    tradeTime = params.get(UnionpayField.ORDER_TIME);
                    //result.setAttachData(params.get(UnionpayField.REQ_RESERVED));
                    break;
                default:
                    throw new UnsupportedOperationException("unsupport " + channelType);
            }
            if (StringUtils.isNotBlank(tradeTime)) {
                result.setTradeTime(Dates.toDate(tradeTime, "yyyyMMddHHmmss"));
            }
            return Result.success(result);
        } else {
            return Result.failure(PayResultCode.PARAMS_VERIFY_FAILURE);
        }
    }

    // -----------------------退款------------------------
    /**
     * <pre>
     *  {
     *    channelType=UNIONPAY_WEB,
     *    createTime=20160813121758,
     *    orderAmt=1,
     *    payId=TP5573109117045,
     *    refundAmt=1,
     *    refundId=201608136471554040835,
     *    refundNo=bizRefundNo,
     *    refundNotifyUrl=http://ba62942c.ngrok.io/pay-center-demo/refund/notify?channelType=UNIONPAY_WEB,
     *    refundRate=100,
     *    refundReason=退款原因,
     *    refundType=1,
     *    remark=refund remark,
     *    thirdTradeNo=201608131218040313918
     *  }
     * </pre>
     */
    @Override
    public Result<RefundApplyResult> refund(Map<String, String> params) {
        // 请求参数
        RefundRequest req = new RefundRequest();
        req.setOrderNumber(params.get(Constants.PARAM_REFUND_ID));
        req.setOrderAmount(Integer.parseInt(params.get(Constants.PARAM_REFUND_AMT)));
        req.setOrigQid(params.get(Constants.PARAM_THIRD_TRADE_NO));
        req.setBackEndUrl(params.get(Constants.PARAM_REFUND_NOTIFY_URL));

        // 退款请求
        RefundResponse resp = getPay(params.get(Constants.PARAM_CHANNEL_TYPE)).refund(req);

        // 返回数据
        RefundApplyResult result = new RefundApplyResult();
        result.setRefundId(params.get(Constants.PARAM_REFUND_ID));
        result.setThirdRefundNo(resp.getQid());
        result.setStatus(RefundStatus.HANDLING);
        return Result.success(result);
    }

    /**
     * <pre>
     *  <b>UNIONPAY_WEB</b>
     *  {
     *    "channelType": "UNIONPAY_WEB",
     *    "charset": "UTF-8",
     *    "cupReserved": "",
     *    "exchangeDate": "",
     *    "exchangeRate": "",
     *    "merAbbr": "深圳市一二三零八网络科技有限公司",
     *    "merId": "898111141120101",
     *    "orderAmount": "1",
     *    "orderCurrency": "156",
     *    "orderNumber": "201608131530378827",
     *    "qid": "201608131530370726708",
     *    "respCode": "00",
     *    "respMsg": "Success!",
     *    "respTime": "20160813153138",
     *    "settleAmount": "1",
     *    "settleCurrency": "156",
     *    "settleDate": "0813",
     *    "signMethod": "MD5",
     *    "signature": "824d0e0ceb5cbacea0b997db67a62873",
     *    "traceNumber": "072670",
     *    "traceTime": "0813153037",
     *    "transType": "04",
     *    "version": "1.0.0"
     *  }
     *  
     *  <b>UNIONPAY_APP</b>
     *  {
     *    "channelType":"UNIONPAY_APP",
     *    "charset":"UTF-8",
     *    "exchangeRate":"0",
     *    "merId":"898111148990735",
     *    "orderNumber":"201609209935692086066993",
     *    "orderTime":"20160920104009",
     *    "qn":"201609201040098833108",
     *    "respCode":"00",
     *    "settleAmount":"100",
     *    "settleCurrency":"156",
     *    "settleDate":"0920",
     *    "signMethod":"MD5",
     *    "signature":"83e2796d3a5008fdb029ad7721175dcc",
     *    "sysReserved":"{traceTime=0920104009&traceNumber=883310&acqCode=48021000}",
     *    "transStatus":"00",
     *    "transType":"04",
     *    "version":"1.0.0"
     *  }
     * </pre>
     * 
     * 退款异步通知
     */
    @Override
    public Result<RefundNotifyResult> refundNotify(Map<String, String> params) {
        String channel = params.get(Constants.PARAM_CHANNEL_TYPE);
        params.remove(Constants.PARAM_CHANNEL_TYPE);
        Component pay = getPay(channel);
        if (!pay.verifySign(params)) {
            return Result.failure(PayResultCode.PARAMS_VERIFY_FAILURE);
        }

        RefundNotifyResult result = new RefundNotifyResult();
        result.setRefundId(params.get(UnionpayField.ORDER_NUMBER));
        String tradeTime;
        switch (super.toChannel(channel, "UNIONPAY")) {
            case UNIONPAY_WEB:
                result.setThirdRefundNo(params.get(UnionpayField.QID));
                result.setRefundAmt(Integer.parseInt(params.get(UnionpayField.ORDER_AMOUNT)));
                tradeTime = params.get(UnionpayField.RESP_TIME);
                if ("00".equals(params.get(UnionpayField.RESP_CODE))) {
                    result.setResponse("success");
                    result.setStatus(RefundStatus.SUCCESS);
                } else {
                    result.setResponse("fail");
                    result.setStatus(RefundStatus.FAILURE);
                }
                break;
            case UNIONPAY_APP:
                result.setThirdRefundNo(params.get(UnionpayField.APP_QN));
                result.setRefundAmt(Integer.parseInt(params.get(UnionpayField.SETTLE_AMOUNT)));
                tradeTime = params.get(UnionpayField.ORDER_TIME);
                if ("00".equals(params.get("transStatus"))) {
                    result.setResponse("success");
                    result.setStatus(RefundStatus.SUCCESS);
                } else {
                    result.setResponse("fail");
                    result.setStatus(RefundStatus.FAILURE);
                }
                break;
            default:
                return Result.failure(PayResultCode.ILLEGAL_CHANNEL_TYPE.getCode(), "illegal ChannelType " + channel);
        }
        result.setTradeTime(Dates.toDate(tradeTime, "yyyyMMddHHmmss"));
        return Result.success(result);
    }

    // -----------------------交易查询------------------------
    @Override
    public Result<PayQueryResult> payQuery(Map<String, String> params) {
        String payId = params.get(Constants.PARAM_PAY_ID);
        Component pay = getPay(params.get(Constants.PARAM_CHANNEL_TYPE));
        OrderQueryRequest request = new OrderQueryRequest(payId);
        String tradeTime = params.get(Constants.PARAM_TRADE_TM);
        if (StringUtils.isNotBlank(tradeTime)) {
            request.setOrderTime(Dates.toDate(tradeTime, "yyyyMMddHHmmss"));
        }
        OrderQueryResponse resp = pay.queryTrade(request);

        PayQueryResult result = new PayQueryResult();
        result.setPayId(payId);
        result.setThirdTradeNo(resp.getQid());
        result.setOrderAmt(Integer.parseInt(resp.getSettleAmount()));
        result.setTradeTime(resp.getRespTime());
        // 0成功；1失败；2处理中；3无此交易；
        if ("0".equals(resp.getQueryResult())) {
            result.setStatus(PaymentStatus.SUCCESS);
        } else if ("2".equals(resp.getQueryResult())) {
            result.setStatus(PaymentStatus.PENDING);
        } else {
            result.setStatus(PaymentStatus.FAILURE);
        }
        return Result.success(result);
    }

    @Override
    public Result<RefundQueryResult> refundQuery(Map<String, String> params) {
        String refundId = params.get(Constants.PARAM_REFUND_ID);
        Component pay = getPay(params.get(Constants.PARAM_CHANNEL_TYPE));
        OrderQueryRequest request = new OrderQueryRequest(refundId);
        String tradeTime = params.get(Constants.PARAM_TRADE_TM);
        if (StringUtils.isNotBlank(tradeTime)) {
            request.setOrderTime(Dates.toDate(tradeTime, "yyyyMMddHHmmss"));
        }
        OrderQueryResponse resp = pay.queryTrade(request);

        RefundDetail detail = new RefundDetail();
        detail.setRefundId(refundId);
        detail.setThirdRefundNo(resp.getQid());
        detail.setRefundAmt(Integer.parseInt(resp.getSettleAmount()));
        detail.setTradeTime(resp.getRespTime());
        // 0成功；1失败；2处理中；3无此交易；
        if ("0".equals(resp.getQueryResult())) {
            detail.setStatus(RefundStatus.SUCCESS);
        } else if ("2".equals(resp.getQueryResult())) {
            detail.setStatus(RefundStatus.HANDLING);
        } else {
            detail.setStatus(RefundStatus.FAILURE);
        }

        RefundQueryResult result = new RefundQueryResult();
        result.setPayId(params.get(Constants.PARAM_PAY_ID));
        result.setThirdTradeNo(params.get(Constants.PARAM_THIRD_TRADE_NO));
        result.setDetails(Arrays.asList(detail));
        return Result.success(result);
    }

    // -----------------------------private method--------------------------------
    @Override
    protected Map<String, String> transformPayParams(Map<String, String> params) {
        Map<String, String> map = new HashMap<>();
        Date now = new Date();
        Date end = Dates.toDate(params.get(PARAM_EXPIRE_TIME), DEFAULT_DATE_FORMAT);
        map.put(UnionpayField.ORDER_TIME, Dates.format(now, DEFAULT_DATE_FORMAT));

        map.put(UnionpayField.APP_ORDER_TIMEOUT, params.get(PARAM_EXPIRE_TIME));
        map.put(UnionpayField.WEB_TRANS_TIMEOUT, String.valueOf(Dates.clockdiff(now, end) * 1000));

        map.put(UnionpayField.FRONT_END_URL, params.get(Constants.PARAM_PAYMENT_RETURN_URL));
        map.put(UnionpayField.BACK_END_URL, params.get(Constants.PARAM_PAYMENT_NOTIFY_URL));
        map.put(UnionpayField.ORDER_NUMBER, params.get(Constants.PARAM_PAY_ID));
        map.put(UnionpayField.ORDER_AMOUNT, params.get(Constants.PARAM_ORDER_AMT));
        map.put(UnionpayField.COMMODITY_NAME, params.get(Constants.PARAM_GOODS_BODY));
        map.put(UnionpayField.COMMODITY_UNIT_PRICE, "1");
        map.put(UnionpayField.COMMODITY_QUANTITY, "1");
        map.put(UnionpayField.MER_ABBR, merAbbr);
        return map;
    }

    private Component getPay(String channelType) {
        ChannelType channel = super.toChannel(channelType, "UNIONPAY");
        switch (channel) {
            case UNIONPAY_APP:
                return appPay;
            case UNIONPAY_WEB:
                return webPay;
            default:
                return null;
        }
    }

    //////////////////////TEST//////////////////////////////
    private static void testPayQuery() {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.PARAM_CHANNEL_TYPE, "UNIONPAY_APP");
        map.put(Constants.PARAM_PAY_ID, "201609231121000010000214");
        //map.put(Constants.PARAM_THIRD_TRADE_NO, "201608241215275387728");
        map.put(Constants.PARAM_TRADE_TM, "20160923180812");
        System.out.println(Jsons.NORMAL.stringify(new UnionpayService().payQuery(map)));
    }

    @SuppressWarnings("unused")
    private static void testRefundQuery() {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.PARAM_CHANNEL_TYPE, "UNIONPAY_APP");
        map.put(Constants.PARAM_REFUND_ID, "201609109076972089898149");
        map.put(Constants.PARAM_TRADE_TM, "20160824121527");
        System.out.println(Jsons.NORMAL.stringify(new UnionpayService().refundQuery(map)));
    }

    public static void main(String[] args) {
        testPayQuery();
    }

}
