package code.ponfee.wechatpay;

import static code.ponfee.pay.common.Constants.DEFAULT_DATE_FORMAT;
import static code.ponfee.pay.common.Constants.PARAM_EXPIRE_TIME;

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
import code.ponfee.wechatpay.core.Wechatpay;
import code.ponfee.wechatpay.core.WechatpayBuilder;
import code.ponfee.wechatpay.model.common.WechatpayField;
import code.ponfee.wechatpay.model.order.WechatpayOrder;
import code.ponfee.wechatpay.model.pay.JsPayRequest;
import code.ponfee.wechatpay.model.pay.PayRequest;
import code.ponfee.wechatpay.model.refund.RefundApplyRequest;
import code.ponfee.wechatpay.model.refund.RefundApplyResponse;
import code.ponfee.wechatpay.model.refund.RefundItem;
import code.ponfee.wechatpay.model.refund.RefundQueryResponse;

/**
 * wechat pay service
 * @author fupf
 */
public class WechatpayService extends PayServiceAdapter {
    private static final String DATE_FORMAT = "yyyyMMddHHmmss";
    private final Wechatpay wepay;
    private final Wechatpay wepayJSAPI;

    public WechatpayService() {
        wepay = WechatpayBuilder.newBuilder(WechatpayConfig.appId, WechatpayConfig.appKey, WechatpayConfig.mchId, WechatpayConfig.sslContext).build();
        wepayJSAPI = WechatpayBuilder.newBuilder(WechatpayConfig.appIdJSAPI, WechatpayConfig.appKey, WechatpayConfig.mchId, WechatpayConfig.sslContext).build();
    }

    // -------------------------支付-----------------------
    /**
     * 支付
     * 
     * <pre>
     *  {
     *    bizType=0,
     *    channelType=WECHAT_NATIVE,
     *    clientIp=183.16.194.73,
     *    orderAmt=1,
     *    orderNo=bizOrderNo,
     *    orderType=1,
     *    payId=TP17884109611873,
     *    paymentNotifyUrl=http://6f2afefd.ngrok.io/pay-center-demo/payment/notify?channelType=WECHAT_NATIVE,
     *    paymentReturnUrl=http://6f2afefd.ngrok.io/pay-center-demo/payment/return?channelType=WECHAT_NATIVE,
     *    remark=remark001,
     *    source=IOS,
     *    userId=1
     *  }
     * </pre>
     */
    @Override
    public Result<PayApplyResult> pay(Map<String, String> params) {
        ChannelType channel = toChannel(params.get(Constants.PARAM_CHANNEL_TYPE), "WECHATPAY");
        params = transformPayParams(params);
        String json = Jsons.NORMAL.stringify(params);
        String resp;
        switch (channel) {
            case WECHAT_NATIVE:
                resp = wepay.pay().qrPay(Jsons.NORMAL.parse(json, PayRequest.class));
                break;
            case WECHAT_APP:
                resp = Jsons.NORMAL.stringify(wepay.pay().appPay(Jsons.NORMAL.parse(json, PayRequest.class)));
                break;
            case WECHAT_WAP:
                resp = wepay.pay().wapPay(Jsons.NORMAL.parse(json, PayRequest.class));
                break;
            case WECHAT_MWEB:
                resp = wepay.pay().mwebPay(Jsons.NORMAL.parse(json, PayRequest.class));
                break;
            case WECHAT_JSAPI:
                resp = Jsons.NORMAL.stringify(wepayJSAPI.pay().jsPay(Jsons.NORMAL.parse(json, JsPayRequest.class)));
                break;
            default:
                return Result.failure(PayResultCode.ILLEGAL_CHANNEL_TYPE.getCode(), "illegal ChannelType " + channel.name());
        }
        return Result.success(new PayApplyResult(resp));
    }

    @Override
    public Result<PayReturnResult> payReturn(Map<String, String> params) {
        throw new UnsupportedOperationException("wechat pay unsupported frontend return.");
    }

    /**
     * 支付异步通知
     * 
     * <pre>
     *   <xml>
     *     <appid>wxc9847938780ba8d1</appid>
     *     <bank_type>CFT</bank_type>
     *     <cash_fee>1</cash_fee>
     *     <channelType>WECHAT_NATIVE</channelType>
     *     <fee_type>CNY</fee_type>
     *     <is_subscribe>N</is_subscribe>
     *     <mch_id>1220996701</mch_id>
     *     <nonce_str>EvXhBNopw16wscQy</nonce_str>
     *     <openid>oK8yRt3dEbvpTd76-mRkPmkXqlVA</openid>
     *     <out_trade_no>TP16420943715646</out_trade_no>
     *     <result_code>SUCCESS</result_code>
     *     <return_code>SUCCESS</return_code>
     *     <sign>509F2F7724406088B8299791DA88E9BF</sign>
     *     <time_end>20160811140240</time_end>
     *     <total_fee>1</total_fee>
     *     <trade_type>NATIVE</trade_type>
     *     <transaction_id>4006702001201608111092886741</transaction_id>
     *   </xml>
     * </pre>
     */
    @Override
    public Result<PayNotifyResult> payNotify(Map<String, String> params) {
        Wechatpay pay = getPay(params.get(Constants.PARAM_CHANNEL_TYPE));
        params.remove(Constants.PARAM_CHANNEL_TYPE);
        PayNotifyResult resp = new PayNotifyResult();
        // 验证签名
        if (pay.notifies().verifySign(params)) {
            resp.setPayId(params.get(WechatpayField.OUT_TRADE_NO));

            // 解析支付状态
            if ("SUCCESS".equals(params.get(WechatpayField.RESULT_CODE))) {
                // 支付成功
                resp.setOrderAmt(Integer.parseInt(params.get(WechatpayField.TOTAL_FEE)));
                resp.setThirdTradeNo(params.get(WechatpayField.TRANSACTION_ID));
                String tradeTime = params.get(WechatpayField.TIME_END);
                if (StringUtils.isNotBlank(tradeTime)) {
                    resp.setTradeTime(Dates.toDate(tradeTime, DATE_FORMAT));
                }
                resp.setStatus(PaymentStatus.SUCCESS);
                resp.setAttachData(params.get(WechatpayField.ATTACH));
                resp.setResponse(pay.notifies().success());
            } else {
                // 支付失败
                resp.setStatus(PaymentStatus.FAILURE);
                resp.setResponse(pay.notifies().fail("支付失败"));
            }
            return Result.success(resp);
        } else {
            return Result.failure(PayResultCode.PARAMS_VERIFY_FAILURE.getCode(), pay.notifies().fail("签名失败"));
        }
    }

    // -----------------------退款---------------------
    /**
     * <pre>
     *  {
     *    channelType=WECHAT_NATIVE,
     *    createTime=20160811144320,
     *    orderAmt=1,
     *    payId=TP19070072473698,
     *    refundAmt=1,
     *    refundId=2016081119168358698184,
     *    refundNo=bizRefundNo,
     *    refundNotifyUrl=http://6f2afefd.ngrok.io/pay-center-demo/refund/notify?channelType=WECHAT_NATIVE,
     *    refundRate=100,
     *    refundReason=退款原因,
     *    refundType=1,
     *    remark=refund remark,
     *    thirdTradeNo=4006702001201608111096408225
     *  }
     * </pre>
     */
    @Override
    public Result<RefundApplyResult> refund(Map<String, String> params) {
        Wechatpay pay = getPay(params.get(Constants.PARAM_CHANNEL_TYPE));
        try {
            RefundApplyRequest req = new RefundApplyRequest();
            req.setOutTradeNo(params.get(Constants.PARAM_PAY_ID));
            req.setOutRefundNo(params.get(Constants.PARAM_REFUND_ID));
            req.setTotalFee(Integer.parseInt(params.get(Constants.PARAM_ORDER_AMT)));
            req.setRefundFee(Integer.parseInt(params.get(Constants.PARAM_REFUND_AMT)));
            RefundApplyResponse resp = pay.refund().apply(req);

            RefundApplyResult result = new RefundApplyResult();
            result.setRefundId(resp.getOutRefundNo());
            result.setThirdRefundNo(resp.getRefundId());
            result.setStatus(RefundStatus.SUCCESS);
            return Result.success(result);
        } catch (Exception e) {
            return Result.failure(PayResultCode.REFUND_TRADE_FAILURE.getCode(), e.getMessage());
        }
    }

    @Override
    public Result<RefundNotifyResult> refundNotify(Map<String, String> params) {
        throw new UnsupportedOperationException("wechat pay unsupported refund notify.");
    }

    // ---------------------------------订单查询----------------------------------
    @Override
    public Result<PayQueryResult> payQuery(Map<String, String> params) {
        Wechatpay pay = getPay(params.get(Constants.PARAM_CHANNEL_TYPE));
        WechatpayOrder order = pay.order().queryOrder(params.get(Constants.PARAM_PAY_ID), params.get(Constants.PARAM_THIRD_TRADE_NO));
        PayQueryResult result = new PayQueryResult();
        result.setPayId(order.getOutTradeNo());
        result.setThirdTradeNo(order.getTransactionId());
        if (order.getTotalFee() != null) {
            result.setOrderAmt(order.getTotalFee());
        }
        result.setTradeTime(order.getTimeEnd());
        switch (order.getTradeState()) {
            case SUCCESS:
            case REFUND:
                result.setStatus(PaymentStatus.SUCCESS);
                break;
            case CLOSED:
                result.setStatus(PaymentStatus.CLOSED);
                break;
            default:
                result.setStatus(PaymentStatus.FAILURE);
                break;
        }
        return Result.success(result);
    }

    @Override
    public Result<RefundQueryResult> refundQuery(Map<String, String> params) {
        Wechatpay pay = getPay(params.get(Constants.PARAM_CHANNEL_TYPE));
        Map<String, String> map = new HashMap<>();
        map.put(WechatpayField.OUT_TRADE_NO, params.get(Constants.PARAM_PAY_ID));
        map.put(WechatpayField.TRANSACTION_ID, params.get(Constants.PARAM_THIRD_TRADE_NO));
        map.put(WechatpayField.OUT_REFUND_NO, params.get(Constants.PARAM_REFUND_ID));
        map.put(WechatpayField.REFUND_ID, params.get(Constants.PARAM_THIRD_REFUND_NO));
        RefundQueryResponse resp = pay.refund().queryRefund(map);

        RefundQueryResult result = new RefundQueryResult();
        result.setPayId(resp.getOutTradeNo());
        result.setThirdTradeNo(resp.getTransactionId());
        result.setOrderAmt(resp.getTotalFee());

        List<RefundDetail> details = new ArrayList<>();
        if (resp.getItems() != null) {
            for (RefundItem item : resp.getItems()) {
                RefundDetail detail = new RefundDetail();
                detail.setRefundAmt(item.getRefundFee());
                detail.setRefundId(item.getOutRefundNo());
                detail.setThirdRefundNo(item.getRefundId());
                detail.setStatus("SUCCESS".equals(item.getStatus()) ? RefundStatus.SUCCESS : RefundStatus.FAILURE);
                details.add(detail);
            }
        }
        result.setDetails(details);
        return Result.success(result);
    }

    // -----------------------------private method---------------------------
    @Override
    protected Map<String, String> transformPayParams(Map<String, String> params) {
        Date start = new Date();
        Date end = Dates.toDate(params.get(PARAM_EXPIRE_TIME), DEFAULT_DATE_FORMAT);

        Map<String, String> map = new HashMap<>();
        map.put("notifyUrl", params.get(Constants.PARAM_PAYMENT_NOTIFY_URL));
        map.put("outTradeNo", params.get(Constants.PARAM_PAY_ID));
        map.put("totalFee", params.get(Constants.PARAM_ORDER_AMT));
        map.put("clientIp", params.get(Constants.PARAM_CLIENT_IP));
        map.put("body", params.get(Constants.PARAM_GOODS_BODY));
        map.put("timeStart", Dates.format(start, DATE_FORMAT));
        map.put("timeExpire", Dates.format(end, DATE_FORMAT));

        // jsapi
        map.put("openId", params.get("openId"));
        return map;
    }

    private Wechatpay getPay(String channelType) {
        switch (toChannel(channelType, "WECHATPAY")) {
            case WECHAT_NATIVE:
            case WECHAT_APP:
            case WECHAT_WAP:
            case WECHAT_MWEB:
                return wepay;
            case WECHAT_JSAPI:
                return wepayJSAPI;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.PARAM_CHANNEL_TYPE, "WECHAT_NATIVE");
        map.put(Constants.PARAM_PAY_ID, "TP7536540445590236");
        map.put(Constants.PARAM_THIRD_TRADE_NO, "4006702001201608232044759117");
        System.out.println(Jsons.NORMAL.stringify(new WechatpayService().refundQuery(map)));
    }

}
