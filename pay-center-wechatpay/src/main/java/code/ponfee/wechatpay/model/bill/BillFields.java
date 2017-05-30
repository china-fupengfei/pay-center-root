package code.ponfee.wechatpay.model.bill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import code.ponfee.wechatpay.model.common.WechatpayField;

/**
 * 账单字段
 */
public final class BillFields {

    /**
     * 账单公用起始字段
     */
    private static final List<String> START_FIELDS = Arrays.asList(
        WechatpayField.TRADE_TIME, WechatpayField.APP_ID, WechatpayField.MCH_ID, WechatpayField.SUB_MCH_ID,
        WechatpayField.DEVICE_INFO, WechatpayField.TRANSACTION_ID, WechatpayField.OUT_TRADE_NO, WechatpayField.OPEN_ID,
        WechatpayField.TRADE_TYPE, WechatpayField.TRADE_STATE, WechatpayField.BANK_TYPE, WechatpayField.FEE_TYPE,
        WechatpayField.TOTAL_FEE, WechatpayField.ENTER_RED_PKG_FEE
    );

    private static final List<String> END_FIELDS = Arrays.asList(
        WechatpayField.BODY, WechatpayField.DATA_PKG, WechatpayField.COMMISSION_FEE, WechatpayField.FEE_RATE
    );

    /**
     * 所有订单账单的字段集，顺序与微信返回数据保持一致
     */
    public static final List<String> ALL = initAllFields();

    /**
     * 退款账单的字段集合，顺序与微信返回数据保持一致
     */
    public static final List<String> REFUND = initRefundFields();

    /**
     * 成功账单的字段集合，顺序与微信返回数据保持一致
     */
    public static final List<String> SUCCESS = initSuccessFields();

    /**
     * 账单统计的字段集合，顺序与微信返回数据保持一致
     */
    public static final List<String> COUNT = Arrays.asList(
        WechatpayField.TRADE_TOTAL_COUNT, WechatpayField.TRADE_TOTAL_FEE,
        WechatpayField.REFUND_TOTAL_FEE, WechatpayField.COUPON_REFUND_TOTAL_FEE,
        WechatpayField.COMMISSION_TOTAL_FEE
    );


    private BillFields(){}

    private static List<String> initAllFields() {
        List<String> all = new ArrayList<>();
        startFields(all);
        initCommonRefundFields(all);
        endFields(all);
        return all;
    }

    private static List<String> initRefundFields() {
        List<String> refund = new ArrayList<>();
        startFields(refund);
        refund.add(WechatpayField.REFUND_APPLY_TIME);
        refund.add(WechatpayField.REFUND_SUCCESS_TIME);
        initCommonRefundFields(refund);
        endFields(refund);
        return refund;
    }

    private static void initCommonRefundFields(List<String> fields){
        fields.add(WechatpayField.REFUND_ID);
        fields.add(WechatpayField.OUT_REFUND_NO);
        fields.add(WechatpayField.REFUND_FEE);
        fields.add(WechatpayField.ENTER_RED_PKG_REFUND_FEE);
        fields.add(WechatpayField.REFUND_CHANNEL);
        fields.add(WechatpayField.REFUND_STATUS);
    }

    private static List<String> initSuccessFields() {
        List<String> success = new ArrayList<>();
        startFields(success);
        endFields(success);
        return success;
    }

    private static void startFields(List<String> fields) {
        for (String f : START_FIELDS){
            fields.add(f);
        }
    }

    private static void endFields(List<String> fields) {
        for (String f : END_FIELDS){
            fields.add(f);
        }
    }
}
