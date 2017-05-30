package code.ponfee.unionpay.model;

import java.io.Serializable;

/**
 * 交互参数
 * @author fupf
 */
abstract class UnionpayParams implements Serializable {

    private static final long serialVersionUID = 2242778287411649270L;

    private final String version = "1.0.0";

    private String charset;

    private String signature;

    private String signMethod;

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignMethod() {
        return signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

    public String getVersion() {
        return version;
    }

}
