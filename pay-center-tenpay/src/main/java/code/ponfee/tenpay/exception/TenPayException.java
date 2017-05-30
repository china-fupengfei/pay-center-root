package code.ponfee.tenpay.exception;

/**
 * tenpay支付异常
 */
public class TenPayException extends RuntimeException {

    private static final long serialVersionUID = 935364238779821539L;

    /**
     * 返回编码
     */
    private int retcode;

    /**
     * 返回信息
     */
    private String retmsg;

    public TenPayException(Throwable cause) {
        super(cause);
    }

    public TenPayException(int retcode, String retmsg) {
        super("[" + retcode + "]" + retmsg);
        this.retcode = retcode;
        this.retmsg = retmsg;
    }

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public String getRetmsg() {
        return retmsg;
    }

    public void setRetmsg(String retmsg) {
        this.retmsg = retmsg;
    }

}