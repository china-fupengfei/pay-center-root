package code.ponfee.wechatpay.model.bill;

import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.wechatpay.model.common.WechatpayField;

/**
 * 退款账单
 */
public class RefundBill extends CommonBill {

    private static final long serialVersionUID = -6051679079124227683L;

    /**
     * 退款申请时间
     */
    @JsonProperty(WechatpayField.REFUND_APPLY_TIME)
    private String refundApplyTime;

    /**
     * 退款成功时间
     */
    @JsonProperty(WechatpayField.REFUND_SUCCESS_TIME)
    private String refundSuccessTime;

    public String getRefundApplyTime() {
        return refundApplyTime;
    }

    public void setRefundApplyTime(String refundApplyTime) {
        this.refundApplyTime = refundApplyTime;
    }

    public String getRefundSuccessTime() {
        return refundSuccessTime;
    }

    public void setRefundSuccessTime(String refundSuccessTime) {
        this.refundSuccessTime = refundSuccessTime;
    }

    @Override
    public String toString() {
        return "RefundBill{" +
                "refundApplyTime='" + refundApplyTime + '\'' +
                ", refundSuccessTime='" + refundSuccessTime + '\'' +
                "} " + super.toString();
    }
}
