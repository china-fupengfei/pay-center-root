package code.ponfee.qpay.model;

/**
 * qpay fields
 * @author fupf
 */
public abstract class QpayFields {
    private QpayFields() {};

    /** 通用交互数据 */
    public static final String APPID = "appid"; // 腾讯开放平台审核通过的应用APPID或腾讯公众平台审核通过的公众号APPID
    public static final String MCH_ID = "mch_id"; // QQ钱包分配的商户号  
    public static final String NONCE_STR = "nonce_str"; // 随机字符串，不长于32位 
    public static final String SIGN = "sign"; // 商户签名
    public static final String OUT_TRADE_NO = "out_trade_no"; // 商户订单号

    /** 支付（下单）参数 */
    public static final String BODY = "body"; // 商品描述
    public static final String ATTACH = "attach"; // 附加数据
    public static final String FEE_TYPE = "fee_type"; // 货币类型定义， 默认为人民币：CNY
    public static final String TOTAL_FEE = "total_fee"; // 商户订单总金额，单位为分，只能为整数
    public static final String SPBILL_CREATE_IP = "spbill_create_ip"; // 用户端实际ip
    public static final String TIME_START = "time_start"; // 订单生成时间，格式为yyyyMMddHHmmss，时区为GMT+8 beijing
    public static final String TIME_EXPIRE = "time_expire"; // 订单失效时间，格式为yyyyMMddHHmmss，时区为GMT+8 beijing
    public static final String LIMIT_PAY = "limit_pay"; // 可以针对当前的交易，限制用户的支付方式：no_balance—不准使用余额；no_credit—不准使用信用卡；no_debit—不准使用借记卡；balance_only—只准使用余额；debit_only—只准使用借记卡；NoBindNoBalan—简化注册用户不允许用余额 ；
    public static final String PROMOTION_TAG = "promotion_tag"; // 指定本单参与某个QQ钱包活动或活动档位的标识，包含两个：sale_tag不同活动的匹配标志；level_tag同一活动不同优惠档位的标志。可不填，格式如下（本字段参与签名）：promotion_tag=urlencode(level_tag=xxx&sale_tag=xxx) 
    public static final String TRADE_TYPE = "trade_type"; // APP、JSAPI、NATIVE、H5
    public static final String NOTIFY_URL = "notify_url"; // 接收QQ钱包异步通知回调地址，通知url必须为直接可访问的url，不能携带参数
    public static final String DEVICE_INFO = "device_info"; // 调用接口提交的终端设备号

    /** 通用响应结果 */
    public static final String RETURN_CODE = "return_code"; // SUCCESS/FAIL，此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
    public static final String RETURN_MSG = "return_msg"; // 返回信息
    public static final String RETCODE = "retcode"; // 手Q CGI原始错误码
    public static final String RETMSG = "retmsg"; // 手Q CGI原始错误信息
    public static final String RESULT_CODE = "result_code"; // 业务结果：SUCCESS/FAIL
    public static final String ERR_CODE = "err_code"; // 错误代码
    public static final String ERR_CODE_DESC = "err_code_desc"; // 错误代码描述

    /** 支付请求响应结果 */
    public static final String PREPAY_ID = "prepay_id"; // QQ钱包的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时
    public static final String CODE_URL = "code_url"; // 当trade_type为 NATIVE 时，才会返回该字段，值可以直接转换为二维码，用户使用手机QQ扫描后，将会打开QQ钱包的支付页面

    /** 支付通知结果 */
    public static final String TRADE_STATE = "trade_state"; // 支付状态， 固定值Success
    public static final String BANK_TYPE = "bank_type"; // 银行类型，采用字符串类型的银行卡标识
    public static final String CASH_FEE = "cash_fee"; // 用户本次交易中，实际支付的金额
    public static final String COUPON_FEE = "coupon_fee"; // 本次交易中，QQ钱包提供的优惠金额 
    public static final String TRANSACTION_ID = "transaction_id"; // QQ钱包订单号 
    public static final String TIME_END = "time_end"; // 订单支付时间，格式为yyyyMMddHHmmss
    public static final String OPENID = "openid"; // 用户在商户appid下的唯一标识

    /** 支付通知结果 */
    public static final String TRADE_STATE_DESC = "trade_state_desc"; // 对当前查询订单状态的描述和下一步操作的指引

    /** 退款请求参数 */
    public static final String OUT_REFUND_NO = "out_refund_no"; // 商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
    public static final String REFUND_FEE = "refund_fee"; // 本次退款申请的退回金额。单位：分。币种：人民币
    public static final String OP_USER_ID = "op_user_id"; // 操作员帐号, 默认为商户号 
    public static final String OP_USER_PASSWD = "op_user_passwd"; // 操作员密码的MD5 

    /** 退款请求响应 */
    public static final String REFUND_ID = "refund_id"; // QQ钱包退款单号，在成功受理商户的退款申请后，QQ钱包会为本次退款申请生成该 QQ钱包退款单号 
    public static final String REFUND_CHANNEL = "refund_channel"; // ORIGINAL原路退回；BALANCE退款到余额 
    public static final String COUPON_REFUND_FEE = "coupon_refund_fee"; // 退款申请金额中，优惠或者立减的金额
    public static final String CASH_REFUND_FEE = "cash_refund_fee"; // 退款申请中的现金金额
    public static final String REFUND_STATUS = "refund_status"; // 退款状态

    /** 退款查询响应 */
    public static final String OUT_REFUND_NO_$ = "out_refund_no_$"; // 商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
    public static final String REFUND_ID_$ = "refund_id_$"; // QQ钱包退款单号，在成功受理商户的退款申请后，QQ钱包会为本次退款申请生成该 QQ钱包退款单号 
    public static final String REFUND_CHANNEL_$ = "refund_channel_$"; // ORIGINAL原路退回；BALANCE退款到余额 
    public static final String REFUND_FEE_$ = "refund_fee_$"; // 本次退款申请的退回金额。单位：分  币种：人民币 
    public static final String COUPON_REFUND_FEE_$ = "coupon_refund_fee_$"; // 退款申请金额中，优惠或者立减的金额，n从0开始
    public static final String CASH_REFUND_FEE_$ = "cash_refund_fee_$"; // 退款申请中的现金金额，n从0开始。 
    public static final String REFUND_STATUS_$ = "refund_status_$"; //  SUCCESS退款成功；FAIL退款失败；PROCESSING退款发起成功，正在处理中；CHANGE—转入代发，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，资金回流到商户的现金帐号，需要商户人工干预，通过线下或者财付通转账的方式进行退款。 n从0开始

    /** 对账单下载 */
    public static final String BILL_DATE = "bill_date"; // 格式为yyyymmdd 
    public static final String BILL_TYPE = "bill_type"; //  ALL返回当日所有交易账单，默认值；SUCCESS返回当日支付账单；REFUND返回当日退款账单 
}
