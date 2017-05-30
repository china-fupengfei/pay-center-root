package code.ponfee.tenpay.model.pay;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.tenpay.model.TenpayField;

/**
 * 手Q支付响应类
 * @author fupf
 */
public class AppPayResponse implements Serializable {

    private static final long serialVersionUID = 4205485918318399179L;

    private String appid;

    @JsonProperty(TenpayField.BARGAINOR_ID)
    private String bargainorId;

    private String sign;

    @JsonProperty(TenpayField.TOKEN_ID)
    private String tokenId;

    @JsonProperty(TenpayField.ERR_INFO)
    private String errInfo;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getBargainorId() {
        return bargainorId;
    }

    public void setBargainorId(String bargainorId) {
        this.bargainorId = bargainorId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getErrInfo() {
        return errInfo;
    }

    public void setErrInfo(String errInfo) {
        this.errInfo = errInfo;
    }

}