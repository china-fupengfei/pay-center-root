package code.ponfee.unionpay.core;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import code.ponfee.unionpay.model.enums.SignType;

/**
 * 银联支付
 * @author fupf
 */
public class Unionpay {

    private final String merId;
    private final SignType signMethod;
    private final String md5Key;
    private final RSAPrivateKey merPrivateKey;
    private final RSAPublicKey upopPublicKey;
    private String charset = "UTF-8";

    private Component pay;

    //private WebPay webPay;
    //private AppPay appPay;

    Unionpay(String merId, String md5Key, boolean signIgnoreEmpty) {
        this(merId, md5Key, null, null, signIgnoreEmpty, SignType.MD5);
    }

    Unionpay(String merId, String md5Key, RSAPrivateKey merPrivateKey, RSAPublicKey upopPublicKey, boolean signIgnoreEmpty) {
        this(merId, md5Key, merPrivateKey, upopPublicKey, signIgnoreEmpty, SignType.SHA1withRSA);
    }

    private Unionpay(String merId, String md5Key, RSAPrivateKey merPrivateKey, RSAPublicKey upopPublicKey, boolean signIgnoreEmpty, SignType signType) {
        this.merId = merId;
        this.md5Key = md5Key;
        this.merPrivateKey = merPrivateKey;
        this.upopPublicKey = upopPublicKey;
        this.signMethod = signType;
        pay = new Component(this, signIgnoreEmpty);
    }

    public String getMerId() {
        return merId;
    }

    public SignType getSignMethod() {
        return signMethod;
    }

    public String getMd5Key() {
        return md5Key;
    }

    public RSAPrivateKey getMerPrivateKey() {
        return merPrivateKey;
    }

    public RSAPublicKey getUpopPublicKey() {
        return upopPublicKey;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Component pay() {
        return pay;
    }

}