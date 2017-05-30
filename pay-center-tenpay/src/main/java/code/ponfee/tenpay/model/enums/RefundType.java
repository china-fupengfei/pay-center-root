package code.ponfee.tenpay.model.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 退款类型
 * @author fupf
 */
@Deprecated
public enum RefundType {
    PARTNER(1), CASH(2), THIRD(3);
    private int value;

    private RefundType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static RefundType from(String value) {
        if (StringUtils.isEmpty(value)) return PARTNER;
        else {
            int val = Integer.parseInt(value);
            switch (val) {
                case 1:
                    return PARTNER;
                case 2:
                    return CASH;
                case 3:
                    return THIRD;
                default:
                    throw new IllegalArgumentException("invalid RefundType value:" + value);
            }
        }
    }

}
