package code.ponfee.unionpay.model.refund;

import code.ponfee.unionpay.model.UnionpayResponse;

public class RefundResponse extends UnionpayResponse {
    private static final long serialVersionUID = 708887773436043452L;

    private String qid;
    private int orderAmount;
    private String orderNumber;

    public String getQid() {
        return qid;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

}
