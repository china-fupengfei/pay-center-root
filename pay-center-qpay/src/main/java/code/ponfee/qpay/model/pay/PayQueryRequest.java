package code.ponfee.qpay.model.pay;

import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.qpay.model.QpayFields;
import code.ponfee.qpay.model.QpayRequest;

/**
 * 支付查询请求
 * @author fupf
 */
public class PayQueryRequest extends QpayRequest {
    private static final long serialVersionUID = -8945153705729384273L;

    @JsonProperty(QpayFields.OUT_TRADE_NO)
    private String outTradeNo;

    @JsonProperty(QpayFields.TRANSACTION_ID)
    private String transactionId;

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

}
