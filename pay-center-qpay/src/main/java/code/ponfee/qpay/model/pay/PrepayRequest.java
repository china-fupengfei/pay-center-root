package code.ponfee.qpay.model.pay;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.commons.jce.digest.DigestUtils;
import code.ponfee.commons.json.Jsons;
import code.ponfee.qpay.model.QpayFields;
import code.ponfee.qpay.model.QpayRequest;
import code.ponfee.qpay.model.enums.FeeType;
import code.ponfee.qpay.model.enums.LimitPay;
import code.ponfee.qpay.model.enums.TradeType;

/**
 * Qpay pay request
 * @author fupf
 */
public class PrepayRequest extends QpayRequest {
    private static final long serialVersionUID = 4864948014404419339L;

    @JsonProperty(QpayFields.OUT_TRADE_NO)
    private String outTradeNo;
    
    private String body;

    private String attach;

    @JsonProperty(QpayFields.FEE_TYPE)
    private FeeType feeType = FeeType.CNY;

    @JsonProperty(QpayFields.TOTAL_FEE)
    private int totalFee;

    @JsonProperty(QpayFields.SPBILL_CREATE_IP)
    private String spbillCreateIp;

    @JsonProperty(QpayFields.TIME_START)
    @JsonFormat(pattern = "yyyyMMddHHmmss", timezone = "GMT+8")
    private Date timeStart;

    @JsonProperty(QpayFields.TIME_EXPIRE)
    @JsonFormat(pattern = "yyyyMMddHHmmss", timezone = "GMT+8")
    private Date timeExpire;

    @JsonProperty(QpayFields.LIMIT_PAY)
    private LimitPay limitPay;

    @JsonProperty(QpayFields.PROMOTION_TAG)
    private String promotionTag;

    @JsonProperty(QpayFields.TRADE_TYPE)
    private TradeType tradeType;

    @JsonProperty(QpayFields.NOTIFY_URL)
    private String notifyUrl;

    @JsonProperty(QpayFields.DEVICE_INFO)
    private String deviceInfo;

    public String getBody() {
        return body;
    }

    public String getAttach() {
        return attach;
    }

    public FeeType getFeeType() {
        return feeType;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public Date getTimeExpire() {
        return timeExpire;
    }

    public LimitPay getLimitPay() {
        return limitPay;
    }

    public String getPromotionTag() {
        return promotionTag;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public void setFeeType(FeeType feeType) {
        this.feeType = feeType;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeExpire(Date timeExpire) {
        this.timeExpire = timeExpire;
    }

    public void setLimitPay(LimitPay limitPay) {
        this.limitPay = limitPay;
    }

    public void setPromotionTag(String promotionTag) {
        this.promotionTag = promotionTag;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
    
    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public static void main(String[] args) {
        PrepayRequest req = new PrepayRequest();
        req.setAttach("abc");
        String s = Jsons.NORMAL.stringify(req);
        req = Jsons.NORMAL.parse(s, PrepayRequest.class);
        System.out.println(s);
        System.out.println(req.getLimitPay());
        
        System.out.println(DigestUtils.sha512Hex("1".getBytes()));
    }
}
