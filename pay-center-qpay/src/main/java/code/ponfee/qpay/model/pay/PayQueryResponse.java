package code.ponfee.qpay.model.pay;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.qpay.model.QpayFields;
import code.ponfee.qpay.model.QpayResponse;
import code.ponfee.qpay.model.enums.FeeType;
import code.ponfee.qpay.model.enums.TradeState;
import code.ponfee.qpay.model.enums.TradeType;

/**
 * 支付查询响应
 * @author fupf
 */
public class PayQueryResponse extends QpayResponse {
    private static final long serialVersionUID = -3996997491285976634L;

    @JsonProperty(QpayFields.DEVICE_INFO)
    private String deviceInfo;

    @JsonProperty(QpayFields.TRADE_TYPE)
    private TradeType tradeType;

    @JsonProperty(QpayFields.TRADE_STATE)
    private TradeState tradeState;

    @JsonProperty(QpayFields.BANK_TYPE)
    private String bankType;

    @JsonProperty(QpayFields.FEE_TYPE)
    private FeeType feeType;

    @JsonProperty(QpayFields.TOTAL_FEE)
    private int totalFee;

    @JsonProperty(QpayFields.CASH_FEE)
    private int cashFee;

    @JsonProperty(QpayFields.COUPON_FEE)
    private int couponFee;

    @JsonProperty(QpayFields.TRANSACTION_ID)
    private String transactionId;

    @JsonProperty(QpayFields.OUT_TRADE_NO)
    private String outTradeNo;

    private String attach;

    @JsonProperty(QpayFields.TIME_END)
    @JsonFormat(pattern = "yyyyMMddHHmmss", timezone = "GMT+8")
    private Date timeEnd;

    @JsonProperty(QpayFields.TRADE_STATE_DESC)
    private String tradeStateDesc;

    private String openid;

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public TradeState getTradeState() {
        return tradeState;
    }

    public String getBankType() {
        return bankType;
    }

    public FeeType getFeeType() {
        return feeType;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public int getCashFee() {
        return cashFee;
    }

    public int getCouponFee() {
        return couponFee;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public String getAttach() {
        return attach;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public String getTradeStateDesc() {
        return tradeStateDesc;
    }

    public String getOpenid() {
        return openid;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public void setTradeState(TradeState tradeState) {
        this.tradeState = tradeState;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public void setFeeType(FeeType feeType) {
        this.feeType = feeType;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public void setCashFee(int cashFee) {
        this.cashFee = cashFee;
    }

    public void setCouponFee(int couponFee) {
        this.couponFee = couponFee;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setTradeStateDesc(String tradeStateDesc) {
        this.tradeStateDesc = tradeStateDesc;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

}
