package code.ponfee.qpay.exception;

/**
 * QpayException
 * @author fupf
 */
public class QpayException extends RuntimeException {
    private static final long serialVersionUID = 7595327328168465533L;

    public QpayException() {
        super();
    }

    public QpayException(String message) {
        super(message);
    }

    public QpayException(String message, Throwable cause) {
        super(message, cause);
    }

    public QpayException(Throwable cause) {
        super(cause);
    }

    protected QpayException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
