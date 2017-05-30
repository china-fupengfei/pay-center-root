package code.ponfee.pay.exception;

import java.io.Serializable;

/**
 * 支付异常类
 * @author fupf
 */
public class PayException extends Exception implements Serializable {
    private static final long serialVersionUID = 8778108449346371909L;
    
    /**
     * 错误编码
     */
    private int code = 0;

    /**
     * 默认构造函数
     */
    public PayException() {
        super();
    }

    /**
     * @param message 错误消息
     * @param cause 异常原因
     */
    public PayException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message 错误消息
     */
    public PayException(String message) {
        super(message);
    }

    /**
     * @param cause 异常根原因
     */
    public PayException(Throwable cause) {
        super(cause);
    }

    /**
     * @param code 错误编码
     * @param message 错误消息
     * @param cause 异常原因
     */
    public PayException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * @param code 错误编码
     * @param message 错误消息
     */
    public PayException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * @param code 错误编码
     * @param cause 异常原因
     */
    public PayException(int code, Throwable cause) {
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

    /**
     * 设置错误编码
     * @param code
     */
    public void setCode(int code) {
        this.code = code;
    }

}