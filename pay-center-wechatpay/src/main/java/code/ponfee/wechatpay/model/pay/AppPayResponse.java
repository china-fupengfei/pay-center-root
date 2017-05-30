package code.ponfee.wechatpay.model.pay;

import java.io.Serializable;

import code.ponfee.wechatpay.model.common.WechatpayField;

/**
 * 公众号支付响应对象
 */
public class AppPayResponse implements Serializable {

    private static final long serialVersionUID = 2540820967097836895L;

    /**
     * 微信APPID
     */
    private String appid;

    /**
     * 商户Id
     */
    private String partnerid;

    /**
     * 预支付ID
     */
    private String prepayid;

    /**
     * 时间戳(s)
     */
    private String timestamp;

    /**
     * 随机字符串
     */
    private String noncestr;

    /**
     * package
     */
    private final String packageValue = WechatpayField.APP_PAY_PACKAGE;

    /**
     * 签名
     */
    private String sign;

    public AppPayResponse(String appId, String partnerId, String prepayId, String timeStamp, String nonceStr, String paySign) {
        this.appid = appId;
        this.partnerid = partnerId;
        this.prepayid = prepayId;
        this.timestamp = timeStamp;
        this.noncestr = nonceStr;
        this.sign = paySign;
    }

    public String getAppid() {
        return appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public String getSign() {
        return sign;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
