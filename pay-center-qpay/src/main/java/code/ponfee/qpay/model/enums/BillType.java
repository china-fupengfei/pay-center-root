package code.ponfee.qpay.model.enums;

/**
 * 账单下载类型
 * @author fupf
 */
public enum BillType {
    ALL, // 返回当日所有交易账单，默认值
    SUCCESS, // 返回当日支付账单
    REFUND, // 返回当日退款账单
    ;
}
