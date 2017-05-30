package code.ponfee.alipay.model.enums;

/**
 * 签名方式
 */
public enum SignType {
    MD5, DSA, RSA;

    public static SignType from(String name) {
        return Enum.valueOf(SignType.class, name);
    }
}
