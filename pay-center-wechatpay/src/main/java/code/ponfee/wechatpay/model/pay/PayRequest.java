package code.ponfee.wechatpay.model.pay;

import java.io.Serializable;

import code.ponfee.wechatpay.model.enums.FeeType;

/**
 * 支付请求对象
 */
public class PayRequest implements Serializable {

    private static final long serialVersionUID = 3080074058147460638L;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 业务系统唯一订单号
     */
    private String outTradeNo;

    /**
     * 总金额(分)
     */
    private Integer totalFee;

    /**
     * 客户端IP
     */
    private String clientIp;

    /**
     * 通知URL
     */
    private String notifyUrl;

    /**
     * 设备号
     */
    private String deviceInfo;

    /**
     * 附加信息
     */
    private String attach;

    /**
     * 商品详情
     */
    private String detail;

    /**
     * 货币类型
     */
    private FeeType feeType = FeeType.CNY;

    /**
     * 交易开始时间
     */
    private String timeStart;

    /**
     * 交易结束时间
     */
    private String timeExpire;

    /**
     * 商品标记
     */
    private String goodsTag;

    /**
     * 指定支付方式
     */
    private String limitPay;

    /**
     * 商品ID（app支付没有该参数）
     */
    private String productId;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public FeeType getFeeType() {
        return feeType;
    }

    public void setFeeType(FeeType feeType) {
        this.feeType = feeType;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(String timeExpire) {
        this.timeExpire = timeExpire;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

    public String getLimitPay() {
        return limitPay;
    }

    public void setLimitPay(String limitPay) {
        this.limitPay = limitPay;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"body\":\"").append(body).append("\", \"outTradeNo\":\"").append(outTradeNo);
        builder.append("\", \"totalFee\":\"").append(totalFee).append("\", \"clientIp\":\"").append(clientIp);
        builder.append("\", \"notifyUrl\":\"").append(notifyUrl).append("\", \"deviceInfo\":\"").append(deviceInfo);
        builder.append("\", \"attach\":\"").append(attach).append("\", \"detail\":\"").append(detail);
        builder.append("\", \"feeType\":\"").append(feeType).append("\", \"timeStart\":\"").append(timeStart);
        builder.append("\", \"timeExpire\":\"").append(timeExpire).append("\", \"goodsTag\":\"").append(goodsTag);
        builder.append("\", \"limitPay\":\"").append(limitPay).append("\", \"productId\":\"").append(productId);
        builder.append("\"}  ");
        return builder.toString();
    }

}
