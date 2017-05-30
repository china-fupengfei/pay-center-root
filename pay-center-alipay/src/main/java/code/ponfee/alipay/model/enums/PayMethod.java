package code.ponfee.alipay.model.enums;

/**
 * 默认支付方式
 */
public enum PayMethod {

    /**
     * 信用支付
     */
    CREDIT_PAY("creditPay"),

    /**
     * 余额支付
     */
    DIRECT_PAY("directPay"),

    /**
     * 网银支付
     */
    BANK_PAY("bankPay"),

    /**
     * 信用卡快捷
     */
    CREDIT_CARD_EXPRESS("creditCardExpress"),

    /**
     * 借记卡快捷
     */
    DEBIT_CARD_EXPRESS("debitCardExpress"),

    /**
     * 卡通
     */
    CARTOON("cartoon"),

    /**
     * 红包
     */
    COUPON("coupon"),

    /**
     * 积分
     */
    POINT("point"),

    /**
     * 购物券
     */
    VOUCHER("voucher");

    private String value;

    private PayMethod(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
