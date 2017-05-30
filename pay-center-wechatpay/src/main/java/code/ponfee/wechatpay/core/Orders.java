package code.ponfee.wechatpay.core;

import static code.ponfee.commons.util.Preconditions.checkNotEmpty;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.RandomStringUtils;

import code.ponfee.commons.json.Jsons;
import code.ponfee.wechatpay.model.common.WechatpayField;
import code.ponfee.wechatpay.model.order.WechatpayOrder;

/**
 * 订单组件
 */
public final class Orders extends Component {

    /**
     * 查询订单
     */
    private static final String ORDER_QUERY = "https://api.mch.weixin.qq.com/pay/orderquery";

    /**
     * 关闭订单
     */
    private static final String ORDER_CLOSE = "https://api.mch.weixin.qq.com/pay/closeorder";

    Orders(Wechatpay wepay) {
        super(wepay);
    }

    /**
     * 根据商户订单号查询订单
     * @param outTradeNo 商户订单号
     * @return PayOrder对象，或抛WepayException
     */
    public WechatpayOrder queryOrder(String outTradeNo, String transactionId) {
        Map<String, String> params = new TreeMap<>();
        params.put(WechatpayField.OUT_TRADE_NO, outTradeNo);
        params.put(WechatpayField.TRANSACTION_ID, transactionId);
        
        buildReqParams(params);
        Map<String, String> orderData = doPost(ORDER_QUERY, params);
        WechatpayOrder order = Jsons.NORMAL.parse(Jsons.NORMAL.stringify(orderData), WechatpayOrder.class);
        order.setCoupons(orderData);
        
        return order;
    }

    /**
     * 关闭订单
     * @param outTradeNo 商户订单号
     * @return 关闭成功返回true，或抛WepayException
     */
    public Boolean closeOrder(String outTradeNo) {
        checkNotEmpty(outTradeNo, "outTradeNo");
        Map<String, String> closeParams = new TreeMap<>();
        closeParams.put(WechatpayField.OUT_TRADE_NO, outTradeNo);
        buildReqParams(closeParams);
        return doPost(ORDER_CLOSE, closeParams) != null;
    }

    /**
     * 构建请求参数
     * @param closeParams 关闭参数
     */
    private void buildReqParams(Map<String, String> params) {
        buildConfigParams(params);
        params.put(WechatpayField.nonce_str, RandomStringUtils.randomAlphanumeric(16));
        buildSignParams(params);
    }

}
