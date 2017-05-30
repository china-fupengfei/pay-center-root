package code.ponfee.alipay.model.enums;

/**
 * 退款状态
 */
public enum RefundStatus {

    /**
     * <pre>
     * 退款成功:
     *     全额退款情况：trade_status=TRADE_CLOSED，refund_status=REFUND_SUCCESS
     *     非全额退款情况：trade_status=TRADE_SUCCESS，refund_status=REFUND_SUCCESS
     * </pre>
     */
    REFUND_SUCCESS("REFUND_SUCCESS"),

    /**
     * 退款关闭
     */
    REFUND_CLOSED("REFUND_CLOSED");

    private String value;

    private RefundStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
