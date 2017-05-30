package code.ponfee.qpay.model.refund;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.qpay.model.QpayFields;
import code.ponfee.qpay.model.QpayResponse;
import code.ponfee.qpay.model.enums.FeeType;
import code.ponfee.qpay.model.enums.RefundChannel;
import code.ponfee.qpay.model.enums.RefundStatus;

/**
 * 退款查询响应
 * @author fupf
 */
public class RefundQueryResponse extends QpayResponse {
    private static final long serialVersionUID = -2400059583776580794L;

    @JsonProperty(QpayFields.TRANSACTION_ID)
    private String transactionId;

    @JsonProperty(QpayFields.OUT_TRADE_NO)
    private String outTradeNo;

    @JsonProperty(QpayFields.TOTAL_FEE)
    private int totalFee;

    @JsonProperty(QpayFields.CASH_FEE)
    private int cashFee;

    @JsonProperty(QpayFields.FEE_TYPE)
    private FeeType feeType;

    private List<RefundItem> refundItems;

    public String getTransactionId() {
        return transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public int getCashFee() {
        return cashFee;
    }

    public FeeType getFeeType() {
        return feeType;
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

    public void setCashFee(int cashFee) {
        this.cashFee = cashFee;
    }

    public void setFeeType(FeeType feeType) {
        this.feeType = feeType;
    }

    public List<RefundItem> getRefundItems() {
        return refundItems;
    }

    public void setRefundItems(List<RefundItem> refundItems) {
        this.refundItems = refundItems;
    }

    /**
     * 退款明细
     * @author fupf
     */
    public static class RefundItem implements Serializable {
        private static final long serialVersionUID = -7723397488453355213L;

        @JsonProperty(QpayFields.OUT_REFUND_NO)
        private String outRefundNo;

        @JsonProperty(QpayFields.REFUND_ID)
        private String refundId;

        @JsonProperty(QpayFields.REFUND_CHANNEL)
        private RefundChannel refundChannel;

        @JsonProperty(QpayFields.REFUND_FEE)
        private int refundFee;

        @JsonProperty(QpayFields.COUPON_REFUND_FEE)
        private int couponRefundFee;

        @JsonProperty(QpayFields.CASH_REFUND_FEE)
        private int cashRefundFee;

        @JsonProperty(QpayFields.REFUND_STATUS_$)
        private RefundStatus refundStatus;

        public String getOutRefundNo() {
            return outRefundNo;
        }

        public String getRefundId() {
            return refundId;
        }

        public RefundChannel getRefundChannel() {
            return refundChannel;
        }

        public int getRefundFee() {
            return refundFee;
        }

        public int getCouponRefundFee() {
            return couponRefundFee;
        }

        public int getCashRefundFee() {
            return cashRefundFee;
        }

        public RefundStatus getRefundStatus() {
            return refundStatus;
        }

        public void setOutRefundNo(String outRefundNo) {
            this.outRefundNo = outRefundNo;
        }

        public void setRefundId(String refundId) {
            this.refundId = refundId;
        }

        public void setRefundChannel(RefundChannel refundChannel) {
            this.refundChannel = refundChannel;
        }

        public void setRefundFee(int refundFee) {
            this.refundFee = refundFee;
        }

        public void setCouponRefundFee(int couponRefundFee) {
            this.couponRefundFee = couponRefundFee;
        }

        public void setCashRefundFee(int cashRefundFee) {
            this.cashRefundFee = cashRefundFee;
        }

        public void setRefundStatus(RefundStatus refundStatus) {
            this.refundStatus = refundStatus;
        }

    }
}
