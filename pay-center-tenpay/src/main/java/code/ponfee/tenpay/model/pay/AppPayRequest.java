package code.ponfee.tenpay.model.pay;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.commons.constrain.Constraint;
import code.ponfee.tenpay.model.TenpayField;

/**
 * 财付通App支付（QQ钱包支付）
 * @author fupf
 */
public class AppPayRequest implements Serializable {

    private static final long serialVersionUID = 3758095943294242403L;

    private String ver;
    private String charset;
    private String sign;

    @JsonProperty(TenpayField.BANK_TYPE)
    private final int bankType = 0;

    @JsonProperty(TenpayField.PAY_CHANNEL)
    private final int payChannel = 1;

    @JsonProperty(TenpayField.FEE_TYPE)
    private final int feeType = 1;

    private String desc;

    @JsonProperty(TenpayField.PURCHASER_ID)
    private String purchaserId;

    @JsonProperty(TenpayField.BARGAINOR_ID)
    private String bargainorId;

    @JsonProperty(TenpayField.SP_BILLNO)
    private String spBillno;

    @JsonProperty(TenpayField.TOTAL_FEE)
    @Constraint(min = 1)
    private int totalFee;

    @JsonProperty(TenpayField.NOTIFY_URL)
    private String notifyUrl;

    private String attach;

    @JsonProperty(TenpayField.TIME_START)
    @JsonFormat(pattern = "yyyyMMddHHmmss", timezone = "GMT+8")
    private Date timeStart;

    @JsonProperty(TenpayField.TIME_EXPIRE)
    @JsonFormat(pattern = "yyyyMMddHHmmss", timezone = "GMT+8")
    private Date timeExpire;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPurchaserId() {
        return purchaserId;
    }

    public void setPurchaserId(String purchaserId) {
        this.purchaserId = purchaserId;
    }

    public String getBargainorId() {
        return bargainorId;
    }

    public void setBargainorId(String bargainorId) {
        this.bargainorId = bargainorId;
    }

    public String getSpBillno() {
        return spBillno;
    }

    public void setSpBillno(String spBillno) {
        this.spBillno = spBillno;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(Date timeExpire) {
        this.timeExpire = timeExpire;
    }

    public int getBankType() {
        return bankType;
    }

    public int getPayChannel() {
        return payChannel;
    }

    public int getFeeType() {
        return feeType;
    }

    public String getVer() {
        return ver;
    }

    public String getCharset() {
        return charset;
    }

    public String getSign() {
        return sign;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}