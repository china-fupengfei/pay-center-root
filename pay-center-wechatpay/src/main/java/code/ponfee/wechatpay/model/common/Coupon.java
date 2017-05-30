package code.ponfee.wechatpay.model.common;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 代金券
 */
public class Coupon implements Serializable {

    private static final long serialVersionUID = -2006707624918389486L;

    /**
     * 代金券或立减优惠批次ID
     */
    private String batchId;

    /**
     * 代金券或立减优惠ID
     */
    private String id;

    /**
     * 单个代金券或立减优惠支付金额
     */
    private Integer fee;

    private Coupon(){}

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public static Coupon newCoupon(String batchId, String id, Integer fee){
        Coupon c = new Coupon();
        c.batchId = batchId;
        c.id = id;
        c.fee = fee;
        return c;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
}