package code.ponfee.wechatpay.model.bill;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import code.ponfee.wechatpay.model.common.WechatpayField;
import code.ponfee.wechatpay.model.enums.RefundChannel;
import code.ponfee.wechatpay.serializer.RefundChannelDeserializer;

/**
 * 普通账单
 */
public class CommonBill extends Bill {

    private static final long serialVersionUID = -4518299029269484159L;

    /**
     * 微信退款单号
     */
    @JsonProperty(WechatpayField.REFUND_ID)
    private String refundId;

    /**
     * 商户退款单号
     */
    @JsonProperty(WechatpayField.OUT_REFUND_NO)
    private String outRefundNo;

    /**
     * 退款金额(元)
     */
    @JsonProperty(WechatpayField.REFUND_FEE)
    private Float refundFee;

    /**
     * 企业红包退款金额(元)
     */
    @JsonProperty(WechatpayField.ENTER_RED_PKG_REFUND_FEE)
    private Float enterRedPkgRefundFee;

    /**
     * 退款类型
     */
    @JsonProperty(WechatpayField.REFUND_CHANNEL)
    @JsonDeserialize(using = RefundChannelDeserializer.class)
    private RefundChannel channel;

    /**
     * 退款状态
     */
    @JsonProperty(WechatpayField.REFUND_STATUS)
    private String refundStatus;

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public Float getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Float refundFee) {
        this.refundFee = refundFee;
    }

    public Float getEnterRedPkgRefundFee() {
        return enterRedPkgRefundFee;
    }

    public void setEnterRedPkgRefundFee(Float enterRedPkgRefundFee) {
        this.enterRedPkgRefundFee = enterRedPkgRefundFee;
    }

    public RefundChannel getChannel() {
        return channel;
    }

    public void setChannel(RefundChannel channel) {
        this.channel = channel;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    @Override
    public String toString() {
        return "CommonBill{" +
                "refundId='" + refundId + '\'' +
                ", outRefundNo='" + outRefundNo + '\'' +
                ", refundFee=" + refundFee +
                ", enterRedPkgRefundFee=" + enterRedPkgRefundFee +
                ", channel=" + channel +
                ", refundStatus='" + refundStatus + '\'' +
                "} " + super.toString();
    }
}
