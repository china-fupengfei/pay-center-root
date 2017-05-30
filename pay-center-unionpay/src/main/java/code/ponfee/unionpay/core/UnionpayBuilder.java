package code.ponfee.unionpay.core;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * UnionpayBuilder
 * @author fupf
 */
public class UnionpayBuilder {

    private final Unionpay pay;

    private UnionpayBuilder(Unionpay pay) {
        this.pay = pay;
    }

    public static UnionpayBuilder newBuilder(String merId, String md5Key, boolean signIgnoreEmpty) {
        return new UnionpayBuilder(new Unionpay(merId, md5Key, signIgnoreEmpty));
    }

    public static UnionpayBuilder newBuilder(String merId, String md5Key, RSAPrivateKey merPrivateKey, RSAPublicKey upopPublicKey, boolean signIgnoreEmpty) {
        return new UnionpayBuilder(new Unionpay(merId, md5Key, merPrivateKey, upopPublicKey, signIgnoreEmpty));
    }

    public UnionpayBuilder charset(String charset) {
        pay.setCharset(charset);
        return this;
    }

    public Unionpay build() {
        return pay;
    }
}
