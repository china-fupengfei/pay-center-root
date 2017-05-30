package code.ponfee.tenpay.model.notify;

import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.tenpay.model.TenpayField;
import code.ponfee.tenpay.model.TenpayResponse;

/**
 * 通知验证应答数据
 * @author fupf
 */
public class NotifyVerifyResponse extends TenpayResponse {

    private static final long serialVersionUID = 3473591654506887198L;

    @JsonProperty(TenpayField.TRADE_STATE)
    private String tradeState;

    @JsonProperty(TenpayField.TRADE_MODE)
    private String tradeMode;

    @JsonProperty(TenpayField.BANK_TYPE)
    private String bankType;

    @JsonProperty(TenpayField.BANK_BILLNO)
    private String bankBillno;

    @JsonProperty(TenpayField.TOTAL_FEE)
    private String totalFee;

    @JsonProperty(TenpayField.FEE_TYPE)
    private String feeType;

    @JsonProperty(TenpayField.TRANSACTION_ID)
    private String transactionId;

    @JsonProperty(TenpayField.OUT_TRADE_NO)
    private String outTradeNo;

    private String attach;

    @JsonProperty(TenpayField.TIME_END)
    private String timeEnd;

    @JsonProperty(TenpayField.TRANSPORT_FEE)
    private String transportFee;

    @JsonProperty(TenpayField.PRODUCT_FEE)
    private String productFee;

    //@Deprecated private String discount;

    @JsonProperty(TenpayField.BUYER_ALIAS)
    private String buyerAlias;

    public String getTradeState() {
        return tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
    }

    public String getTradeMode() {
        return tradeMode;
    }

    public void setTradeMode(String tradeMode) {
        this.tradeMode = tradeMode;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getBankBillno() {
        return bankBillno;
    }

    public void setBankBillno(String bankBillno) {
        this.bankBillno = bankBillno;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
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

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTransportFee() {
        return transportFee;
    }

    public void setTransportFee(String transportFee) {
        this.transportFee = transportFee;
    }

    public String getProductFee() {
        return productFee;
    }

    public void setProductFee(String productFee) {
        this.productFee = productFee;
    }

    public String getBuyerAlias() {
        return buyerAlias;
    }

    public void setBuyerAlias(String buyerAlias) {
        this.buyerAlias = buyerAlias;
    }

}
