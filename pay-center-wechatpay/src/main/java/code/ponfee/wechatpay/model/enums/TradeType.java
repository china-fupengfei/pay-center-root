package code.ponfee.wechatpay.model.enums;

/**
 * 交易类型
 * @since 1.0.0
 */
public enum TradeType {

    /** 公众号支付 */
    JSAPI,

    /** wap支付 */
    @Deprecated WAP,

    /** H5支付 */
    MWEB,

    /** 原生扫码支付 */
    NATIVE,

    /** APP支付 */
    APP,

    /** 刷卡支付 */
    MICROPAY;
}