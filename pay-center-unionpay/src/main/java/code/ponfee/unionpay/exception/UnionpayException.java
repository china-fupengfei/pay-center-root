package code.ponfee.unionpay.exception;

import java.io.Serializable;

/**
 * 异常类
 * @author fupf
 */
public class UnionpayException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1211051959341214590L;

    /**
     * 错误编码
     */
    private int code = 0;

    /**
     * 默认构造函数
     */
    public UnionpayException() {
        super();
    }

    /**
     * @param message 错误消息
     * @param cause 异常原因
     */
    public UnionpayException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message 错误消息
     */
    public UnionpayException(String message) {
        super(message);
    }

    /**
     * @param cause 异常根原因
     */
    public UnionpayException(Throwable cause) {
        super(cause);
    }

    /**
     * @param code 错误编码
     * @param message 错误消息
     * @param cause 异常原因
     */
    public UnionpayException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * @param code 错误编码
     * @param message 错误消息
     */
    public UnionpayException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * @param code 错误编码
     * @param cause 异常原因
     */
    public UnionpayException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    /**
     * 取得错误编码
     * @return
     */
    public int getCode() {
        return code;
    }

}