package code.ponfee.wechatpay.exception;

/**
 * 微信支付异常
 */
public class WechatpayException extends RuntimeException {

    private static final long serialVersionUID = 6598105006536575407L;

    /**
     * 当微信发生错误时，对应的错误码
     */
    private String errorCode;

    /**
     * 当微信发生错误时，对应的错误消息
     */
    private String errorMsg;

    public WechatpayException(Throwable cause) {
        super(cause);
    }

    public WechatpayException(String errorCode, String errorMsg){
        super("[" + errorCode + "]"+ errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
