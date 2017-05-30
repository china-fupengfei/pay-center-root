package code.ponfee.unionpay.model.pay;

import java.util.Date;
import java.util.Map;

import code.ponfee.commons.json.Jsons;
import code.ponfee.unionpay.model.UnionpayRequest;

/**
 * web 支付
 * @author fupf
 */
public class WebPayRequest extends UnionpayRequest {

    private static final long serialVersionUID = 5828974060432449778L;

    private String origQid;

    private String merAbbr;

    private String acqCode;

    private String merCode;

    private String commodityUrl;

    private String commodityName;

    private int commodityUnitPrice;

    private int commodityQuantity = 1;

    private int commodityDiscount = 0;

    private int transferFee = 0;

    private int orderAmount;

    private final String orderCurrency = "156";

    private String customerIp;

    private String customerName;

    private String defaultPayType;

    private String defaultBankNumber;

    private int transTimeout; // time millis

    private String frontEndUrl;

    private String backEndUrl;

    public String getOrigQid() {
        return origQid;
    }

    public void setOrigQid(String origQid) {
        this.origQid = origQid;
    }

    public String getMerAbbr() {
        return merAbbr;
    }

    public void setMerAbbr(String merAbbr) {
        this.merAbbr = merAbbr;
    }

    public String getAcqCode() {
        return acqCode;
    }

    public void setAcqCode(String acqCode) {
        this.acqCode = acqCode;
    }

    public String getMerCode() {
        return merCode;
    }

    public void setMerCode(String merCode) {
        this.merCode = merCode;
    }

    public String getCommodityUrl() {
        return commodityUrl;
    }

    public void setCommodityUrl(String commodityUrl) {
        this.commodityUrl = commodityUrl;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public int getCommodityUnitPrice() {
        return commodityUnitPrice;
    }

    public void setCommodityUnitPrice(int commodityUnitPrice) {
        this.commodityUnitPrice = commodityUnitPrice;
    }

    public int getCommodityQuantity() {
        return commodityQuantity;
    }

    public void setCommodityQuantity(int commodityQuantity) {
        this.commodityQuantity = commodityQuantity;
    }

    public int getCommodityDiscount() {
        return commodityDiscount;
    }

    public void setCommodityDiscount(int commodityDiscount) {
        this.commodityDiscount = commodityDiscount;
    }

    public int getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(int transferFee) {
        this.transferFee = transferFee;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getCustomerIp() {
        return customerIp;
    }

    public void setCustomerIp(String customerIp) {
        this.customerIp = customerIp;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDefaultPayType() {
        return defaultPayType;
    }

    public void setDefaultPayType(String defaultPayType) {
        this.defaultPayType = defaultPayType;
    }

    public String getDefaultBankNumber() {
        return defaultBankNumber;
    }

    public void setDefaultBankNumber(String defaultBankNumber) {
        this.defaultBankNumber = defaultBankNumber;
    }

    public int getTransTimeout() {
        return transTimeout;
    }

    public void setTransTimeout(int transTimeout) {
        this.transTimeout = transTimeout;
    }

    public String getFrontEndUrl() {
        return frontEndUrl;
    }

    public void setFrontEndUrl(String frontEndUrl) {
        this.frontEndUrl = frontEndUrl;
    }

    public String getBackEndUrl() {
        return backEndUrl;
    }

    public void setBackEndUrl(String backEndUrl) {
        this.backEndUrl = backEndUrl;
    }

    public String getOrderCurrency() {
        return orderCurrency;
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        WebPayRequest r = new WebPayRequest();
        r.setOrderTime(new Date());
        String s = Jsons.NORMAL.stringify(r);
        Map<String, Object> m = Jsons.NORMAL.parse(s, Map.class);
        System.out.println(m.get("orderTime"));
        
        
        r = Jsons.NORMAL.parse(s, WebPayRequest.class);
        System.out.println(r.getOrderTime());
        s = Jsons.NORMAL.stringify(r);
        m = Jsons.NORMAL.parse(s, Map.class);
        System.out.println(m.get("orderTime"));
    }
}
