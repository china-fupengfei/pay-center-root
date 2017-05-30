package code.ponfee.qpay.model.enums;

/**
 * 退款渠道
 * @author fupf
 */
public enum RefundChannel {
    ORIGINAL, // 原路退回
    BALANCE, // 退款到余额
    ;

    public static RefundChannel from(String channel) {
        if (channel == null) return null;
        
        return Enum.valueOf(RefundChannel.class, channel);
    }
}
