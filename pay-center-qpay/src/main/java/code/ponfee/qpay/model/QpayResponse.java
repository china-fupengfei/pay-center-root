package code.ponfee.qpay.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.qpay.model.enums.ResultCode;
import code.ponfee.qpay.model.enums.ReturnCode;

/**
 * Qpay common response
 * @author fupf
 */
public abstract class QpayResponse extends QpayParams {
    private static final long serialVersionUID = -1484776094911677942L;

    @JsonProperty(QpayFields.RETURN_CODE)
    private ReturnCode returnCode;

    @JsonProperty(QpayFields.RETURN_MSG)
    private String returnMsg;

    private String retcode;

    private String retmsg;

    @JsonProperty(QpayFields.RESULT_CODE)
    private ResultCode resultCode;

    @JsonProperty(QpayFields.ERR_CODE)
    private String errCode;

    @JsonProperty(QpayFields.ERR_CODE_DESC)
    private String errCodeDesc;

    public ReturnCode getReturnCode() {
        return returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public String getRetcode() {
        return retcode;
    }

    public String getRetmsg() {
        return retmsg;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getErrCodeDesc() {
        return errCodeDesc;
    }

    public void setReturnCode(ReturnCode returnCode) {
        this.returnCode = returnCode;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public void setRetmsg(String retmsg) {
        this.retmsg = retmsg;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public void setErrCodeDesc(String errCodeDesc) {
        this.errCodeDesc = errCodeDesc;
    }

}
