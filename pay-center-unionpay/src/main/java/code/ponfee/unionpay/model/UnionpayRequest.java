package code.ponfee.unionpay.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 请求参数
 * @author fupf
 */
public abstract class UnionpayRequest extends UnionpayParams {

    private static final long serialVersionUID = -8056051354641899489L;

    private String transType = "01";

    private String merId;

    private String orderNumber;

    private String merReserved;

    @JsonFormat(pattern = "yyyyMMddHHmmss", timezone="GMT+8")
    private Date orderTime;

    public String getTransType() {
        return transType;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getMerReserved() {
        return merReserved;
    }

    public void setMerReserved(String merReserved) {
        this.merReserved = merReserved;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

}