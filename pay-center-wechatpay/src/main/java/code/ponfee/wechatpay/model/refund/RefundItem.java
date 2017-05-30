package code.ponfee.wechatpay.model.refund;

import code.ponfee.wechatpay.model.common.Coupon;
import code.ponfee.wechatpay.model.enums.RefundChannel;

import java.io.Serializable;
import java.util.List;

/**
 * 单笔退款
 */
public class RefundItem implements Serializable {

    private static final long serialVersionUID = -8803509387441693049L;

    /**
     * 商户退款单号
     */
    private String outRefundNo;

    /**
     * 微信退款单号
     */
    private String refundId;

    /**
     * 退款渠道
     */
    private RefundChannel channel;

    /**
     * 退款金额
     */
    private Integer refundFee;

    /**
     * 代金券或立减优惠退款金额
     */
    private Integer couponRefundFee;

    /**
     * 代金券或立减优惠退款项
     */
    private List<Coupon> coupons;
    
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public RefundChannel getChannel() {
        return channel;
    }

    public void setChannel(RefundChannel channel) {
        this.channel = channel;
    }

    public Integer getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Integer refundFee) {
        this.refundFee = refundFee;
    }

    public Integer getCouponRefundFee() {
        return couponRefundFee;
    }

    public void setCouponRefundFee(Integer couponRefundFee) {
        this.couponRefundFee = couponRefundFee;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    @Override
    public String toString() {
        return "RefundItem{" +
                "outRefundNo='" + outRefundNo + '\'' +
                ", refundId='" + refundId + '\'' +
                ", channel=" + channel +
                ", refundFee=" + refundFee +
                ", couponRefundFee=" + couponRefundFee +
                ", coupons=" + coupons +
                '}';
    }
}
