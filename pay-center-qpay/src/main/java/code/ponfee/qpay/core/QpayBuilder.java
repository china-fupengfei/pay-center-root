package code.ponfee.qpay.core;

import javax.net.ssl.SSLContext;

/**
 * QpayBuilder
 * @author fupf
 */
public final class QpayBuilder {

    private final Qpay qpay;

    private QpayBuilder(Qpay qpay) {
        this.qpay = qpay;
    }

    public static QpayBuilder newBuilder(String appid, String mchId, String md5Key, String appKey, SSLContext sslContext, String opUserId, String opUserPwd) {
        return new QpayBuilder(new Qpay(appid, mchId, md5Key, appKey, sslContext, opUserId, opUserPwd));
    }

    public Qpay build() {
        return qpay.init();
    }
}
