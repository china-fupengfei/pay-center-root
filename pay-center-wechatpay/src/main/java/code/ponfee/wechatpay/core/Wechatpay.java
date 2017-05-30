package code.ponfee.wechatpay.core;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * 微信支付核心类
 */
public final class Wechatpay {

    /**
     * 微信APP ID
     */
    private final String appId;

    /**
     * 微信APP Key
     */
    private final String appKey;

    /**
     * 商户ID
     */
    private final String mchId;

    /**
     * 证书相关
     */
    private final SSLContext sslContext;

    /**
     * 支付组件
     */
    private Pays pays;

    /**
     * 订单组件
     */
    private Orders orders;

    /**
     * 退款组件
     */
    private Refunds refunds;

    /**
     * 通知组件
     */
    private Notifies notifies;

    /**
     * 账单组件
     */
    private Bills bills;

    Wechatpay(String appId, String appKey, String mchId, SSLContext sslContext) {
        this.appId = appId;
        this.appKey = appKey;
        this.mchId = mchId;
        this.sslContext = sslContext;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getMchId() {
        return mchId;
    }

    public SSLSocketFactory getSocketFactory() {
        return sslContext.getSocketFactory();
    }

    /**
     * 初始化操作
     * @return this
     */
    Wechatpay init() {
        pays = new Pays(this);
        orders = new Orders(this);
        refunds = new Refunds(this);
        notifies = new Notifies(this);
        bills = new Bills(this);
        return this;
    }

    /**
     * 调用支付组件
     * @return 支付组件
     */
    public Pays pay() {
        return pays;
    }

    /**
     * 调用订单组件
     * @return 订单组件
     */
    public Orders order() {
        return orders;
    }

    /**
     * 调用退款组件
     * @return 退款组件
     */
    public Refunds refund() {
        return refunds;
    }

    /**
     * 调用通知组件
     * @return 通知组件
     */
    public Notifies notifies() {
        return notifies;
    }

    /**
     * 调用账单组件
     * @return 账单组件
     */
    public Bills bill() {
        return bills;
    }
}
