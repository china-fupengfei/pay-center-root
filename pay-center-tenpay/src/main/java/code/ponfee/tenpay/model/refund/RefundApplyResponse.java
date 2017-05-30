package code.ponfee.tenpay.model.refund;

import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.tenpay.model.TenpayField;
import code.ponfee.tenpay.model.TenpayResponse;

/**
 * 退款请求数据
 * @author fupf
 */
public class RefundApplyResponse extends TenpayResponse {

    private static final long serialVersionUID = -3679965222347624253L;

    @JsonProperty(TenpayField.TRANSACTION_ID)
    private String transactionId;

    @JsonProperty(TenpayField.OUT_TRADE_NO)
    private String outTradeNo;

    @JsonProperty(TenpayField.OUT_REFUND_NO)
    private String outRefundNo;

    @JsonProperty(TenpayField.REFUND_ID)
    private String refundId;

    @JsonProperty(TenpayField.REFUND_CHANNEL)
    private String refundChannel;

    @JsonProperty(TenpayField.REFUND_FEE)
    private String refundFee;

    @JsonProperty(TenpayField.REFUND_STATUS)
    private String refundStatus;

    @JsonProperty(TenpayField.RECV_USER_ID)
    private String recvUserId;

    @JsonProperty(TenpayField.RECCV_USER_NAME)
    private String reccvUserName;

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

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getRefundChannel() {
        return refundChannel;
    }

    public void setRefundChannel(String refundChannel) {
        this.refundChannel = refundChannel;
    }

    public String getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(String refundFee) {
        this.refundFee = refundFee;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getRecvUserId() {
        return recvUserId;
    }

    public void setRecvUserId(String recvUserId) {
        this.recvUserId = recvUserId;
    }

    public String getReccvUserName() {
        return reccvUserName;
    }

    public void setReccvUserName(String reccvUserName) {
        this.reccvUserName = reccvUserName;
    }

}
