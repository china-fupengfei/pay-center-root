package code.ponfee.unionpay.model;

public final class UnionpayField {

    /**
     * 公共参数
     */
    public static final String SIGN = "signature"; // 签名信息
    public static final String SIGN_METHOD = "signMethod"; // 签名方法

    /**
     * 请求参数
     */
    public static final String VERSION = "version"; // 消息版本号
    public static final String CHARSET = "charset"; // 字符编码
    public static final String TRANS_TYPE = "transType"; // 交易类型
    public static final String ORIG_QID = "origQid"; // 原始交易流水号
    public static final String MER_ID = "merId"; // 商户代码
    public static final String MER_ABBR = "merAbbr"; // 商户名称
    public static final String ACQ_CODE = "acqCode"; // 收单机构代码（当商户直接与银联互联网系统相连时，该域可不出现;当商户通过其他系统间接与银联互联网系统相连时，该域必须出现）
    public static final String MER_CODE = "merCode"; // 商户类型
    public static final String COMMODITY_URL = "commodityUrl"; // 商品URL
    public static final String COMMODITY_NAME = "commodityName"; // 商品名称
    public static final String COMMODITY_UNIT_PRICE = "commodityUnitPrice"; // 商品单价
    public static final String COMMODITY_QUANTITY = "commodityQuantity"; // 商品数量
    public static final String COMMODITY_DISCOUNT = "commodityDiscount"; // 优惠信息
    public static final String TRANSFER_FEE = "transferFee"; // 运输费用
    public static final String ORDER_NUMBER = "orderNumber"; // 商户订单号
    public static final String ORDER_AMOUNT = "orderAmount"; // 交易金额
    public static final String ORDER_CURRENCY = "orderCurrency"; // 交易币种
    public static final String ORDER_TIME = "orderTime"; // 交易开始日期时间
    public static final String CUSTOMER_IP = "customerIp"; // 持卡人IP
    public static final String CUSTOMER_NAME = "customerName"; // 持卡人姓名
    public static final String DEFAULT_PAY_TYPE = "defaultPayType"; // 默认支付方式
    public static final String DEFAULT_BANK_NUMBER = "defaultBankNumber"; // 默认银行编码
    public static final String FRONT_END_URL = "frontEndUrl"; // 返回URL
    public static final String BACK_END_URL = "backEndUrl"; // 通知URL
    public static final String MER_RESERVED = "merReserved"; // 商户保留域
    public static final String REQ_RESERVED = "reqReserved"; // 请求保留域

    /**
     * 通知
     */
    public static final String RESP_CODE = "respCode"; // 响应码：00-成功；其它-失败
    public static final String RESP_MSG = "respMsg"; // 响应信息
    public static final String TRACE_NUMBER = "traceNumber"; // 系统跟踪号
    public static final String TRACE_TIME = "traceTime"; // 系统跟踪时间
    public static final String CUP_RESERVED = "cupReserved"; // 系统保留域
    public static final String EXCHANGE_DATE = "exchangeDate"; // 兑换日期
    public static final String EXCHANGE_RATE = "exchangeRate"; // 清算汇率
    public static final String QID = "qid"; // 交易流水号
    public static final String RESP_TIME = "respTime"; // 交易完成时间
    public static final String SETTLE_AMOUNT = "settleAmount"; // 清算金额
    public static final String SETTLE_CURRENCY = "settleCurrency"; // 清算币种
    public static final String SETTLE_DATE = "settleDate"; // 清算日期

    /**
     * 订单查询应答
     */
    public static final String QUERY_RESULT = "queryResult"; // 查询结果：0-交易成功；1-交易失败；2-交易处理中；3-无此交易；
    
    /**
     * APP特有
     */
    public static final String APP_QN = "qn";
    public static final String APP_TN = "tn";
    public static final String APP_TRANS_STATUS = "transStatus"; // 交易状态（APP pay notify)
    public static final String APP_ORDER_TIMEOUT = "orderTimeout"; // APP支付超时时间
    
    /**
     * WEB特有
     */
    public static final String WEB_TRANS_TIMEOUT = "transTimeout"; // WEB支付超时时间
}