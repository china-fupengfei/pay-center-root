package code.ponfee.qpay.model.enums;

/**
 * 交易类型
 * @author fupf
 */
public enum TradeType {
    APP, // APP支付
    JSAPI, // 公众号支付
    NATIVE, // 原生扫码支付
    MICROPAY, //  付款码支付
    ;
}
