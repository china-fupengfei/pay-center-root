package code.ponfee.qpay.model.refund;

import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.qpay.model.QpayFields;
import code.ponfee.qpay.model.QpayRequest;

/**
 * 退款申请请求
 * @author fupf
 */
public class RefundApplyRequest extends QpayRequest {
    private static final long serialVersionUID = -4889540452161101428L;

    @JsonProperty(QpayFields.OUT_TRADE_NO)
    private String outTradeNo;

    @JsonProperty(QpayFields.TRANSACTION_ID)
    private String transactionId;

    @JsonProperty(QpayFields.OUT_REFUND_NO)
    private String outRefundNo;

    @JsonProperty(QpayFields.REFUND_FEE)
    private int refundFee;

    @JsonProperty(QpayFields.OP_USER_ID)
    private String opUserId;

    @JsonProperty(QpayFields.OP_USER_PASSWD)
    private String opUserPasswd;

    public String getTransactionId() {
        return transactionId;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public int getRefundFee() {
        return refundFee;
    }

    public String getOpUserId() {
        return opUserId;
    }

    public String getOpUserPasswd() {
        return opUserPasswd;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public void setRefundFee(int refundFee) {
        this.refundFee = refundFee;
    }

    public void setOpUserId(String opUserId) {
        this.opUserId = opUserId;
    }

    public void setOpUserPasswd(String opUserPasswd) {
        this.opUserPasswd = opUserPasswd;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

}
