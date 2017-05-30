package code.ponfee.tenpay;

import static code.ponfee.pay.common.Constants.DEFAULT_DATE_FORMAT;
import static code.ponfee.pay.common.Constants.PARAM_EXPIRE_TIME;
import static code.ponfee.tenpay.TenpayConfig.appId;
import static code.ponfee.tenpay.TenpayConfig.appSecret;
import static code.ponfee.tenpay.TenpayConfig.md5Key;
import static code.ponfee.tenpay.TenpayConfig.opUserPwd;
import static code.ponfee.tenpay.TenpayConfig.partner;
import static code.ponfee.tenpay.TenpayConfig.sslContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
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
import code.ponfee.tenpay.core.Notifies;
import code.ponfee.tenpay.core.Tenpay;
import code.ponfee.tenpay.core.TenpayBuilder;
import code.ponfee.tenpay.exception.TenPayException;
import code.ponfee.tenpay.model.TenpayField;
import code.ponfee.tenpay.model.order.OrderQueryRequest;
import code.ponfee.tenpay.model.order.OrderQueryResponse;
import code.ponfee.tenpay.model.pay.AppPayRequest;
import code.ponfee.tenpay.model.pay.WebPayRequest;
import code.ponfee.tenpay.model.refund.RefundApplyRequest;
import code.ponfee.tenpay.model.refund.RefundApplyResponse;
import code.ponfee.tenpay.model.refund.RefundQueryRequest;
import code.ponfee.tenpay.model.refund.RefundQueryResponse;
import code.ponfee.tenpay.model.refund.RefundQueryResponse.RefundItem;

/**
 * tenpay service
 * @author fupf
 */
public class TenpayService extends PayServiceAdapter {
    private final Tenpay tenpay;

    public TenpayService() {
        tenpay = TenpayBuilder.newBuilder(partner, md5Key, sslContext, appId, appSecret, opUserPwd).build();
    }

    // ------------------------支付-----------------------
    /**
     * <pre>
     *   {
     *     bizType=0,
     *     channelType=TENPAY_WEB,
     *     clientIp=183.16.194.73,
     *     orderAmt=1,
     *     orderNo=bizOrderNo,
     *     orderType=1,
     *     payId=TP29025485003870,
     *     paymentNotifyUrl=http://6f2afefd.ngrok.io/pay-center-demo/payment/notify?channelType=TENPAY_WEB,
     *     paymentReturnUrl=http://6f2afefd.ngrok.io/pay-center-demo/payment/return?channelType=TENPAY_WEB,
     *     remark=remark001,
     *     source=IOS,
     *     userId=1
     *   }
     * </pre>
     */
    @Override
    public Result<PayApplyResult> pay(Map<String, String> params) {
        ChannelType channel = super.toChannel(params.get(Constants.PARAM_CHANNEL_TYPE), "TENPAY");
        Date start = new Date();
        Date end = Dates.toDate(params.get(PARAM_EXPIRE_TIME), DEFAULT_DATE_FORMAT);
        String resp;
        switch (channel) {
            case TENPAY_WEB:
                WebPayRequest webReq = new WebPayRequest();
                webReq.setBody(params.get(Constants.PARAM_GOODS_BODY));
                webReq.setNotifyUrl(params.get(Constants.PARAM_PAYMENT_NOTIFY_URL));
                webReq.setReturnUrl(params.get(Constants.PARAM_PAYMENT_RETURN_URL));
                webReq.setOutTradeNo(params.get(Constants.PARAM_PAY_ID));
                webReq.setTotalFee(Integer.parseInt(params.get(Constants.PARAM_ORDER_AMT)));
                webReq.setSpbillCreateIp(params.get(Constants.PARAM_CLIENT_IP));
                webReq.setAttach(params.get(Constants.PARAM_ATTACH_DATA));
                webReq.setTimeStart(start);
                webReq.setTimeExpire(end);
                resp = tenpay.pay().webPay(webReq);
                break;
            case TENPAY_APP:
                AppPayRequest appReq = new AppPayRequest();
                appReq.setDesc(params.get(Constants.PARAM_GOODS_BODY));
                appReq.setNotifyUrl(params.get(Constants.PARAM_PAYMENT_NOTIFY_URL));
                appReq.setSpBillno(params.get(Constants.PARAM_PAY_ID));
                appReq.setTotalFee(Integer.parseInt(params.get(Constants.PARAM_ORDER_AMT)));
                appReq.setPurchaserId(params.get(Constants.PARAM_QPAY_QQ));
                appReq.setAttach(params.get(Constants.PARAM_ATTACH_DATA));
                appReq.setTimeStart(start);
                appReq.setTimeExpire(end);
                resp = Jsons.NORMAL.stringify(tenpay.pay().appPay(appReq));
                break;
            default:
                return Result.failure(PayResultCode.ILLEGAL_CHANNEL_TYPE.getCode(), "illegal ChannelType " + channel.name());
        }
        return Result.success(new PayApplyResult(resp));
    }

    /**
     * 同步跳转
     * 
     * <pre>
     *  <b>TENPAY_WEB</b>
     *  {
     *    bank_billno = 201608114997530,
     *    bank_type = CMB_FP,
     *    channelType = TENPAY_WEB,
     *    discount = 0,
     *    fee_type = 1,
     *    input_charset = UTF - 8,
     *    notify_id = WE37gwCoFBfnq2bZNEHt9xyMlhQA4cc2ck09oxzHljQXLgPIpL6IxT9wHnoVoOdJ86mYxhj9wDqXwMbaz5yM04yTNG0v_HYA,
     *    out_trade_no = TP29025485003870,
     *    partner = 1900000109,
     *    product_fee = 1,
     *    sign = A5C6B2B3CDDE7700CCD63844B79D06A8,
     *    sign_type = MD5,
     *    time_end = 20160811173254,
     *    total_fee = 1,
     *    trade_mode = 1,
     *    trade_state = 0,
     *    transaction_id = 1900000109201608111600315949,
     *    transport_fee = 0
     *  }
     * 
     *  <b>TENPAY_APP</b>
     *  {
     *    "bank_billno":"20160921012108299833",
     *    "bank_type":"2011",
     *    "bargainor_id":"1233438601",
     *    "channelType":"TENPAY_APP",
     *    "charset":"1",
     *    "fee_type":"1",
     *    "pay_result":"0",
     *    "purchase_alias":"1123656567",
     *    "sign":"543F89D621613F9B4D26F0F18662B17F",
     *    "sp_billno":"TP10017418955165232",
     *    "time_end":"20160921092224",
     *    "total_fee":"2900",
     *    "transaction_id":"1233438601201609212324116589",
     *    "attach": "",
     *    "ver":"2.0"
     *  }
     * </pre>
     */
    @Override
    public Result<PayReturnResult> payReturn(Map<String, String> params) {
        ChannelType channel = super.toChannel(params.get(Constants.PARAM_CHANNEL_TYPE), "TENPAY");
        if (verify(params)) {
            PayReturnResult result = new PayReturnResult();
            if (channel == ChannelType.TENPAY_APP) {
                result.setPayId(params.get(TenpayField.SP_BILLNO));
                result.setStatus(PaymentStatus.SUCCESS);
            } else {
                result.setPayId(params.get(TenpayField.OUT_TRADE_NO));
                if ("0".equals(params.get(TenpayField.TRADE_STATE))) {
                    result.setStatus(PaymentStatus.SUCCESS);
                } else {
                    result.setStatus(PaymentStatus.FAILURE);
                }
            }
            result.setOrderAmt(Integer.parseInt(params.get(TenpayField.TOTAL_FEE)));
            result.setThirdTradeNo(params.get(TenpayField.TRANSACTION_ID));
            result.setAttachData(params.get(TenpayField.ATTACH));
            String tradeTime = params.get(TenpayField.TIME_END);
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
     *  <span></span>
     *  {
     *    bank_billno=201608114997530,
     *    bank_type=CMB_FP,
     *    channelType=TENPAY_WEB,
     *    discount=0,
     *    fee_type=1,
     *    input_charset=UTF-8,
     *    notify_id=WE37gwCoFBfnq2bZNEHt9xyMlhQA4cc2ck09oxzHljQXLgPIpL6IxRor3VRSeg4LbBG_CsCjxbTUWn0_SFLz-zC-UUCH7r74,
     *    out_trade_no=TP29025485003870,
     *    partner=1900000109,
     *    product_fee=1,
     *    sign=0C14110B55F97EDF63B3A514D8366992,
     *    sign_type=MD5,
     *    time_end=20160811173254,
     *    total_fee=1,
     *    trade_mode=1,
     *    trade_state=0,
     *    transaction_id=1900000109201608111600315949,
     *    transport_fee=0
     *  }
     *   
     *  <span>TENPAY_APP</span>
     *  {
     *    "bank_billno":"20160921012108299833",
     *    "bank_type":"2011",
     *    "bargainor_id":"1233438601",
     *    "channelType":"TENPAY_APP",
     *    "charset":"1",
     *    "fee_type":"1",
     *    "pay_result":"0",
     *    "purchase_alias":"1123656567",
     *    "sign":"543F89D621613F9B4D26F0F18662B17F",
     *    "sp_billno":"TP10017418955165232",
     *    "time_end":"20160921092224",
     *    "total_fee":"2900",
     *    "transaction_id":"1233438601201609212324116589",
     *    "attach": "",
     *    "ver":"2.0"
     *  }
     * </pre>
     */
    @Override
    public Result<PayNotifyResult> payNotify(Map<String, String> params) {
        ChannelType channel = super.toChannel(params.get(Constants.PARAM_CHANNEL_TYPE), "TENPAY");
        if (verify(params)) {
            PayNotifyResult result = new PayNotifyResult();
            if (channel == ChannelType.TENPAY_APP) {
                // APP支付
                result.setPayId(params.get(TenpayField.SP_BILLNO));
                result.setStatus(PaymentStatus.SUCCESS);
                result.setResponse("success");
            } else {
                // WEB支付
                result.setPayId(params.get(TenpayField.OUT_TRADE_NO));
                if ("0".equals(params.get(TenpayField.TRADE_STATE))) {
                    result.setStatus(PaymentStatus.SUCCESS);
                    result.setResponse("success");
                } else {
                    result.setStatus(PaymentStatus.FAILURE);
                    result.setResponse("fail");
                }
            }
            result.setOrderAmt(Integer.parseInt(params.get(TenpayField.TOTAL_FEE)));
            result.setThirdTradeNo(params.get(TenpayField.TRANSACTION_ID));
            result.setAttachData(params.get(TenpayField.ATTACH));
            String tradeTime = params.get(TenpayField.TIME_END);
            if (StringUtils.isNotBlank(tradeTime)) {
                result.setTradeTime(Dates.toDate(tradeTime, "yyyyMMddHHmmss"));
            }
            return Result.success(result);
        } else {
            return Result.failure(PayResultCode.PARAMS_VERIFY_FAILURE.getCode(), "fail");
        }
    }

    // ------------------------退款---------------------------
    /**
     * <pre>
     *  {
     *    channelType=TENPAY_WEB,
     *    createTime=20160811174157,
     *    orderAmt=1,
     *    payId=TP29786713086612,
     *    refundAmt=1,
     *    refundId=2016081130078013216337,
     *    refundNo=bizRefundNo,
     *    refundNotifyUrl=http://6f2afefd.ngrok.io/pay-center-demo/refund/notify?channelType=TENPAY_WEB,
     *    refundRate=100,
     *    refundReason=退款原因,
     *    refundType=1,
     *    remark=refund remark,
     *    thirdTradeNo=1900000109201608111100452647
     *  }
     * </pre>
     */
    @Override
    public Result<RefundApplyResult> refund(Map<String, String> params) {
        RefundApplyRequest req = new RefundApplyRequest();
        req.setOutTradeNo(params.get(Constants.PARAM_PAY_ID));
        req.setOutRefundNo(params.get(Constants.PARAM_REFUND_ID));
        req.setTotalFee(Integer.parseInt(params.get(Constants.PARAM_ORDER_AMT)));
        req.setRefundFee(Integer.parseInt(params.get(Constants.PARAM_REFUND_AMT)));
        req.setTransactionId(params.get(Constants.PARAM_THIRD_TRADE_NO));
        req.setNotifyUrl(params.get(Constants.PARAM_REFUND_NOTIFY_URL));
        try {
            /**
             * <pre>
             *  {
             *    sign_key_index=1,
             *    refund_fee=1,
             *    sign_type=MD5,
             *    out_trade_no=TP262363658573346,
             *    out_refund_no=20160921264067235342947,
             *    refund_status=9,
             *    partner=1233438601,
             *    refund_id=1111233438601201609219679581,
             *    retcode=0,
             *    refund_channel=1,
             *    input_charset=UTF-8,
             *    transaction_id=1233438601201609210571340041,
             *    sign=FEC56C9D96AF6BE26645C13138916734,
             *    recv_user_id=,
             *    retmsg=,
             *    reccv_user_name=
             *  }
             * </pre>
             */
            RefundApplyResponse resp = tenpay.refund().apply(req);
            RefundApplyResult result = new RefundApplyResult();
            result.setThirdTradeNo(resp.getTransactionId());
            result.setRefundId(resp.getOutRefundNo());
            result.setThirdRefundNo(resp.getRefundId());
            result.setStatus(transRefundStatus(resp.getRefundStatus()));
            return Result.success(result);
        } catch (TenPayException e) {
            return Result.failure(PayResultCode.REFUND_TRADE_FAILURE.getCode(), e.getMessage());
        }
    }

    /**
     * 退款异步通知
     */
    @Override
    public Result<RefundNotifyResult> refundNotify(Map<String, String> params) {
        // XXX 财付通不回调
        if (verify(params)) {
            RefundNotifyResult result = new RefundNotifyResult();
            result.setPayId(params.get(TenpayField.OUT_TRADE_NO));
            result.setThirdTradeNo(params.get(TenpayField.TRANSACTION_ID));
            result.setRefundId(params.get(TenpayField.OUT_REFUND_NO));
            result.setThirdRefundNo(params.get(TenpayField.REFUND_ID));
            result.setRefundAmt(Integer.parseInt(params.get(TenpayField.REFUND_FEE)));
            result.setStatus(transRefundStatus(params.get(TenpayField.REFUND_STATUS)));
            if (result.getStatus() == RefundStatus.SUCCESS) {
                result.setResponse("success");
            } else {
                result.setResponse("fail");
            }
            return Result.success(result);
        } else {
            return Result.failure(PayResultCode.PARAMS_VERIFY_FAILURE);
        }
    }

    // ---------------------查询---------------------
    /**
     * 支付查询
     */
    @Override
    public Result<PayQueryResult> payQuery(Map<String, String> params) {
        OrderQueryRequest req = new OrderQueryRequest();
        req.setOutTradeNo(params.get(Constants.PARAM_PAY_ID));
        req.setTransactionId(params.get(Constants.PARAM_THIRD_TRADE_NO));
        OrderQueryResponse resp = tenpay.order().queryOrder(req);

        PayQueryResult result = new PayQueryResult();
        result.setPayId(resp.getOutTradeNo());
        result.setOrderAmt(resp.getTotalFee());
        result.setThirdTradeNo(resp.getTransactionId());
        result.setTradeTime(resp.getTimeEnd());
        result.setStatus(resp.getTradeState() == 0 ? PaymentStatus.SUCCESS : PaymentStatus.FAILURE);
        result.setRefund(resp.isRefund());
        return Result.success(result);
    }

    /**
     * 退款查询
     */
    @Override
    public Result<RefundQueryResult> refundQuery(Map<String, String> params) {
        RefundQueryRequest req = new RefundQueryRequest();
        req.setOutTradeNo(params.get(Constants.PARAM_PAY_ID));
        req.setTransactionId(params.get(Constants.PARAM_THIRD_TRADE_NO));
        req.setOutRefundNo(params.get(Constants.PARAM_REFUND_ID));
        req.setRefundId(params.get(Constants.PARAM_THIRD_REFUND_NO));
        RefundQueryResponse resp = tenpay.refund().query(req);

        RefundQueryResult result = new RefundQueryResult();
        result.setPayId(resp.getOutTradeNo());
        result.setThirdTradeNo(resp.getTransactionId());

        List<RefundDetail> details = new ArrayList<>();
        if (resp.getDetails() != null) {
            for (RefundItem item : resp.getDetails()) {
                RefundDetail detail = new RefundDetail();
                detail.setRefundId(item.getOutRefundNo());
                detail.setThirdRefundNo(item.getRefundId());
                detail.setRefundAmt(item.getRefundFee());
                detail.setTradeTime(item.getRefundTime()); // 此版本为空
                detail.setStatus(transRefundStatus(item.getState()));
                details.add(detail);
            }
        }
        result.setDetails(details);
        return Result.success(result);
    }

    // ---------------------------private method-----------------------------
    /**
     * 验证签名
     * @param params
     * @return
     */
    private boolean verify(Map<String, String> params) {
        params.remove(Constants.PARAM_CHANNEL_TYPE);
        String notifyId = params.get(TenpayField.NOTIFY_ID);
        Notifies notify = tenpay.notifies();
        if (StringUtils.isEmpty(notifyId)) {
            return notify.verifySign(params);
        } else {
            return notify.verifySign(params) && notify.verifyNotify(notifyId);
        }
    }

    /**
     * 支付状态转换
     * @param status
     * @return
     */
    private RefundStatus transRefundStatus(String status) {
        //4，10：退款成功。
        //3，5，6：退款失败。
        //8，9，11:退款处理中。
        //1，2: 未确定，需要商户原退款单号重新发起。
        //7：转入代发，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，资金回流到商户的现金帐号，需要商户人工干预，通过线下或者财付通转账的方式进行退款。
        if (ArrayUtils.contains(new String[] { "8", "9", "11" }, status)) {
            return RefundStatus.HANDLING;
        } else if (ArrayUtils.contains(new String[] { "4", "10" }, status)) {
            return RefundStatus.SUCCESS;
        } else {
            return RefundStatus.FAILURE;
        }
    }

    @Override
    protected Map<String, String> transformPayParams(Map<String, String> params) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.PARAM_PAY_ID, "TP9932882164608393");
        map.put(Constants.PARAM_THIRD_TRADE_NO, "1233438601201609202218281438");
        map.put(Constants.PARAM_REFUND_ID, "201609209933291084457681");
        System.out.println(Jsons.NORMAL.stringify(new TenpayService().refundQuery(map)));
    }

}
