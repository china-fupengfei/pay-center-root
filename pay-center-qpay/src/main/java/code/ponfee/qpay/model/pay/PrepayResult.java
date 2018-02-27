package code.ponfee.qpay.model.pay;

import java.io.Serializable;
import java.util.Base64;
import java.util.Objects;

import org.apache.commons.lang3.RandomStringUtils;

import code.ponfee.commons.jce.digest.HmacUtils;
import code.ponfee.commons.util.Dates;

/**
 * 预支付结果
 * @author fupf
 */
public class PrepayResult implements Serializable {
    private static final long serialVersionUID = -2131161208279885395L;

    private String appId;
    private String bargainorId;
    private String nonce;
    private String pubAcc;
    private String tokenId;
    private long timeStamp;
    private final String sigType = "HMAC-SHA1";
    private String sig;

    public PrepayResult(String appId, String bargainorId, String tokenId) {
        this(appId, bargainorId, tokenId, null);
    }

    public PrepayResult(String appId, String bargainorId, String tokenId, String pubAcc) {
        this.appId = appId;
        this.bargainorId = bargainorId;
        this.tokenId = tokenId;
        this.pubAcc = pubAcc;
        this.nonce = RandomStringUtils.randomAlphanumeric(16);
        this.timeStamp = Dates.seconds();
    }

    public String getAppId() {
        return appId;
    }

    public String getNonce() {
        return nonce;
    }

    public String getTokenId() {
        return tokenId;
    }

    public String getPubAcc() {
        return pubAcc;
    }

    public String getBargainorId() {
        return bargainorId;
    }

    public String getSigType() {
        return sigType;
    }

    public String getSig() {
        return sig;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public void setPubAcc(String pubAcc) {
        this.pubAcc = pubAcc;
    }

    public void setBargainorId(String bargainorId) {
        this.bargainorId = bargainorId;
    }

    public void doBuildSign(String appKey) {
        StringBuilder builder = new StringBuilder();
        builder.append("appId=").append(toStr(appId));
        builder.append("bargainorId=").append(toStr(bargainorId));
        builder.append("nonce=").append(toStr(nonce));
        builder.append("pubAcc=").append(toStr(pubAcc));
        builder.append("tokenId=").append(toStr(tokenId));
        
        byte[] key = (appKey + "&").getBytes();
        byte[] hashed = HmacUtils.sha1(key, builder.toString().getBytes());
        this.sig = Base64.getEncoder().encodeToString(hashed);
    }

    private static String toStr(Object obj) {
        return Objects.toString(obj, "");
    }
}
