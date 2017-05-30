package code.ponfee.tenpay.model.order;

import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.tenpay.model.TenpayField;
import code.ponfee.tenpay.model.TenpayRequest;

/**
 * 订单查询请求类
 * @author fupf
 */
public class OrderQueryRequest extends TenpayRequest {

    private static final long serialVersionUID = 2415817280617951426L;

    @JsonProperty(TenpayField.TRANSACTION_ID)
    private String transactionId;

    //@JsonProperty(TenpayField.USE_SPBILL_NO_FLAG)
    //private int useSpbillNoFlag;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

}
