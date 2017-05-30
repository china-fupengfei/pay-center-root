package code.ponfee.unionpay.model.order;

import code.ponfee.unionpay.model.UnionpayResponse;

public class OrderQueryResponse extends UnionpayResponse {

    private static final long serialVersionUID = 4246584488338639760L;

    private String queryResult;

    private String qid;

    private String traceNumber;

    private String traceTime;

    private String settleAmount;

    private String settleCurrency;

    private String settleDate;

    private String exchangeRate;

    private String exchangeDate;

    private String cupReserved;

    public String getQueryResult() {
        return queryResult;
    }

    public void setQueryResult(String queryResult) {
        this.queryResult = queryResult;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getTraceNumber() {
        return traceNumber;
    }

    public void setTraceNumber(String traceNumber) {
        this.traceNumber = traceNumber;
    }

    public String getTraceTime() {
        return traceTime;
    }

    public void setTraceTime(String traceTime) {
        this.traceTime = traceTime;
    }

    public String getSettleAmount() {
        return settleAmount;
    }

    public void setSettleAmount(String settleAmount) {
        this.settleAmount = settleAmount;
    }

    public String getSettleCurrency() {
        return settleCurrency;
    }

    public void setSettleCurrency(String settleCurrency) {
        this.settleCurrency = settleCurrency;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(String exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    public String getCupReserved() {
        return cupReserved;
    }

    public void setCupReserved(String cupReserved) {
        this.cupReserved = cupReserved;
    }

}
