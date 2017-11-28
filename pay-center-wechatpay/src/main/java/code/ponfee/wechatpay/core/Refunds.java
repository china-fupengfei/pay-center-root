package code.ponfee.wechatpay.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

import code.ponfee.wechatpay.model.common.Coupon;
import code.ponfee.wechatpay.model.common.WechatpayField;
import code.ponfee.wechatpay.model.enums.FeeType;
import code.ponfee.wechatpay.model.enums.RefundChannel;
import code.ponfee.wechatpay.model.refund.RefundApplyRequest;
import code.ponfee.wechatpay.model.refund.RefundApplyResponse;
import code.ponfee.wechatpay.model.refund.RefundItem;
import code.ponfee.wechatpay.model.refund.RefundQueryResponse;

/**
 * 退款组件
 */
public final class Refunds extends Component {

    /**
     * 申请退款
     */
    private static final String APPLY = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    /**
     * 查询退款
     */
    private static final String QUERY = "https://api.mch.weixin.qq.com/pay/refundquery";

    Refunds(Wechatpay wepay) {
        super(wepay);
    }

    /**
     * 申请退款
     * @param request 退款请求对象
     * @return RefundResponse对象，或抛WepayException
     * 
     * <pre>
     *  <span>返回结果</span>
     *  {
     *    deviceInfo='null',
     *    nonceStr='A9voHjZDAdpVn22p',
     *    transactionId='4006702001201608111096408225',
     *    outTradeNo='TP19070072473698',
     *    outRefundNo='2016081119168358698184',
     *    refundId='2006702001201608110375903882',
     *    channel=null,
     *    refundFee=1,
     *    totalFee=1,
     *    feeType=null,
     *    cashFee=1,
     *    cashRefundFee=1,
     *    couponRefundFee=0,
     *    couponRefundCount=0,
     *    couponRefundId=null
     *  }
     * </pre>
     */
    public RefundApplyResponse apply(RefundApplyRequest request){
        request.setOpUserId(wechatpay.getMchId());
        checkApplyParams(request);
        Map<String, String> applyParams = buildApplyParams(request);
        return doHttpsPost(APPLY, applyParams, RefundApplyResponse.class);
    }

    /**
     * out_refund_no
     * transaction_id
     * refund_id
     * out_trade_no
     * 
     * @param params
     * @return 退款查询对象，或抛WepayException
     */
    public RefundQueryResponse queryRefund(Map<String, String> params){
        buildConfigParams(params);
        params.put(WechatpayField.nonce_str, RandomStringUtils.randomAlphanumeric(16));
        buildSignParams(params);
        Map<String, String> respData = doPost(QUERY, params);
        return renderQueryResp(respData);
    }


    private RefundQueryResponse renderQueryResp(Map<String, String> refundData) {
        RefundQueryResponse queryResp = new RefundQueryResponse();

        queryResp.setOutTradeNo((String)refundData.get(WechatpayField.OUT_TRADE_NO));
        queryResp.setTransactionId((String)refundData.get(WechatpayField.TRANSACTION_ID));
        queryResp.setTotalFee(Integer.parseInt((String)refundData.get(WechatpayField.TOTAL_FEE)));
        queryResp.setCashFee(Integer.parseInt((String) refundData.get(WechatpayField.CASH_FEE)));
        String feeType = (String)refundData.get(WechatpayField.FEE_TYPE);
        if (!StringUtils.isEmpty(feeType)){
            queryResp.setFeeType(Enum.valueOf(FeeType.class, feeType));
        }

        Integer refundCount = Integer.parseInt((String) refundData.get(WechatpayField.REFUND_COUNT));

        List<RefundItem> refundItems = new ArrayList<>();
        RefundItem refundItem;
        for (int refundIndex = 0; refundIndex < refundCount; refundIndex++){
            refundItem = renderRefundItem(refundData, refundIndex);
            refundItems.add(refundItem);
        }
        queryResp.setItems(refundItems);

        return queryResp;
    }

    private RefundItem renderRefundItem(Map<String, String> refundData, int refundItemIndex) {
        RefundItem refundItem = new RefundItem();
        refundItem.setOutRefundNo((String)refundData.get(WechatpayField.OUT_REFUND_NO + "_" + refundItemIndex));
        refundItem.setRefundId((String)refundData.get(WechatpayField.REFUND_ID + "_" + refundItemIndex));
        refundItem.setChannel(Enum.valueOf(RefundChannel.class, (String)refundData.get(WechatpayField.REFUND_CHANNEL + "_" + refundItemIndex)));
        refundItem.setRefundFee(Integer.parseInt((String)refundData.get(WechatpayField.REFUND_FEE + "_" + refundItemIndex)));
        refundItem.setStatus((String)refundData.get(WechatpayField.REFUND_STATUS + "_" + refundItemIndex));
        Object couponRefundFee = refundData.get(WechatpayField.COUPON_REFUND_FEE + "_" + refundItemIndex);
        if (couponRefundFee != null){
            refundItem.setCouponRefundFee(Integer.parseInt((String)couponRefundFee));
        }
        Object couponRefundCountObj = refundData.get(WechatpayField.COUPON_REFUND_COUNT + "_" + refundItemIndex);
        if (couponRefundCountObj != null){
            Integer couponRefundCount = Integer.parseInt((String)couponRefundCountObj);
            if (couponRefundCount > 0){
                List<Coupon> couponItems = new ArrayList<>();
                Coupon couponItem;
                for (int couponItemIndex = 0; couponItemIndex < couponRefundCount; couponItemIndex++){
                    couponItem = Coupon.newCoupon(
                            (String) refundData.get(WechatpayField.COUPON_REFUND_BATCH_ID + "_" + refundItemIndex + "_" + couponItemIndex),
                            (String) refundData.get(WechatpayField.COUPON_REFUND_ID + "_" + refundItemIndex + "_" + couponItemIndex),
                            Integer.parseInt((String) refundData.get(WechatpayField.COUPON_REFUND_FEE + "_" + refundItemIndex + "_" + couponItemIndex))
                    );
                    couponItems.add(couponItem);
                }
                refundItem.setCoupons(couponItems);
            }
        }

        return refundItem;
    }

    /**
     * 校验退款参数
     * @param request 退款请求对象
     */
    private void checkApplyParams(RefundApplyRequest request) {
        Preconditions.checkNotNull(wechatpay.getSocketFactory(), "merchant sslContext can't be null before apply refund");
        Preconditions.checkNotNull(request, "apply request can't be null");
        if (StringUtils.isEmpty(request.getTransactionId())){
            Preconditions.checkArgument(StringUtils.isNotBlank(request.getOutTradeNo()), 
                                        "transactionId && outTradeNo can not be empty");
        }
        Preconditions.checkArgument(StringUtils.isNotBlank(request.getOutRefundNo()), 
                                    "outRefundNo can not be empty");
        Preconditions.checkArgument(StringUtils.isNotBlank(request.getOpUserId()), 
                                    "opUserId can not be empty");
        Integer totalFee = request.getTotalFee();
        Integer refundFee = request.getRefundFee();
        Preconditions.checkArgument(totalFee > 0, "totalFee must be greater than 0");
        Preconditions.checkArgument(refundFee > 0, "refundFee must be greater than 0");
        Preconditions.checkArgument(totalFee > refundFee, "totalFee must be greater than refundFee");
    }

    /**
     * 构建退款参数
     * @param request 退款请求
     * @return 退款参数
     */
    private Map<String, String> buildApplyParams(RefundApplyRequest request) {
        Map<String, String> refundParams = new TreeMap<>();

        // 配置参数
        buildConfigParams(refundParams);

        // 业务参数
        putIfNotEmpty(refundParams, WechatpayField.TRANSACTION_ID, request.getTransactionId());
        putIfNotEmpty(refundParams, WechatpayField.OUT_TRADE_NO, request.getOutTradeNo());
        refundParams.put(WechatpayField.OUT_REFUND_NO, request.getOutRefundNo());
        refundParams.put(WechatpayField.TOTAL_FEE, request.getTotalFee() + "");
        refundParams.put(WechatpayField.REFUND_FEE, request.getRefundFee() + "");
        refundParams.put(WechatpayField.nonce_str, RandomStringUtils.randomAlphanumeric(16));
        refundParams.put(WechatpayField.OP_USER_ID, request.getOpUserId());
        putIfNotEmpty(refundParams, WechatpayField.DEVICE_INFO, request.getDeviceInfo());
        if (request.getRefundFeeType() != null){
            refundParams.put(WechatpayField.REFUND_FEE_TYPE, request.getRefundFeeType().name());
        }

        // 签名参数
        buildSignParams(refundParams);

        return refundParams;
    }
}
