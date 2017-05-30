package code.ponfee.tenpay.model.refund;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.tenpay.model.TenpayField;
import code.ponfee.tenpay.model.TenpayResponse;

/**
 * 退款查询应答类
 * @author fupf
 */
public class RefundQueryResponse extends TenpayResponse {

    private static final long serialVersionUID = 3788747558219360994L;

    @JsonProperty(TenpayField.OUT_TRADE_NO)
    private String outTradeNo;

    @JsonProperty(TenpayField.TRANSACTION_ID)
    private String transactionId;

    @JsonProperty(TenpayField.REFUND_COUNT)
    private int refundCount;

    @JsonProperty(TenpayField.REFUND_DETAILS)
    private List<RefundItem> details;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public int getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(int refundCount) {
        this.refundCount = refundCount;
    }

    public List<RefundItem> getDetails() {
        return details;
    }

    public void setDetails(List<RefundItem> details) {
        this.details = details;
    }

    public static class RefundItem implements Serializable {
        private static final long serialVersionUID = 215762667421065849L;

        @JsonProperty(TenpayField.OUT_REFUND_NO)
        private String outRefundNo;

        @JsonProperty(TenpayField.REFUND_ID)
        private String refundId;

        @JsonProperty(TenpayField.REFUND_CHANNEL)
        private String refundChannel;

        @JsonProperty(TenpayField.REFUND_FEE)
        private int refundFee;

        @JsonProperty(TenpayField.REFUND_STATE)
        private String state;

        @JsonProperty(TenpayField.RECV_USER_ID)
        private String recvUserId;

        @JsonProperty(TenpayField.RECCV_USER_NAME)
        private String reccvUserName;

        @JsonProperty(TenpayField.REFUND_TIME_BEGIN)
        @JsonFormat(pattern = "yyyyMMddhhmmss", timezone = "GMT+8")
        private Date refundTimeBegin;

        @JsonProperty(TenpayField.REFUND_TIME)
        @JsonFormat(pattern = "yyyyMMddhhmmss", timezone = "GMT+8")
        private Date refundTime;

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

        public int getRefundFee() {
            return refundFee;
        }

        public void setRefundFee(int refundFee) {
            this.refundFee = refundFee;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
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

        public Date getRefundTimeBegin() {
            return refundTimeBegin;
        }

        public void setRefundTimeBegin(Date refundTimeBegin) {
            this.refundTimeBegin = refundTimeBegin;
        }

        public Date getRefundTime() {
            return refundTime;
        }

        public void setRefundTime(Date refundTime) {
            this.refundTime = refundTime;
        }

    }
}