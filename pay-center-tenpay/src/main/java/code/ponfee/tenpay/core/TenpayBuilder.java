package code.ponfee.tenpay.core;

import javax.net.ssl.SSLContext;

/**
 * tenpay构建器
 */
public final class TenpayBuilder {

    private final Tenpay tenpay;

    private TenpayBuilder(Tenpay tenpay) {
        this.tenpay = tenpay;
    }
    
    public static TenpayBuilder newBuilder(String partner, String md5Key, SSLContext sslContext, String appId, String appSecret, String opUserPwd) {
        return new TenpayBuilder(new Tenpay(partner, md5Key, sslContext, appId, appSecret, opUserPwd));
    }

    public TenpayBuilder inputCharset(String inputCharset) {
        tenpay.setInputCharset(inputCharset);
        return this;
    }

    public Tenpay build() {
        return tenpay.init();
    }
}
