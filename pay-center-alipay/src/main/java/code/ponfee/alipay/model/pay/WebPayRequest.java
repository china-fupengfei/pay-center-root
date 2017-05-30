package code.ponfee.alipay.model.pay;

/**
 * 支付宝web支付明细
 */
public class WebPayRequest extends PayRequest {

    private static final long serialVersionUID = -1542442458795168095L;

    protected String exterInvokeIp; // 客服端IP

    //protected String errorNotifyUrl; // 支付宝错误通知跳转

    protected String payMethod; // 支付方式

    protected String defaultBank; // 默认银行

    protected String extraCommonParam; // 公用回传参数

    public WebPayRequest() {}

    public WebPayRequest(String outTradeNo, String subject, String totalFee) {
        super(outTradeNo, subject, totalFee);
    }

    public String getExterInvokeIp() {
        return exterInvokeIp;
    }

    public void setExterInvokeIp(String exterInvokeIp) {
        this.exterInvokeIp = exterInvokeIp;
    }

    public String getDefaultBank() {
        return defaultBank;
    }

    public void setDefaultBank(String defaultBank) {
        this.defaultBank = defaultBank;
    }

    public String getExtraCommonParam() {
        return extraCommonParam;
    }

    public void setExtraCommonParam(String extraCommonParam) {
        this.extraCommonParam = extraCommonParam;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    @Override
    public String toString() {
        return "WebPayDetail {exterInvokeIp=" + exterInvokeIp + ", payMethod=" + payMethod + ", defaultBank=" + defaultBank + ", extraCommonParam="
            + extraCommonParam + "}";
    }

}