package code.ponfee.tenpay.model.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 签名类型
 * @author fupf
 */
public enum SignType {
    MD5, RSA;

    public static SignType from(String name) {
        if (StringUtils.isEmpty(name)) return MD5;
        else return Enum.valueOf(SignType.class, name);
    }

}
