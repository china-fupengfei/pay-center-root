package code.ponfee.tenpay.model.order;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.tenpay.model.TenpayField;
import code.ponfee.tenpay.model.TenpayResponse;

/**
 * 订单查询应答类
 * @author fupf
 */
public class OrderQueryResponse extends TenpayResponse {

    private static final long serialVersionUID = 2312163343220463507L;

    @JsonProperty(TenpayField.TRADE_STATE)
    private int tradeState;

    @JsonProperty(TenpayField.TRADE_MODE)
    private int tradeMode;

    @JsonProperty(TenpayField.BANK_TYPE)
    private String bankType;

    @JsonProperty(TenpayField.BANK_BILLNO)
    private String bankBillno;

    @JsonProperty(TenpayField.TOTAL_FEE)
    private int totalFee;

    @JsonProperty(TenpayField.FEE_TYPE)
    private int feeType;

    @JsonProperty(TenpayField.TRANSACTION_ID)
    private String transactionId;

    @JsonProperty(TenpayField.OUT_TRADE_NO)
    private String outTradeNo;

    @JsonProperty(TenpayField.IS_SPLIT)
    private boolean isSplit;

    @JsonProperty(TenpayField.IS_REFUND)
    private boolean isRefund;

    private String attach;

    @JsonProperty(TenpayField.TIME_END)
    @JsonFormat(pattern="yyyyMMddhhmmss", timezone="GMT+8")
    private Date timeEnd;

    @JsonProperty(TenpayField.TRANSPORT_FEE)
    private int transportFee;

    @JsonProperty(TenpayField.PRODUCT_FEE)
    private int productFee;

    //@Deprecated private int discount;

    @JsonProperty(TenpayField.BUYER_ALIAS)
    private String buyerAlias;

    @JsonProperty(TenpayField.CASH_TICKET_FEE)
    private int cashTicketFee;

    public int getTradeState() {
        return tradeState;
    }

    public void setTradeState(int tradeState) {
        this.tradeState = tradeState;
    }

    public int getTradeMode() {
        return tradeMode;
    }

    public void setTradeMode(int tradeMode) {
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

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public int getFeeType() {
        return feeType;
    }

    public void setFeeType(int feeType) {
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

    public boolean isSplit() {
        return isSplit;
    }

    public void setSplit(boolean isSplit) {
        this.isSplit = isSplit;
    }

    public boolean isRefund() {
        return isRefund;
    }

    public void setRefund(boolean isRefund) {
        this.isRefund = isRefund;
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

    public int getTransportFee() {
        return transportFee;
    }

    public void setTransportFee(int transportFee) {
        this.transportFee = transportFee;
    }

    public int getProductFee() {
        return productFee;
    }

    public void setProductFee(int productFee) {
        this.productFee = productFee;
    }

    public String getBuyerAlias() {
        return buyerAlias;
    }

    public void setBuyerAlias(String buyerAlias) {
        this.buyerAlias = buyerAlias;
    }

    public int getCashTicketFee() {
        return cashTicketFee;
    }

    public void setCashTicketFee(int cashTicketFee) {
        this.cashTicketFee = cashTicketFee;
    }

}