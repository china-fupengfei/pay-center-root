package code.ponfee.wechatpay.model.order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import code.ponfee.wechatpay.model.common.Coupon;
import code.ponfee.wechatpay.model.common.WechatpayField;
import code.ponfee.wechatpay.model.enums.FeeType;
import code.ponfee.wechatpay.model.enums.TradeState;
import code.ponfee.wechatpay.model.enums.TradeType;
import code.ponfee.wechatpay.serializer.BooleanDeserializer;
import code.ponfee.wechatpay.serializer.FeeTypeDeserializer;
import code.ponfee.wechatpay.serializer.TradeStateDeserializer;
import code.ponfee.wechatpay.serializer.TradeTypeDeserializer;

/**
 * 微信查询订单对象
 */
public class WechatpayOrder implements Serializable {

    private static final long serialVersionUID = -3808893700552515824L;

    /**
     * 用户openId
     */
    @JsonProperty(WechatpayField.OPEN_ID)
    private String openId;

    /**
     * 用户是否关注公众号
     */
    @JsonProperty(WechatpayField.IS_SUBSCRIBE)
    @JsonDeserialize(using = BooleanDeserializer.class)
    private Boolean subscribe;

    /**
     * 交易类型
     */
    @JsonProperty(WechatpayField.TRADE_TYPE)
    @JsonDeserialize(using = TradeTypeDeserializer.class)
    private TradeType tradeType;

    /**
     * 银行类型
     */
    @JsonProperty(WechatpayField.BANK_TYPE)
    private String bankType;

    /**
     * 总金额
     */
    @JsonProperty(WechatpayField.TOTAL_FEE)
    private Integer totalFee;

    @JsonProperty(WechatpayField.FEE_TYPE)
    @JsonDeserialize(using = FeeTypeDeserializer.class)
    private FeeType feeType;

    /**
     * 微信订单好
     */
    @JsonProperty(WechatpayField.TRANSACTION_ID)
    private String transactionId;

    /**
     * 商户订单号
     */
    @JsonProperty(WechatpayField.OUT_TRADE_NO)
    private String outTradeNo;

    /**
     * 附加数据
     */
    private String attach;

    /**
     * 支付完成时间
     */
    @JsonProperty(WechatpayField.TIME_END)
    @JsonFormat(pattern = "yyyyMMddHHmmss", timezone="GMT+8")
    private Date timeEnd;

    /**
     * 交易状态
     */
    @JsonProperty(WechatpayField.TRADE_STATE)
    @JsonDeserialize(using = TradeStateDeserializer.class)
    private TradeState tradeState;

    /**
     * 交易状态描述
     */
    @JsonProperty(WechatpayField.TRADE_STATE_DESC)
    private String tradeStateDesc;

    /**
     * 现金支付金额
     */
    @JsonProperty(WechatpayField.CASH_FEE)
    private Integer cachFee;

    @JsonProperty(WechatpayField.CASH_FEE_TYPE)
    @JsonDeserialize(using = FeeTypeDeserializer.class)
    private FeeType cashFeeType;

    /**
     * 设备号
     */
    @JsonProperty(WechatpayField.DEVICE_INFO)
    private String deviceInfo;

    /**
     * 代金券或立减优惠金额(分)
     */
    @JsonProperty(WechatpayField.COUPON_FEE)
    private Integer couponFee;

    /**
     * 代金券或立减优惠使用数量
     */
    @JsonProperty(WechatpayField.COUPON_COUNT)
    private Integer couponCount;

    /**
     * 代金券或立减优惠明细
     */
    @JsonIgnore
    private List<Coupon> coupons;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Boolean getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Boolean subscribe) {
        this.subscribe = subscribe;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public FeeType getFeeType() {
        return feeType;
    }

    public void setFeeType(FeeType feeType) {
        this.feeType = feeType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public TradeState getTradeState() {
        return tradeState;
    }

    public void setTradeState(TradeState tradeState) {
        this.tradeState = tradeState;
    }

    public String getTradeStateDesc() {
        return tradeStateDesc;
    }

    public void setTradeStateDesc(String tradeStateDesc) {
        this.tradeStateDesc = tradeStateDesc;
    }

    public Integer getCachFee() {
        return cachFee;
    }

    public void setCachFee(Integer cachFee) {
        this.cachFee = cachFee;
    }

    public FeeType getCashFeeType() {
        return cashFeeType;
    }

    public void setCashFeeType(FeeType cashFeeType) {
        this.cashFeeType = cashFeeType;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public Integer getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(Integer couponFee) {
        this.couponFee = couponFee;
    }

    public Integer getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(Integer couponCount) {
        this.couponCount = couponCount;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }
    
    public void setCoupons(Map<String, String> orderData) {
        if (this.getCouponCount() != null && this.getCouponCount() > 0) {
            List<Coupon> coupons = new ArrayList<>();
            for (int idx = 0; idx < this.getCouponCount(); idx++) {
                String batchId = (String)orderData.get(WechatpayField.COUPON_BATCH_ID + "_" + idx);
                String id = (String)orderData.get(WechatpayField.COUPON_ID + "_" + idx);
                int fee = Integer.parseInt((String)orderData.get(WechatpayField.COUPON_FEE + "_" + idx));
                coupons.add(Coupon.newCoupon(batchId, id, fee));
            }
            this.setCoupons(coupons);
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
