package code.ponfee.wechatpay.model.common;

/**
 * 微信支付字段
 */
public final class WechatpayField {

    public static final String SUCCESS = "SUCCESS"; // 成功标识
    public static final String RETURN_CODE = "return_code"; // 响应结果
    public static final String RETURN_MSG = "return_msg"; // 错误响应消息
    public static final String RESULT_CODE = "result_code"; // 业务结果
    public static final String ERR_CODE = "err_code"; // 业务错误码
    public static final String ERR_CODE_DES = "err_code_des"; // 业务错误描述

    /** **************************************** */
    public static final String APP_ID = "appid"; // 公众帐号ID
    public static final String appId = "appId"; // JSAPI支付使用
    /** **************************************** */

    public static final String MCH_ID = "mch_id"; // 微信支付分配的商户号
    public static final String DEVICE_INFO = "device_info"; // 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"

    /** **************************************** */
    public static final String nonce_str = "nonce_str"; // 随机字符串，不长于32位
    public static final String noncestr = "noncestr"; // app支付使用
    public static final String nonceStr = "nonceStr"; // JSAPI支付使用
    /** **************************************** */

    public static final String SIGN = "sign"; // 签名
    public static final String payType = "payType"; // 付款类型
    public static final String SIGN_TYPE = "signType"; // 签名类型
    public static final String BODY = "body"; // 商品或支付单简要描述
    public static final String DETAIL = "detail"; // 商品名称明细列表
    public static final String ATTACH = "attach"; // 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
    public static final String OUT_TRADE_NO = "out_trade_no"; // 商户订单号，商户系统内部的订单号,32个字符内、可包含字母
    public static final String FEE_TYPE = "fee_type"; // 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见
    public static final String TOTAL_FEE = "total_fee"; // 订单总金额，单位为分
    public static final String SPBILL_CREATE_IP = "spbill_create_ip"; // 终端IP，APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
    public static final String TIME_START = "time_start"; // 交易起始时间：订单生成时间，格式为yyyyMMddHHmmss
    public static final String TIME_EXPIRE = "time_expire"; // 交易结束时间：订单失效时间，格式为yyyyMMddHHmmss。 注意：最短失效时间间隔必须大于5分钟
    public static final String GOODS_TAG = "goods_tag"; // 商品标记，代金券或立减优惠功能的参数
    public static final String NOTIFY_URL = "notify_url"; // 接收微信支付异步通知回调地址
    public static final String TRADE_TYPE = "trade_type"; // 交易类型：@see TradeType
    public static final String PRODUCT_ID = "product_id"; // 商品ID：trade_type=NATIVE且为静态链接支付，此参数必传。此id为二维码中包含的商品ID，商户自行定义
    public static final String LIMIT_PAY = "limit_pay"; // 指定支付方式：no_credit--指定不能使用信用卡支付
    public static final String OPEN_ID = "openid"; // 用户标识：trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识

    /** **************************************** */
    public static final String prepay_id = "prepay_id"; // 预支付ID
    public static final String prepayid = "prepayid"; // app支付使用
    //public static final String prepayId = "prepayId";
    /** **************************************** */

    public static final String CODE_URL = "code_url"; // 二维码链接
    public static final String TRANSACTION_ID = "transaction_id"; // 微信订单号
    public static final String IS_SUBSCRIBE = "is_subscribe"; // 是否关注
    public static final String BANK_TYPE = "bank_type"; // 银行
    public static final String TIME_END = "time_end"; // 支付完成时间
    public static final String TRADE_STATE = "trade_state"; // 交易状态
    public static final String TRADE_STATE_DESC = "trade_state_desc"; // 交易状态描述
    public static final String CASH_FEE = "cash_fee"; // 现金支付金额
    public static final String CASH_FEE_TYPE = "cash_fee_type"; // 现金支付货币类型
    public static final String COUPON_FEE = "coupon_fee"; // 代金券或立减优惠金额
    public static final String COUPON_COUNT = "coupon_count"; // 代金券或立减优惠使用数量
    public static final String PACKAGE = "package"; // prepay_id=xxx

    /** **************************************** */
    public static final String timestamp = "timestamp"; // 时间戳
    public static final String timeStamp = "timeStamp"; // JSAPI支付使用
    /** **************************************** */

    public static final String KEY = "key"; // 签名KEY
    public static final String OUT_REFUND_NO = "out_refund_no"; // 商户唯一退款订单号
    public static final String REFUND_FEE = "refund_fee"; // 退款金额
    public static final String OP_USER_ID = "op_user_id"; // 操作员ID
    public static final String REFUND_ID = "refund_id"; // 微信退款单号
    public static final String REFUND_CHANNEL = "refund_channel"; // 退款渠道：{@link code.ponfee.wechatpay.model.enums.RefundChannel}
    public static final String CASH_REFUND_FEE = "cash_refund_fee"; // 现金退款金额
    public static final String COUPON_REFUND_FEE = "coupon_refund_fee"; // 代金券或立减优惠退款金额
    public static final String COUPON_REFUND_COUNT = "coupon_refund_count"; // 代金券或立减优惠使用数量
    public static final String COUPON_REFUND_ID = "coupon_refund_id"; // 代金券或立减优惠ID
    public static final String REFUND_FEE_TYPE = "refund_fee_type"; // 退款货币类型
    public static final String REFUND_COUNT = "refund_count"; // 退款笔数
    public static final String COUPON_REFUND_BATCH_ID = "coupon_refund_batch_id"; // 代金券或立减优惠批次ID

    /** **************************************** */
    public static final String partnerId = "partnerId"; // 商户号
    //public static final String partner_id = "partner_id";
    public static final String partnerid = "partnerid"; // app支付使用
    /** **************************************** */

    public static final String COUPON_BATCH_ID = "coupon_batch_id"; // 代金券或立减优惠批次ID
    public static final String COUPON_ID = "coupon_id"; // 代金券或立减优惠ID
    public static final String BILL_TYPE = "bill_type"; // 账单类型
    public static final String BILL_DATE = "bill_date"; // 账单日期
    public static final String SUB_MCH_ID = "sub_mch_id"; // 子商户ID
    public static final String ENTER_RED_PKG_FEE = "enter_ed_pkg_fee"; // 企业红包金额
    public static final String DATA_PKG = "data_pkg"; // 商户数据包
    public static final String COMMISSION_FEE = "commission_fee"; // 商户手续费
    public static final String FEE_RATE = "fee_rate"; // 费率
    public static final String ENTER_RED_PKG_REFUND_FEE = "enter_red_pkg_refund_fee"; // 企业红包退款金额
    public static final String REFUND_APPLY_TIME = "refund_apply_time"; // 退款申请时间
    public static final String REFUND_SUCCESS_TIME = "refund_success_time"; // 退款成功时间
    public static final String TRADE_TIME = "trade_time"; // 交易时间
    public static final String REFUND_STATUS = "refund_status"; // 退款状态
    public static final String TRADE_TOTAL_COUNT = "trade_total_count"; // 总交易笔数
    public static final String TRADE_TOTAL_FEE = "trade_total_fee"; // 总交易额
    public static final String REFUND_TOTAL_FEE = "refund_total_fee"; // 总退款金额
    public static final String COUPON_REFUND_TOTAL_FEE = "coupon_refund_total_fee"; // 总代金券或立减优惠退款金额
    public static final String COMMISSION_TOTAL_FEE = "commission_total_fee"; // 手续费总金额

    public static final String APP_PAY_PACKAGE = "Sign=WXPay"; // app支付package
}
