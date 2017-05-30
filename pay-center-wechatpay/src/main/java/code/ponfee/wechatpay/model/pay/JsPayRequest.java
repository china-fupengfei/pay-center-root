package code.ponfee.wechatpay.model.pay;


/**
 * JS支付请求对象
 */
public class JsPayRequest extends PayRequest {

    private static final long serialVersionUID = 4914745161493839575L;
    /**
     * 用户标识
     */
    private String openId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Override
    public String toString() {
        return "JsPayRequest{" +
                "openId='" + openId + '\'' +
                "} " + super.toString();
    }
}
