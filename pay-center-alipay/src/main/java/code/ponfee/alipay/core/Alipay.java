package code.ponfee.alipay.core;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import code.ponfee.alipay.model.enums.PayMethod;
import code.ponfee.alipay.model.enums.PaymentType;
import code.ponfee.alipay.model.enums.SignType;

/**
 * 支付宝支付
 */
public final class Alipay {
    /**
     * 支付宝网关
     */
    static final String GATEWAY = "https://mapi.alipay.com/gateway.do?";

    /**
     * 签约的支付宝账号对应的支付宝唯一用户号，以2088开头的16位纯数字组成。
     */
    final String partner;

    /**
     * 签名类型
     */
    final SignType signType;

    /**
     * 商户密钥
     */
    final String md5Key;

    /**
     * 私钥
     */
    final RSAPrivateKey ptnPriKey;

    /**
     * 公钥
     */
    final RSAPublicKey aliPubKey;

    /**
     * 卖家支付宝账号
     */
    String sellerEmail;

    /**
     * 商户网站使用的编码格式，如utf-8、gbk、gb2312等
     */
    String inputCharset = "UTF-8";

    /**
     * 支付类型
     */
    String paymentType = PaymentType.BUY.value();

    /**
     * 默认支付方式
     */
    String payMethod = PayMethod.DIRECT_PAY.value();

    private Pays pays;

    private Refunds refunds;

    private Verifies verifies;

    Alipay(String partner, String md5Key) {
        this.signType = SignType.MD5;
        this.partner = partner;
        this.md5Key = md5Key;

        ptnPriKey = null;
        aliPubKey = null;
    }

    Alipay(String partner, RSAPrivateKey ptnPriKey, RSAPublicKey aliPubKey) {
        this.signType = SignType.RSA;
        this.partner = partner;
        this.ptnPriKey = ptnPriKey;
        this.aliPubKey = aliPubKey;

        md5Key = null;
    }

    Alipay init() {
        pays = new Pays(this);
        refunds = new Refunds(this);
        verifies = new Verifies(this);
        return this;
    }

    public Pays pay() {
        return pays;
    }

    public Refunds refund() {
        return refunds;
    }

    public Verifies verify() {
        return verifies;
    }

}