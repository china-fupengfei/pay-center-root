package code.ponfee.wechatpay.model.refund;

import java.io.Serializable;

import code.ponfee.wechatpay.model.enums.FeeType;

/**
 * 退款请求对象
 */
public class RefundApplyRequest implements Serializable {

    private static final long serialVersionUID = 5046932866574485686L;

    /**
     * 设备号
     */
    private String deviceInfo;

    /**
     * 微信订单号，与outTradeNo二选一
     */
    private String transactionId;

    /**
     * 商户订单号，与transactionId二选一
     */
    private String outTradeNo;

    /**
     * 商户退款单号
     */
    private String outRefundNo;

    /**
     * 订单总金额
     */
    private Integer totalFee;

    /**
     * 退款金额
     */
    private Integer refundFee;

    /**
     * 货币类型
     */
    private FeeType refundFeeType = FeeType.CNY;

    /**
     * 操作员
     */
    private String opUserId;

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

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

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public Integer getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Integer refundFee) {
        this.refundFee = refundFee;
    }

    public FeeType getRefundFeeType() {
        return refundFeeType;
    }

    public void setRefundFeeType(FeeType refundFeeType) {
        this.refundFeeType = refundFeeType;
    }

    public String getOpUserId() {
        return opUserId;
    }

    public void setOpUserId(String opUserId) {
        this.opUserId = opUserId;
    }

    @Override
    public String toString() {
        return "RefundRequest{" +
                "deviceInfo='" + deviceInfo + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", outRefundNo='" + outRefundNo + '\'' +
                ", totalFee=" + totalFee +
                ", refundFee=" + refundFee +
                ", refundFeeType=" + refundFeeType +
                ", opUserId='" + opUserId + '\'' +
                '}';
    }
}
