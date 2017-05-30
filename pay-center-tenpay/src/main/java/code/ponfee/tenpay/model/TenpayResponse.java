package code.ponfee.tenpay.model;

/**
 * 应答参数 
 * @author fupf
 */
public abstract class TenpayResponse extends TenpayParams {

    private static final long serialVersionUID = -5675107083765660350L;

    private int retcode;

    private String retmsg;

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
