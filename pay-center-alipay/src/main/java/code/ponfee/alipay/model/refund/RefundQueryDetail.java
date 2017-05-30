package code.ponfee.alipay.model.refund;

import java.io.Serializable;

public class RefundQueryDetail implements Serializable {

    private static final long serialVersionUID = -758791298332730542L;

    private String outRefundNo;
    private String tradeNo;
    private String status;
    private String refundFee;

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public String getStatus() {
        return status;
    }

    public String getRefundFee() {
        return refundFee;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRefundFee(String refundFee) {
        this.refundFee = refundFee;
    }

}
