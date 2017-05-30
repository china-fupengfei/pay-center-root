package code.ponfee.qpay.model.refund;

import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.qpay.model.QpayFields;
import code.ponfee.qpay.model.QpayResponse;

/**
 * 退款申请响应
 * @author fupf
 */
public class RefundApplyResponse extends QpayResponse {
    private static final long serialVersionUID = -1677893264308734171L;

    @JsonProperty(QpayFields.TRANSACTION_ID)
    private String transactionId;

    @JsonProperty(QpayFields.OUT_TRADE_NO)
    private String outTradeNo;

    @JsonProperty(QpayFields.TOTAL_FEE)
    private int totalFee;

    @JsonProperty(QpayFields.OUT_REFUND_NO)
    private String outRefundNo;

    @JsonProperty(QpayFields.REFUND_ID)
    private String refundId;

    @JsonProperty(QpayFields.REFUND_CHANNEL)
    private String refundChannel;

    @JsonProperty(QpayFields.REFUND_FEE)
    private int refundFee;

    public String getTransactionId() {
        return transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public String getRefundId() {
        return refundId;
    }

    public String getRefundChannel() {
        return refundChannel;
    }

    public int getRefundFee() {
        return refundFee;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public void setRefundChannel(String refundChannel) {
        this.refundChannel = refundChannel;
    }

    public void setRefundFee(int refundFee) {
        this.refundFee = refundFee;
    }

}
