package code.ponfee.alipay.core;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import code.ponfee.alipay.model.enums.PayMethod;
import code.ponfee.alipay.model.enums.PaymentType;

/**
 * alipay builder
 */
public final class AlipayBuilder {

    private final Alipay alipay;

    private AlipayBuilder(Alipay alipay) {
        this.alipay = alipay;
    }

    public static AlipayBuilder newBuilder(String partner, String md5Key) {
        return new AlipayBuilder(new Alipay(partner, md5Key));
    }

    public static AlipayBuilder newBuilder(String partner, RSAPrivateKey privateKey, RSAPublicKey publicKey) {
        return new AlipayBuilder(new Alipay(partner, privateKey, publicKey));
    }

    public Alipay build() {
        return alipay.init();
    }

    /**
     * Set email account
     * @param email email account
     * @return this
     */
    public AlipayBuilder sellerEmail(String sellerEmail) {
        alipay.sellerEmail = sellerEmail;
        return this;
    }

    /**
     * Set the charset of the merchant，ex. utf-8、gbk、gb2312，default is utf-8
     * @param charset charset
     * @return this
     */
    public AlipayBuilder inputCharset(String charset) {
        alipay.inputCharset = charset;
        return this;
    }

    /**
     * Set the Payment type
     * @see code.ponfee.alipay.api.model.enums.PaymentType
     * @param type the payment type
     * @return this
     */
    public AlipayBuilder paymentType(PaymentType type) {
        alipay.paymentType = type.value();
        return this;
    }

    /**
     * 设置默认支付方式，默认BUY
     * @param method 支付方式
     * @return this
     */
    public AlipayBuilder payMethod(PayMethod method) {
        alipay.payMethod = method.value();
        return this;
    }

}