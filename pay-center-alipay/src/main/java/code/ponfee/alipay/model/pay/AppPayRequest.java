package code.ponfee.alipay.model.pay;

import code.ponfee.alipay.model.enums.GoodsType;

/**
 * 支付宝APP支付明细
 */
public class AppPayRequest extends PayRequest {

    private static final long serialVersionUID = 7265488308580697604L;

    /**
     * 客户端号，标识客户端
     */
    private String appId;

    /**
     * 客户端来源
     */
    private String appenv;

    /**
     * 是否发起实名校验
     */
    private String rnCheck;

    /**
     * 授权令牌(32)
     */
    private String externToken;

    /**
     * 商户业务扩展参数
     */
    private String outContext;

    /**
     * 商品类型
     */
    private GoodsType goodsType;

    public AppPayRequest() {}

    public AppPayRequest(String outTradeNo, String orderName, String totalFee, String body) {
        super(outTradeNo, orderName, totalFee);
        super.body = body;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppenv() {
        return appenv;
    }

    public void setAppenv(String appenv) {
        this.appenv = appenv;
    }

    public String getRnCheck() {
        return rnCheck;
    }

    public void setRnCheck(String rnCheck) {
        this.rnCheck = rnCheck;
    }

    public String getExternToken() {
        return externToken;
    }

    public void setExternToken(String externToken) {
        this.externToken = externToken;
    }

    public String getOutContext() {
        return outContext;
    }

    public void setOutContext(String outContext) {
        this.outContext = outContext;
    }

    public GoodsType getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(GoodsType goodsType) {
        this.goodsType = goodsType;
    }

    @Override
    public String toString() {
        return "AppPayDetail {appId=" + appId + ", appenv=" + appenv + ", rnCheck=" + rnCheck + ", externToken=" + externToken + ", outContext=" + outContext
            + ", goodsType=" + goodsType + "}";
    }

}