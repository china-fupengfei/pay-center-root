package code.ponfee.tenpay.model;

public class TenpayField {
    /** 返回参数 */
    public static final String RET_CODE = "retcode";

    public static final String RET_MSG = "retmsg";

    /** 协议参数 */
    public static final String SIGN_TYPE = "sign_type"; // 签名方式（默认MD5）

    public static final String SERVICE_VERSION = "service_version"; // 接口版本（默认为1.0）

    public static final String INPUT_CHARSET = "input_charset"; // 字符集（默认GBK）

    public static final String SIGN = "sign"; // 签名

    public static final String SIGN_KEY_INDEX = "sign_key_index"; // 密钥序号（多密钥支持的密钥序号，默认1）

    /** 业务参数 */
    // ---支付
    public static final String BANK_TYPE = "bank_type"; // 银行类型（默认为DEFAULT）

    public static final String BODY = "body"; // 商品描述

    public static final String ATTACH = "attach"; // 附加数据

    public static final String RETURN_URL = "return_url"; // 返回URL

    public static final String NOTIFY_URL = "notify_url"; // 通知URL

    public static final String BUYER_ID = "buyer_id"; // 买方财付通账号

    public static final String PARTNER = "partner"; // 商户号

    public static final String OUT_TRADE_NO = "out_trade_no"; // 商户订单号

    public static final String TOTAL_FEE = "total_fee"; // 总金额

    public static final String FEE_TYPE = "fee_type"; // 币种

    public static final String SPBILL_CREATE_IP = "spbill_create_ip"; // 用户IP

    public static final String TIME_START = "time_start"; // 订单生成时间（格式为yyyyMMddHHmmss）

    public static final String TIME_EXPIRE = "time_expire"; // 订单失效时间（格式为yyyyMMddHHmmss）

    public static final String TRANSPORT_FEE = "transport_fee"; // 物流费用，单位为分。如果有值，必须保证transport_fee + product_fee=total_fee

    public static final String PRODUCT_FEE = "product_fee"; // 商品费用，单位为分。如果有值，必须保证transport_fee + product_fee=total_fee

    public static final String GOODS_TAG = "goods_tag"; // 商品标记，优惠券时可能用到

    // -- 通知
    public static final String TRADE_MODE = "trade_mode"; // 交易模式（1-即时到账，其他保留）

    public static final String TRADE_STATE = "trade_state"; // 交易状态（0-成功，其他保留）

    public static final String PAY_INFO = "pay_info"; // 支付结果信息，支付成功时为空

    public static final String BANK_BILLNO = "bank_billno"; // 银行订单号，若为财付通余额支付则为空

    public static final String NOTIFY_ID = "notify_id"; // 支付结果通知id

    public static final String TRANSACTION_ID = "transaction_id"; // 财付通交易号

    public static final String TIME_END = "time_end"; // 支付完成时间（格式为yyyyMMddhhmmss）

    @Deprecated
    public static final String DISCOUNT = "discount"; // 折扣价格，单位分，如果有值，通知的total_fee + discount = 请求的total_fee。目前discount已不再使用。

    public static final String BUYER_ALIAS = "buyer_alias"; // 买家别名，对应买家账号的一个加密串

    // -- 订单
    @Deprecated
    public static final String USE_SPBILL_NO_FLAG = "use_spbill_no_flag"; // 若通过接口(https://www.tenpay.com/cgi-bin/v1.0/pay_gate.cgi) 支付的商户订单号来查询，则取值为1；而通过本文档的支付接口的，则无需传值。

    public static final String IS_SPLIT = "is_split"; // 是否分账，false无分账，true分账

    public static final String IS_REFUND = "is_refund"; // 是否退款，false无退款，true退款

    public static final String CASH_TICKET_FEE = "cash_ticket_fee"; // 彩贝积分金额，单位分，表示用户使用多少彩贝积分金额支付该笔订单。

    // -- 退款
    public static final String OUT_REFUND_NO = "out_refund_no"; // 商户退款单号

    public static final String REFUND_FEE = "refund_fee"; // 退款总金额,单位为分,可以做部分退款

    public static final String OP_USER_ID = "op_user_id"; // 操作员帐号,默认为商户号

    public static final String OP_USER_PASSWD = "op_user_passwd"; // 账号密码，明文密码做MD5后的值

    public static final String RECV_USER_ID = "recv_user_id"; // 转账退款接收退款的财付通帐号（只有退银行失败，资金转入商户号现金账号时才需要）

    public static final String RECCV_USER_NAME = "reccv_user_name"; // 转账退款接收退款的姓名

    public static final String REFUND_TYPE = "refund_type"; // 退款类型

    public static final String REFUND_ID = "refund_id"; // 财付通退款单号

    public static final String REFUND_CHANNEL = "refund_channel"; // 退款渠道：0-退到财付通；1-退到银行；

    public static final String REFUND_STATE = "refund_state"; // 退款状态
    
    public static final String REFUND_STATUS = "refund_status"; // 退款状态

    public static final String REFUND_COUNT = "refund_count"; // 退款记录数

    public static final String REFUND_DETAILS = "refund_details"; // 退款明细（自定义）

    public static final String REFUND_TIME_BEGIN = "refund_time_begin"; // 退款明细

    public static final String REFUND_TIME = "refund_time"; // 退款明细
    
    
    
    
    //////////////////////Qpay/////////////////////////////
    // 支付请求参数
    public static final String VER = "ver";
    public static final String CHARSET = "charset";
    public static final String PAY_CHANNEL = "pay_channel";
    public static final String DESC = "desc";
    public static final String PURCHASER_ID = "purchaser_id";
    public static final String BARGAINOR_ID = "bargainor_id";
    public static final String SP_BILLNO = "sp_billno";
    public static final String PAY_RESULT = "pay_result"; // 支付结果
    
    // 下载
    public static final String APP_ID = "appid";
    public static final String TOKEN_ID = "token_id";
    public static final String ERR_INFO = "err_info";

}