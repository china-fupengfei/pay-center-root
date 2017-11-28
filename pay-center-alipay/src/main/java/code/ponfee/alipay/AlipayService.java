package code.ponfee.alipay;

import static code.ponfee.pay.common.Constants.DEFAULT_DATE_FORMAT;
import static code.ponfee.pay.common.Constants.GOODS_SUBJECT;
import static code.ponfee.pay.common.Constants.PARAM_ALI_DEFAULTBANK;
import static code.ponfee.pay.common.Constants.PARAM_ALI_PAYMETHOD;
import static code.ponfee.pay.common.Constants.PARAM_ATTACH_DATA;
import static code.ponfee.pay.common.Constants.PARAM_CHANNEL_TYPE;
import static code.ponfee.pay.common.Constants.PARAM_CLIENT_IP;
import static code.ponfee.pay.common.Constants.PARAM_EXPIRE_TIME;
import static code.ponfee.pay.common.Constants.PARAM_GOODS_BODY;
import static code.ponfee.pay.common.Constants.PARAM_ORDER_AMT;
import static code.ponfee.pay.common.Constants.PARAM_PAYMENT_NOTIFY_URL;
import static code.ponfee.pay.common.Constants.PARAM_PAYMENT_RETURN_URL;
import static code.ponfee.pay.common.Constants.PARAM_PAY_ID;
import static code.ponfee.pay.common.Constants.PARAM_REFUND_AMT;
import static code.ponfee.pay.common.Constants.PARAM_REFUND_ID;
import static code.ponfee.pay.common.Constants.PARAM_REFUND_NOTIFY_URL;
import static code.ponfee.pay.common.Constants.PARAM_REFUND_REASON;
import static code.ponfee.pay.common.Constants.PARAM_THIRD_TRADE_NO;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import code.ponfee.alipay.core.Alipay;
import code.ponfee.alipay.core.AlipayBuilder;
import code.ponfee.alipay.model.enums.AlipayField;
import code.ponfee.alipay.model.pay.AppPayRequest;
import code.ponfee.alipay.model.pay.BatchPayRequest;
import code.ponfee.alipay.model.pay.PayQueryResponse;
import code.ponfee.alipay.model.pay.WapPayRequest;
import code.ponfee.alipay.model.pay.WebPayRequest;
import code.ponfee.alipay.model.refund.RefundDetailData;
import code.ponfee.alipay.model.refund.RefundQueryDetail;
import code.ponfee.alipay.model.refund.RefundRequest;
import code.ponfee.commons.json.Jsons;
import code.ponfee.commons.math.Numbers;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import code.ponfee.commons.util.Dates;
import code.ponfee.commons.util.ObjectUtils;
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

/**
 * alipay service
 * @author fupf
 */
public class AlipayService extends PayServiceAdapter {
    private static Logger logger = LoggerFactory.getLogger(AlipayService.class);
    //private static final String[] SUCCEED_STATUS = { TradeStatus.TRADE_FINISHED.name(), TradeStatus.TRADE_SUCCESS.name(), TradeStatus.TRADE_CLOSED.name() };
    private static final String[] DATE_PATTERNS = { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-ddHH: mm: ss" };

    private final Alipay pay;
    private final Alipay appPay;

    public AlipayService() {
        pay = AlipayBuilder.newBuilder(AlipayConfig.partner, AlipayConfig.md5Key).build();
        appPay = AlipayBuilder.newBuilder(AlipayConfig.partner, AlipayConfig.ptnPriKey, AlipayConfig.aliPubKey).build();
    }

    @Override
    public Result<PayApplyResult> pay(Map<String, String> params) {
        ChannelType channel = super.toChannel(params.get(PARAM_CHANNEL_TYPE), "ALIPAY");
        params = transformPayParams(params);
        String resp = null;
        switch (channel) {
            case ALIPAY_WEB:
                resp = pay.pay().webPay(ObjectUtils.map2bean(params, WebPayRequest.class));
                break;
            case ALIPAY_APP:
                resp = appPay.pay().appPay(ObjectUtils.map2bean(params, AppPayRequest.class));
                break;
            case ALIPAY_WAP:
                resp = pay.pay().wapPay(ObjectUtils.map2bean(params, WapPayRequest.class));
                break;
            case ALIPAY_BATCH:
                resp = pay.pay().batchPay(ObjectUtils.map2bean(params, BatchPayRequest.class));
                break;
            default:
                return new Result<>(ResultCode.ILLEGAL_ARGS.getCode(), "illegal ChannelType " + channel.name());
        }
        return Result.success(new PayApplyResult(resp));
    }

    /**
     * 支付同步跳转
     * 
     * <pre>
     *   {
     *     "body": "购票总金额：0.01",
     *     "buyer_email": "fupengfei163@163.com",
     *     "buyer_id": "2088702466919323",
     *     "channelType": "ALIPAY_WEB",
     *     "exterface": "create_direct_pay_by_user",
     *     "is_success": "T",
     *     "notify_id": "RqPnCoPT3K9%2Fvwbh3InWfj4mWQ1%2FNIhDG5IV4LmvMeM7wwD5n53JqYUAmWh%2BDJM6V%2Bx7",
     *     "notify_time": "2016-08-10 18:12:57",
     *     "notify_type": "trade_status_sync",
     *     "out_trade_no": "TP31229571999727",
     *     "payment_type": "1",
     *     "seller_email": "piaokuan03@12308.com",
     *     "seller_id": "2088411293539364",
     *     "sign": "138fe38117d077b28d790c0fa6fce047",
     *     "sign_type": "MD5",
     *     "subject": "12308全国汽车票",
     *     "total_fee": "0.01",
     *     "trade_no": "2016081021001004320276954797",
     *     "trade_status": "TRADE_SUCCESS"
     *   }
     * </pre>
     */
    @Override
    public Result<PayReturnResult> payReturn(Map<String, String> params) {
        if (verifyParams(params, false)) {
            PayReturnResult result = new PayReturnResult();
            result.setPayId(params.get(AlipayField.OUT_TRADE_NO.field()));
            result.setOrderAmt((int) (Float.parseFloat(params.get(AlipayField.TOTAL_FEE.field())) * 100));
            result.setThirdTradeNo(params.get(AlipayField.TRADE_NO.field()));
            result.setAttachData(params.get(AlipayField.EXTEND_PARAM.field()));

            String status = params.get(AlipayField.TRADE_STATUS.field());
            if (StringUtils.isNotBlank(status)) {
                // 单笔交易
                Map<String, String> map = new HashMap<>();
                map.put(PARAM_PAY_ID, result.getPayId());
                map.put(PARAM_THIRD_TRADE_NO, result.getThirdTradeNo());
                Result<PayQueryResult> rs = payQuery(map);
                if (rs.isSuccess() && rs.getData() != null) {
                    result.setStatus(rs.getData().getStatus());
                } else {
                    result.setStatus(convertStatus(status));
                }

                //if (!ArrayUtils.contains(SUCCEED_STATUS, status)) {
                //    logger.warn("alipay notify payed status {}", status);
                //}
            } else {
                // 批量支款
                String successKey = AlipayField.SUCCESS_DETAILS.field();
                String failKey = AlipayField.FAIL_DETAILS.field();
                Map<String, String> resultDetail = new HashMap<>();
                resultDetail.put(successKey, params.get(successKey));
                resultDetail.put(failKey, params.get(failKey));
                result.setBatchData(Jsons.NON_EMPTY.stringify(resultDetail));
            }
            return Result.success(result);
        } else {
            return Result.failure(PayResultCode.PARAMS_VERIFY_FAILURE);
        }
    }

    /**
     * 支付异步通知
     * 
     * <pre>
     *   {
     *     "body": "购票总金额：0.01",
     *     "buyer_email": "fupengfei163@163.com",
     *     "buyer_id": "2088702466919323",
     *     "channelType": "ALIPAY_WEB",
     *     "discount": "0.00",
     *     "gmt_create": "2016-08-10 18:12:42",
     *     "gmt_payment": "2016-08-10 18:12:48",
     *     "is_total_fee_adjust": "N",
     *     "notify_id": "0d2191affb79fcd23a087bac0d8a646igy",
     *     "notify_time": "2016-08-10 18:12:49",
     *     "notify_type": "trade_status_sync",
     *     "out_trade_no": "TP31229571999727",
     *     "payment_type": "1",
     *     "price": "0.01",
     *     "quantity": "1",
     *     "seller_email": "piaokuan03@12308.com",
     *     "seller_id": "2088411293539364",
     *     "sign": "d39780b840da420844c186c1a97bb3d7",
     *     "sign_type": "MD5",
     *     "subject": "12308全国汽车票",
     *     "total_fee": "0.01",
     *     "trade_no": "2016081021001004320276954797",
     *     "trade_status": "TRADE_SUCCESS",
     *     "use_coupon": "N"
     *   }
     * </pre>
     */
    @Override
    public Result<PayNotifyResult> payNotify(Map<String, String> params) {
        if (verifyParams(params, false)) {
            PayNotifyResult result = new PayNotifyResult();
            result.setResponse("success");
            result.setPayId(params.get(AlipayField.OUT_TRADE_NO.field()));
            result.setAttachData(params.get(AlipayField.EXTEND_PARAM.field()));
            result.setOrderAmt((int) (Float.parseFloat(params.get(AlipayField.TOTAL_FEE.field())) * 100));
            result.setThirdTradeNo(params.get(AlipayField.TRADE_NO.field()));
            String tradeTime = params.get(AlipayField.GMT_PAYMENT.field());
            if (StringUtils.isNotBlank(tradeTime)) {
                try {
                    result.setTradeTime(DateUtils.parseDate(tradeTime, DATE_PATTERNS));
                } catch (ParseException e) {
                    logger.error("alipay parse date error[{}]", tradeTime, e);
                }
            }
            result.setAttachData(params.get(AlipayField.EXTEND_PARAM.field()));

            String status = params.get(AlipayField.TRADE_STATUS.field());
            if (StringUtils.isNotBlank(status)) {
                // 单笔交易
                Map<String, String> map = new HashMap<>();
                map.put(PARAM_PAY_ID, result.getPayId());
                map.put(PARAM_THIRD_TRADE_NO, result.getThirdTradeNo());
                Result<PayQueryResult> rs = payQuery(map);
                if (rs.isSuccess() && rs.getData() != null) {
                    result.setStatus(rs.getData().getStatus()); // 以查询的状态为准
                } else {
                    result.setStatus(convertStatus(status));
                }

                //if (!ArrayUtils.contains(SUCCEED_STATUS, status)) {
                //    logger.warn("alipay notify payed status {}", status);
                //}
                return Result.success(result);
            } else {
                // 批量付款
                String successKey = AlipayField.SUCCESS_DETAILS.field();
                String failKey = AlipayField.FAIL_DETAILS.field();
                Map<String, String> resultDetail = new HashMap<>();
                resultDetail.put(successKey, params.get(successKey));
                resultDetail.put(failKey, params.get(failKey));
                result.setBatchData(Jsons.NORMAL.stringify(resultDetail));
                return Result.success(result);
            }
        } else {
            return Result.failure(PayResultCode.PARAMS_VERIFY_FAILURE);
        }
    }

    // ----------------------------退款-----------------------------
    /**
     * 退款
     */
    @Override
    public Result<RefundApplyResult> refund(Map<String, String> params) {
        // 组装退款请求参数
        int refundAmt = Integer.parseInt(params.get(PARAM_REFUND_AMT));
        String totalFee = String.valueOf(Numbers.lower(refundAmt, 2));
        RefundDetailData detail = new RefundDetailData();
        detail.setTradeNo(params.get(PARAM_THIRD_TRADE_NO));
        detail.setFee(totalFee);
        detail.setReason(params.get(PARAM_REFUND_REASON));
        RefundRequest req = new RefundRequest();
        req.setBatchNo(params.get(PARAM_REFUND_ID));
        req.setNotifyUrl(params.get(PARAM_REFUND_NOTIFY_URL));
        req.setDetailDatas(Arrays.asList(detail));

        // 发起退款申请
        boolean state = pay.refund().apply(req);
        if (state) {
            RefundApplyResult result = new RefundApplyResult();
            result.setRefundId(req.getBatchNo());
            result.setStatus(RefundStatus.HANDLING);
            return Result.success(result);
        } else {
            return Result.failure(PayResultCode.REFUND_TRADE_FAILURE);
        }
    }

    /**
     * <span>退款成功记录</span>
     * 
     * <pre>
     *  {
     *    batch_no=201608114414930517348,
     *    notify_id=c21e50adeca6d1e913aea331a46901fk0i,
     *    notify_time=2016-08-1110: 42: 23,
     *    notify_type=batch_refund_notify,
     *    result_details=2016081121001004320278315709^0.01^SUCCESS^true^P$piaokuan03@12308.com^2088411293539364^0.00^SUCCESS,
     *    sign=8cbf44df0f276392d36ca8a0d3471c2b,
     *    sign_type=MD5,
     *    success_num=1
     *  }
     * </pre>
     */
    @Override
    public Result<RefundNotifyResult> refundNotify(Map<String, String> params) {
        if (verifyParams(params, true)) {
            RefundNotifyResult result = new RefundNotifyResult();
            result.setResponse("success");
            result.setRefundId(params.get(AlipayField.BATCH_NO.field()));
            // 交易号^退款金额^处理结果$退费账号^退费账户ID^退费金额^处理结果；
            int successNum = Integer.parseInt(params.get(AlipayField.SUCCESS_NUM.field()));

            //if (successNum == 0) {
            //    throw new AlipayException("无成功退款记录：" + Jsons.DEFAULT.toJson(params));
            //} else {
            if (successNum > 1) {
                logger.warn("多批退款通知：{}", Jsons.NORMAL.stringify(params));
            }
            String first = params.get(AlipayField.RESULT_DETAILS.field()).split("\\|")[0];
            String[] array = first.split("\\^");
            if ("SUCCESS".equals(array[2])) {
                result.setRefundAmt((int) (Float.parseFloat(array[1]) * 100));
                result.setStatus(RefundStatus.SUCCESS);
            } else {
                result.setStatus(RefundStatus.FAILURE);
            }

            String time = params.get(AlipayField.NOTIFY_TIME.field());
            if (StringUtils.isNotBlank(time)) {
                try {
                    result.setTradeTime(DateUtils.parseDate(time, DATE_PATTERNS));
                } catch (ParseException e) {
                    logger.error("alipay parse date error[{}]", time, e);
                }
            }
            //}

            return Result.success(result);
        } else {
            return Result.failure(PayResultCode.PARAMS_VERIFY_FAILURE);
        }
    }

    /**
     * <pre>
     *   <alipay>
     *     <is_success>T</is_success>
     *     <request>
     *       <param name="trade_no">2016081121001004320274171692</param>
     *       <param name="_input_charset">UTF-8</param>
     *       <param name="service">single_trade_query</param>
     *       <param name="partner">2088411293539364</param>
     *       <param name="out_trade_no">TP8096837109126</param>
     *     </request>
     *     <response>
     *       <trade>
     *         <additional_trade_status>DAEMON_CONFIRM_CLOSE</additional_trade_status>
     *         <body>购票总金额：0.01</body>
     *         <buyer_email>fupengfei163@163.com</buyer_email>
     *         <buyer_id>2088702466919323</buyer_id>
     *         <discount>0.00</discount>
     *         <flag_trade_locked>0</flag_trade_locked>
     *         <gmt_close>2016-08-11 11:44:48</gmt_close>
     *         <gmt_create>2016-08-11 11:43:46</gmt_create>
     *         <gmt_last_modified_time>2016-08-11 11:44:48</gmt_last_modified_time>
     *         <gmt_payment>2016-08-11 11:43:52</gmt_payment>
     *         <gmt_refund>2016-08-11 11:44:48.665</gmt_refund>
     *         <is_total_fee_adjust>F</is_total_fee_adjust>
     *         <operator_role>B</operator_role>
     *         <out_trade_no>TP8096837109126</out_trade_no>
     *         <payment_type>1</payment_type>
     *         <price>0.01</price>
     *         <quantity>1</quantity>
     *         <refund_fee>0.01</refund_fee>
     *         <refund_flow_type>3</refund_flow_type>
     *         <refund_id>92133524</refund_id>
     *         <refund_status>REFUND_SUCCESS</refund_status>
     *         <seller_email>piaokuan03@12308.com</seller_email>
     *         <seller_id>2088411293539364</seller_id>
     *         <subject>12308全国汽车票</subject>
     *         <to_buyer_fee>0.01</to_buyer_fee>
     *         <to_seller_fee>0.01</to_seller_fee>
     *         <total_fee>0.01</total_fee>
     *         <trade_no>2016081121001004320274171692</trade_no>
     *         <trade_status>TRADE_CLOSED</trade_status>
     *         <use_coupon>F</use_coupon>
     *       </trade>
     *     </response>
     *     <sign>dff958c388fe79fff36952d76cb2e9ee</sign>
     *     <sign_type>MD5</sign_type>
     *   </alipay>
     * </pre>
     */
    @Override
    public Result<PayQueryResult> payQuery(Map<String, String> params) {
        PayQueryResponse resp = pay.pay().query(params.get(PARAM_PAY_ID), params.get(PARAM_THIRD_TRADE_NO));
        PayQueryResult result = new PayQueryResult();
        result.setStatus(convertStatus(resp.getTradeStatus()));
        result.setPayId(resp.getOutTradeNo());
        result.setOrderAmt((int) (Float.parseFloat(resp.getTotalFee()) * 100));
        result.setThirdTradeNo(resp.getTradeNo());
        if (StringUtils.isNotBlank(resp.getGmtPayment())) {
            try {
                result.setTradeTime(DateUtils.parseDate(resp.getGmtPayment(), DATE_PATTERNS));
            } catch (ParseException e) {
                logger.error("alipay parse date error[{}]", resp.getGmtPayment(), e);
            }
        }
        boolean isRefund = StringUtils.isNotBlank(resp.getRefundId());
        if (isRefund) {
            result.setRefundAmt((int) (Float.parseFloat(resp.getRefundFee()) * 100));
            if (StringUtils.isNotBlank(resp.getGmtRefund())) {
                try {
                    result.setRefundTime(DateUtils.parseDate(resp.getGmtRefund(), DATE_PATTERNS));
                } catch (ParseException e) {
                    logger.error("alipay parse date error[{}]", resp.getGmtRefund(), e);
                }
            }
        }
        result.setRefund(isRefund);
        return Result.success(result);
    }

    @Override
    public Result<RefundQueryResult> refundQuery(Map<String, String> params) {
        String refundId = params.get(PARAM_REFUND_ID);
        String thirdTradeNo = params.get(PARAM_THIRD_TRADE_NO);
        List<RefundQueryDetail> list = pay.refund().query(thirdTradeNo, refundId);

        RefundQueryResult result = new RefundQueryResult();
        result.setPayId(params.get(PARAM_PAY_ID));
        result.setThirdTradeNo(thirdTradeNo);
        List<RefundDetail> details = new ArrayList<>();
        for (RefundQueryDetail item : list) {
            RefundDetail detail = new RefundDetail();
            detail.setRefundId(item.getOutRefundNo());
            detail.setStatus("SUCCESS".equals(item.getStatus()) ? RefundStatus.SUCCESS : RefundStatus.FAILURE);
            detail.setRefundAmt((int) (Float.parseFloat(item.getRefundFee()) * 100));
            details.add(detail);
        }
        result.setDetails(details);
        return Result.success(result);
    }

    // ------------------------------------private method ------------------------------------------
    @Override
    protected Map<String, String> transformPayParams(Map<String, String> params) {
        // 公共参数
        int orderAmt = Integer.parseInt(params.get(PARAM_ORDER_AMT));
        String totalFee = String.valueOf(Numbers.lower(orderAmt, 2));
        Map<String, String> map = new HashMap<>();
        map.put("notifyUrl", params.get(PARAM_PAYMENT_NOTIFY_URL));
        map.put("returnUrl", params.get(PARAM_PAYMENT_RETURN_URL));
        map.put("outTradeNo", params.get(PARAM_PAY_ID));
        map.put("totalFee", totalFee);
        map.put("subject", GOODS_SUBJECT);
        map.put("body", params.get(PARAM_GOODS_BODY));

        Date expireTime = Dates.toDate(params.get(PARAM_EXPIRE_TIME), DEFAULT_DATE_FORMAT);
        int seconds = Dates.clockdiff(new Date(), expireTime) / 60;
        map.put("expireTime", seconds + "m");

        // webPay参数
        map.put("payMethod", params.get(PARAM_ALI_PAYMETHOD));
        map.put("defaultbank", params.get(PARAM_ALI_DEFAULTBANK));
        map.put("exterInvokeIp", params.get(PARAM_CLIENT_IP));
        map.put("extraCommonParam", params.get(PARAM_ATTACH_DATA));
        return map;
    }

    private boolean verifyParams(Map<String, String> params, boolean isRefund) {
        ChannelType channel = super.toChannel(params.get(PARAM_CHANNEL_TYPE), "ALIPAY");
        params.remove(PARAM_CHANNEL_TYPE);
        Alipay alipay = (!isRefund && channel == ChannelType.ALIPAY_APP) ? appPay : pay;
        return alipay.verify().verifySign(params) &&
            alipay.verify().verifyNotify(params.get(AlipayField.NOTIFY_ID.field()));
    }

    private PaymentStatus convertStatus(String status) {
        switch (status) {
            case "WAIT_BUYER_PAY":
                return PaymentStatus.WAITING;
            case "TRADE_FINISHED":
            case "TRADE_SUCCESS":
                return PaymentStatus.SUCCESS;
            case "TRADE_PENDING":
                return PaymentStatus.PENDING;
            case "TRADE_CLOSED":
                return PaymentStatus.CLOSED;
            default:
                return PaymentStatus.FAILURE;
        }
    }

    static boolean testSign() {
        Map<String, String> params = new HashMap<>();
        params.put("body", "购买 广元--上海 火车票!");
        params.put("notify_id", "RqPnCoPT3K9/vwbh3InWeOZJDGthEDCoBCHiUQl56jWuvahmgzTSKQLKonjkRSrX48X+");
        params.put("notify_time", "2016-10-12 09:05:57");
        params.put("out_trade_no", "201610121031000010567417");
        params.put("sign", "5441e4bed34e6d29802e32e5e112d491");
        params.put("total_fee", "867.00");
        params.put("trade_no", "2016101221001004760295783100");

        params.put("notify_type", "trade_status_sync");
        params.put("payment_type", "1");
        params.put("seller_id", "2088411293539364");
        params.put("service", "alipay.wap.create.direct.pay.by.user");
        params.put("sign_type", "MD5");
        params.put("subject", "12308全国汽车票");
        params.put("channelType", "ALIPAY_WAP");
        params.put("is_success", "T");
        params.put("trade_status", "TRADE_SUCCESS");

        return new AlipayService().verifyParams(params, false);
    }

    static void testRefund() {
        Map<String, String> map = new HashMap<>();
        map.put(PARAM_REFUND_ID, "20170116" + System.nanoTime());
        map.put(PARAM_THIRD_TRADE_NO, "2017011621001004320245538412");
        map.put(PARAM_REFUND_AMT, "990");
        map.put(PARAM_REFUND_REASON, "AA巴士测试");
        map.put(PARAM_REFUND_NOTIFY_URL, "http://a24118da.ngrok.io/pay-center-testdemo/refund/notify");
        System.out.println(Jsons.NORMAL.stringify(new AlipayService().refund(map)));
    }

    static void testPayQuery() {
        Map<String, String> map = new HashMap<>();
        //map.put(PARAM_PAY_ID, "bus1701163756220");
        map.put(PARAM_THIRD_TRADE_NO, "2017020921001004870289805119");
        System.out.println(Jsons.NORMAL.stringify(new AlipayService().payQuery(map)));
    }

    static void testRefundQuery() {
        Map<String, String> map = new HashMap<>();
        map.put(PARAM_THIRD_TRADE_NO, "2016091921001004910294954442");
        System.out.println(Jsons.NORMAL.stringify(new AlipayService().refundQuery(map)));
    }

    public static void main(String[] args) {
        System.out.println(testSign());
    }

}
