package code.ponfee.alipay.exception;

/**
 * alipay支付异常
 */
public class AlipayException extends RuntimeException {
    private static final long serialVersionUID = -2421850248322223570L;

    public AlipayException() {
        super();
    }

    public AlipayException(String message) {
        super(message);
    }

    public AlipayException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlipayException(Throwable cause) {
        super(cause);
    }

    protected AlipayException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
