package code.ponfee.qpay.model.pay;

import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.qpay.model.QpayFields;
import code.ponfee.qpay.model.QpayResponse;
import code.ponfee.qpay.model.enums.TradeType;

/**
 * Qpay pay response
 * @author fupf
 */
public class PrepayResponse extends QpayResponse {
    private static final long serialVersionUID = -1815706379148215117L;

    @JsonProperty(QpayFields.TRADE_TYPE)
    private TradeType tradeType;

    @JsonProperty(QpayFields.PREPAY_ID)
    private String prepayId;

    @JsonProperty(QpayFields.CODE_URL)
    private String codeUrl;

    public TradeType getTradeType() {
        return tradeType;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

}