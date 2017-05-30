package code.ponfee.unionpay.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 请求应答
 * @author fupf
 */
public class UnionpayResponse extends UnionpayParams {

    private static final long serialVersionUID = 4169262645208322074L;

    private String respCode;
    private String respMsg;

    @JsonFormat(pattern = "yyyyMMddHHmmss", timezone="GMT+8")
    private Date respTime;
    

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public Date getRespTime() {
        return respTime;
    }

    public void setRespTime(Date respTime) {
        this.respTime = respTime;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

}