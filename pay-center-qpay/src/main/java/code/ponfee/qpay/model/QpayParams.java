package code.ponfee.qpay.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Qpay common params
 * @author fupf
 */
abstract class QpayParams implements Serializable {
    private static final long serialVersionUID = -2545754932723807702L;

    private String appid;

    @JsonProperty(QpayFields.MCH_ID)
    private String mchId;

    @JsonProperty(QpayFields.NONCE_STR)
    private String nonceStr;

    private String sign;

    public String getAppid() {
        return appid;
    }

    public String getMchId() {
        return mchId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
