package code.ponfee.tenpay.core;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import code.ponfee.tenpay.model.enums.SignType;

/**
 * tenpay
 * @author fupf
 */
public final class Tenpay {

    private final String partner;
    private final String md5Key;
    private final SignType signType;
    private final int signKeyIndex;
    private final SSLContext sslContext;
    private final String appId;
    private final String appSecret;
    private final String opUserPwd;
    private String inputCharset = "UTF-8";

    private Pays pays;
    private Notifies notifies;
    private Refunds refunds;
    private Orders orders;
    private Bills bills;
    
    Tenpay(String partner, String md5Key, SSLContext sslContext, String appId, String appSecret, String opUserPwd) {
        this(partner, md5Key, 1, sslContext, appId, appSecret, opUserPwd);
    }

    Tenpay(String partner, String md5Key, int signKeyIndex, SSLContext sslContext, String appId, String appSecret, String opUserPwd) {
        this.partner = partner;
        this.md5Key = md5Key;
        this.signType = SignType.MD5;
        this.signKeyIndex = signKeyIndex;
        this.sslContext = sslContext;
        this.appId = appId;
        this.appSecret = appSecret;
        this.opUserPwd = opUserPwd;
    }

    Tenpay init() {
        pays = new Pays(this);
        notifies = new Notifies(this);
        refunds = new Refunds(this);
        orders = new Orders(this);
        bills = new Bills(this);
        return this;
    }

    public Pays pay() {
        return pays;
    }

    public Refunds refund() {
        return refunds;
    }

    public Orders order() {
        return orders;
    }

    public Notifies notifies() {
        return notifies;
    }

    public Bills bill() {
        return bills;
    }

    public String getInputCharset() {
        return inputCharset;
    }

    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
    }

    public String getPartner() {
        return partner;
    }

    public String getMd5Key() {
        return md5Key;
    }

    public SignType getSignType() {
        return signType;
    }

    public int getSignKeyIndex() {
        return signKeyIndex;
    }

    public SSLSocketFactory getSocketFactory() {
        return sslContext.getSocketFactory();
    }

    public String getAppId() {
        return appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getOpUserPwd() {
        return opUserPwd;
    }
}