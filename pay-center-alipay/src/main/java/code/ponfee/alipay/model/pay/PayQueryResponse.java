package code.ponfee.alipay.model.pay;

import java.io.Serializable;

/**
 * 支付查询响应
 * @author fupf
 */
public class PayQueryResponse implements Serializable {

    private static final long serialVersionUID = -4162127519709792752L;

    private String tradeStatus;
    private String outTradeNo;
    private String totalFee;
    private String tradeNo;
    private String gmtPayment;
    private String refundId;
    private String refundStatus;
    private String refundFee;
    private String gmtRefund;

    public String getTradeStatus() {
        return tradeStatus;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public String getGmtPayment() {
        return gmtPayment;
    }

    public String getRefundId() {
        return refundId;
    }

    public String getRefundFee() {
        return refundFee;
    }

    public String getGmtRefund() {
        return gmtRefund;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public void setGmtPayment(String gmtPayment) {
        this.gmtPayment = gmtPayment;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public void setRefundFee(String refundFee) {
        this.refundFee = refundFee;
    }

    public void setGmtRefund(String gmtRefund) {
        this.gmtRefund = gmtRefund;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

}
