package code.ponfee.alipay.model.enums;

/**
 * 支付类型
 */
public enum PayType {

    /**
     * WEB支付
     */
    WEB_PAY("create_direct_pay_by_user"),

    /**
     * WAP支付
     */
    WAP_PAY("alipay.wap.create.direct.pay.by.user"),

    /**
     * APP支付
     */
    APP_PAY("mobile.securitypay.pay"),

    /**
     * 无密退款
     */
    REFUND_NO_PWD("refund_fastpay_by_platform_nopwd"),
    
    /**
     * 有密退款
     */
    REFUND_PWD("refund_fastpay_by_platform_pwd"),

    /**
     * 支付宝通知校验
     */
    NOTIFY_VERIFY("notify_verify"),

    /**
     * 批量付款到支付宝账户有密接口
     */
    BATCH_TRANS_NOTIFY("batch_trans_notify");
    
    private String value;

    private PayType(String value){
        this.value = value;
    }

    public String value(){
        return value;
    }
}
