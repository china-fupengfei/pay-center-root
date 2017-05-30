package code.ponfee.qpay.model.refund;

import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.qpay.model.QpayFields;
import code.ponfee.qpay.model.QpayRequest;

/**
 * 退款查询请求
 * @author fupf
 */
public class RefundQueryRequest extends QpayRequest {
    private static final long serialVersionUID = 3300262727588520569L;

    @JsonProperty(QpayFields.OUT_TRADE_NO)
    private String outTradeNo;
    
    @JsonProperty(QpayFields.REFUND_ID)
    private String refundId;

    @JsonProperty(QpayFields.OUT_REFUND_NO)
    private String outRefundNo;

    @JsonProperty(QpayFields.TRANSACTION_ID)
    private String transactionId;

    public String getRefundId() {
        return refundId;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
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

}
