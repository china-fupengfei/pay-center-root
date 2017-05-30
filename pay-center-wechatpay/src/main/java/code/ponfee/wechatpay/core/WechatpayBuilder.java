package code.ponfee.wechatpay.core;

import javax.net.ssl.SSLContext;

/**
 * Wepay构建器
 */
public final class WechatpayBuilder {

    private final Wechatpay wechatpay;

    private WechatpayBuilder(Wechatpay wechatPay) {
        this.wechatpay = wechatPay;
    }

    public static WechatpayBuilder newBuilder(String appId, String appSecret, String mchId, SSLContext sslContext) {
        return new WechatpayBuilder(new Wechatpay(appId, appSecret, mchId, sslContext));
    }

    public Wechatpay build() {
        return wechatpay.init();
    }
}
