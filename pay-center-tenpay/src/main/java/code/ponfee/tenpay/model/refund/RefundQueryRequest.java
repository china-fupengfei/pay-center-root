package code.ponfee.tenpay.model.refund;

import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.tenpay.model.TenpayField;
import code.ponfee.tenpay.model.TenpayRequest;

/**
 * 退款明细查询请求
 * @author fupf
 */
public class RefundQueryRequest extends TenpayRequest {

    private static final long serialVersionUID = 7410285440110651236L;

    @JsonProperty(TenpayField.TRANSACTION_ID)
    private String transactionId;

    @JsonProperty(TenpayField.OUT_REFUND_NO)
    private String outRefundNo;

    @JsonProperty(TenpayField.REFUND_ID)
    private String refundId;

    //private String useSpbillNoFlag;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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

}
