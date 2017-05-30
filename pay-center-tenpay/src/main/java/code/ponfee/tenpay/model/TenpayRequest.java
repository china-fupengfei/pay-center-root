package code.ponfee.tenpay.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 请求参数
 * @author fupf
 */
public abstract class TenpayRequest extends TenpayParams {

    private static final long serialVersionUID = -4614690335048693306L;

    @JsonProperty(TenpayField.OUT_TRADE_NO)
    private String outTradeNo;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

}
