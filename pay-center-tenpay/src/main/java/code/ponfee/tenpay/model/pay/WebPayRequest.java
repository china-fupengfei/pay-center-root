package code.ponfee.tenpay.model.pay;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.commons.constrain.Constraint;
import code.ponfee.tenpay.model.TenpayField;
import code.ponfee.tenpay.model.TenpayRequest;

/**
 * 财付通即时到账支付
 * @author fupf
 */
public class WebPayRequest extends TenpayRequest {

    private static final long serialVersionUID = 5825528302729379097L;

    @JsonProperty(TenpayField.BANK_TYPE)
    private String bankType = "DEFAULT"; // 银行类型（默认为DEFAULT）

    @JsonProperty(TenpayField.BODY)
    @Constraint(notBlank = true)
    private String body; // 商品描述

    @JsonProperty(TenpayField.ATTACH)
    private String attach; // 附加数据

    @JsonProperty(TenpayField.RETURN_URL)
    @Constraint(notBlank = true)
    private String returnUrl; // 返回URL

    @JsonProperty(TenpayField.NOTIFY_URL)
    @Constraint(notBlank = true)
    private String notifyUrl; // 通知URL

    @JsonProperty(TenpayField.BUYER_ID)
    private String buyerId; // 买方财付通账号（QQ或EMAIL）

    @JsonProperty(TenpayField.TOTAL_FEE)
    @Constraint(min = 1)
    private int totalFee; // 总金额（分）

    @JsonProperty(TenpayField.FEE_TYPE)
    private int feeType = 1; // 币种（暂只支持1-人民币）

    @JsonProperty(TenpayField.SPBILL_CREATE_IP)
    @Constraint(notBlank = true)
    private String spbillCreateIp; // 用户浏览器端IP

    @JsonProperty(TenpayField.TIME_START)
    @JsonFormat(pattern = "yyyyMMddHHmmss", timezone="GMT+8")
    private Date timeStart; // 订单生成时间（格式为yyyyMMddHHmmss）

    @JsonProperty(TenpayField.TIME_EXPIRE)
    @JsonFormat(pattern = "yyyyMMddHHmmss", timezone="GMT+8")
    private Date timeExpire; // 订单失效时间（格式为yyyyMMddHHmmss）

    @JsonProperty(TenpayField.TRANSPORT_FEE)
    private int transportFee; // 物流费用（分）。如果有值，必须保证transport_fee + product_fee=total_fee

    @JsonProperty(TenpayField.PRODUCT_FEE)
    private int productFee; // 商品费用，单位为分。如果有值，必须保证transport_fee + product_fee=total_fee

    @JsonProperty(TenpayField.GOODS_TAG)
    private String goodsTag; // 商品标记，优惠券时可能用到

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public int getFeeType() {
        return feeType;
    }

    public void setFeeType(int feeType) {
        this.feeType = feeType;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
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

    public int getTransportFee() {
        return transportFee;
    }

    public void setTransportFee(int transportFee) {
        this.transportFee = transportFee;
    }

    public int getProductFee() {
        return productFee;
    }

    public void setProductFee(int productFee) {
        this.productFee = productFee;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

}