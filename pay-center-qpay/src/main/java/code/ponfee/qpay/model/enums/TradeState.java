package code.ponfee.qpay.model.enums;

/**
 * 交易状态
 * @author fupf
 */
public enum TradeState {
    SUCCESS, // 交易成功
    REFUND, // 转入退款
    REVOKED, // 订单已撤销
    CLOSED, // 订单已经关闭
    USERPAYING, // 用户正在支付
    ;
}
