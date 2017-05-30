package code.ponfee.qpay.core;

import org.apache.commons.lang3.RandomStringUtils;

import code.ponfee.commons.json.Jsons;
import code.ponfee.qpay.model.enums.TradeType;
import code.ponfee.qpay.model.pay.PayQueryRequest;
import code.ponfee.qpay.model.pay.PayQueryResponse;
import code.ponfee.qpay.model.pay.PrepayRequest;
import code.ponfee.qpay.model.pay.PrepayResponse;
import code.ponfee.qpay.model.pay.PrepayResult;

/**
 * 支付组件
 * @author fupf
 */
public final class Pays extends Component {

    /** 统一下单接口 */
    private static final String PAY_URL = "https://qpay.qq.com/cgi-bin/pay/qpay_unified_order.cgi";

    /** 订单查询接口 */
    private static final String QUERY_URL = "https://qpay.qq.com/cgi-bin/pay/qpay_order_query.cgi";

    protected Pays(Qpay qpay) {
        super(qpay);
    }

    /**
     * 原生扫码支付
     * @param req
     * @return
     */
    public String nativePay(PrepayRequest req) {
        req.setTradeType(TradeType.NATIVE);

        return doPrepay(req).getCodeUrl();
    }

    /**
     * 公众号支付
     * @param req
     * @return
     */
    public String jsapiPay(PrepayRequest req) {
        req.setTradeType(TradeType.JSAPI);
        PrepayResult prepay = buildPrepay(doPrepay(req));
        return Jsons.NORMAL.stringify(prepay);
    }

    /**
     * app支付
     * @param req
     * @return
     */
    public String appPay(PrepayRequest req) {
        req.setTradeType(TradeType.APP);
        PrepayResult prepay = buildPrepay(doPrepay(req));
        return Jsons.NORMAL.stringify(prepay);
    }

    /**
     * 订单查询
     * @param req
     * @return
     */
    public PayQueryResponse query(PayQueryRequest req) {
        return super.doHttpPost(QUERY_URL, req, PayQueryResponse.class);
    }

    /**
     * 支付请求
     * @param req
     * @return
     */
    private PrepayResponse doPrepay(PrepayRequest req) {
        req.setNonceStr(RandomStringUtils.randomAlphanumeric(16));
        return doHttpPost(PAY_URL, req, PrepayResponse.class);
    }

    /**
     * 构建预支付请求
     * @param resp
     * @return
     */
    private PrepayResult buildPrepay(PrepayResponse resp) {
        PrepayResult prepay = new PrepayResult(resp.getAppid(), resp.getMchId(), resp.getPrepayId());
        prepay.doBuildSign(qpay.getAppKey());
        return prepay;
    }
}
