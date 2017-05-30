package code.ponfee.alipay.model.pay;

import java.io.Serializable;

/**
 * 支付宝PC和WAP公共支付明细
 */
public abstract class PayRequest implements Serializable {

    private static final long serialVersionUID = 5892926888312847503L;

    /**
     * 我方唯一订单号
     */
    protected String outTradeNo;

    /**
     * 商品名称
     */
    protected String subject;

    /**
     * 商品金额(元)
     */
    protected String totalFee;

    /**
     * 描述(APP支付时必填)
     */
    protected String body;

    /**
     * 支付宝后置通知url，若为空，则使用Alipay类中的notifyUrl
     */
    protected String notifyUrl;

    /**
     * 支付宝前端跳转url，若为空，则使用Alipay类中的returnUrl
     */
    protected String returnUrl;
    
    protected String expireTime; // it_b_pay

    public PayRequest() {}

    public PayRequest(String outTradeNo, String subject, String totalFee) {
        this.outTradeNo = outTradeNo;
        this.subject = subject;
        this.totalFee = totalFee;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return "PayDetail {outTradeNo=" + outTradeNo + ", subject=" + subject + ", totalFee=" + totalFee + ", body=" + body + ", notifyUrl=" + notifyUrl
            + ", returnUrl=" + returnUrl + "}";
    }

}
