package code.ponfee.qpay.model.enums;

/**
 * 支付方式限制定义
 * @author fupf
 */
public enum LimitPay {
    no_balance, // 不准使用余额
    no_credit, // 不准使用信用卡
    no_debit, // 不准使用借记卡
    balance_only, // 只准使用余额
    debit_only, // 只准使用借记卡
    NoBindNoBalan, // 简化注册用户不允许用余额
    ;
}
