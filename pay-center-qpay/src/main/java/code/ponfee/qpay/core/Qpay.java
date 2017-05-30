package code.ponfee.qpay.core;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * qpay
 * @author fupf
 */
public final class Qpay {

    private final String appid;
    private final String mchId;
    private final String md5Key;
    private final String appKey;
    private final SSLContext sslContext;
    private final String opUserPwd;
    private final String opUserId;

    // ------------------组件
    private Pays pays;
    private Notifies notifies;
    private Refunds refunds;
    private Bills bills;

    Qpay(String appid, String mchId, String md5Key, String appKey, SSLContext sslContext, String opUserId, String opUserPwd) {
        this.appid = appid;
        this.mchId = mchId;
        this.md5Key = md5Key;
        this.appKey = appKey;
        this.sslContext = sslContext;
        this.opUserId = opUserId;
        this.opUserPwd = opUserPwd;
    }

    Qpay init() {
        pays = new Pays(this);
        notifies = new Notifies(this);
        refunds = new Refunds(this);
        bills = new Bills(this);
        return this;
    }

    public Pays pays() {
        return pays;
    }

    public Notifies notifies() {
        return notifies;
    }

    public Refunds refunds() {
        return refunds;
    }

    public Bills bills() {
        return bills;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getAppid() {
        return appid;
    }

    public String getMchId() {
        return mchId;
    }

    public String getMd5Key() {
        return md5Key;
    }

    public SSLSocketFactory getSocketFactory() {
        return sslContext.getSocketFactory();
    }

    public String getOpUserPwd() {
        return opUserPwd;
    }

    public String getOpUserId() {
        return opUserId;
    }

}
