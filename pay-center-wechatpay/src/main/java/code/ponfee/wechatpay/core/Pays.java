package code.ponfee.wechatpay.core;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

import code.ponfee.commons.util.Dates;
import code.ponfee.wechatpay.exception.WechatpayException;
import code.ponfee.wechatpay.model.common.WechatpayField;
import code.ponfee.wechatpay.model.enums.TradeType;
import code.ponfee.wechatpay.model.pay.AppPayResponse;
import code.ponfee.wechatpay.model.pay.JsPayRequest;
import code.ponfee.wechatpay.model.pay.JsPayResponse;
import code.ponfee.wechatpay.model.pay.PayRequest;

/**
 * 支付组件
 * @author fupf
 */
public final class Pays extends Component {

    /**
     * 统一下单接口
     */
    private static final String PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 联图二维码转换
     */
    private static final String LIANTU_URL = "http://qr.liantu.com/api.php?text=";

    protected Pays(Wechatpay wepay) {
        super(wepay);
    }

    /**
     * JS支付(公众号支付)
     * @param request 支付请求对象
     * @return JsPayResponse对象，或抛WepayException
     */
    public JsPayResponse jsPay(JsPayRequest request) {
        checkJsPayParams(request);
        Map<String, String> payParams = buildPayParams(request, TradeType.JSAPI);
        payParams.put(WechatpayField.OPEN_ID, request.getOpenId());
        Map<String, String> respData = doPay(payParams);
        return buildJsPayResp(respData);
    }

    /**
     * WAP支付(H5支付)
     * @param request 支付请求对象
     * @return JsPayResponse对象，或抛WepayException
     */
    @Deprecated
    public String wapPay(PayRequest request) {
        checkPayParams(request);
        Map<String, String> payParams = buildPayParams(request, TradeType.WAP);
        Map<String, String> respData = doPay(payParams);
        return buildWapPayResp(respData);
    }

    /**
     * 动态二维码支付(NATIVE)
     * @param request 支付请求对象
     * @return 使用联图生成的二维码链接，或抛WepayException
     */
    public String qrPay(PayRequest request) {
        return qrPay(request, Boolean.FALSE);
    }

    /**
     * 动态二维码支付(NATIVE)
     * @param request 支付请求对象
     * @param convert 是否转换为二维码图片链接(使用联图)
     * @return 可访问的二维码链接，或抛WepayException
     */
    public String qrPay(PayRequest request, boolean convert) {
        checkPayParams(request);
        Map<String, String> payParams = buildPayParams(request, TradeType.NATIVE);
        Map<String, String> respData = doPay(payParams);

        String codeUrl = respData.get(WechatpayField.CODE_URL);
        if (convert) {
            try {
                return LIANTU_URL + URLEncoder.encode(codeUrl, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new WechatpayException(e);
            }
        }
        return codeUrl;
    }

    /**
     * 通过访问微信支付返回的跳转url来调起微信支付收，需要在后台配置支付域名， 只允许配置过的域名使用
     * 替换原来的wap支付
     * @param request
     * @param convert
     * @return
     */
    public String mwebPay(PayRequest request, boolean convert) {
        checkPayParams(request);
        Map<String, String> payParams = buildPayParams(request, TradeType.MWEB);
        Map<String, String> respData = doPay(payParams);
        String codeUrl = respData.get(WechatpayField.CODE_URL);
        if (convert) {
            try {
                return LIANTU_URL + URLEncoder.encode(codeUrl, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new WechatpayException(e);
            }
        }
        return codeUrl;
    }

    /**
     * mweb pay
     * @param request
     * @return
     */
    public String mwebPay(PayRequest request) {
        return mwebPay(request, Boolean.FALSE);
    }

    /**
     * app支付
     * @param request 支付请求对象
     * @return AppPayResponse对象，或抛WepayException
     */
    public AppPayResponse appPay(PayRequest request) {
        checkPayParams(request);
        request.setProductId(null); // app支付没有product_id参数
        Map<String, String> payParams = buildPayParams(request, TradeType.APP);
        Map<String, String> respData = doPay(payParams);
        return buildAppPayResp(respData);
    }

    // ================================private methods============================= //
    private Map<String, String> doPay(Map<String, String> payParams) {
        buildSignParams(payParams);
        return doPost(PAY_URL, payParams);
    }

    private JsPayResponse buildJsPayResp(Map<String, String> data) {
        String appId = (String) data.get(WechatpayField.APP_ID);
        String nonceStr = RandomStringUtils.randomAlphanumeric(16);
        String timeStamp = String.valueOf(Dates.seconds());
        String pkg = WechatpayField.prepay_id + "=" + data.get(WechatpayField.prepay_id);

        Map<String, String> params = new HashMap<>();
        params.put(WechatpayField.appId, appId);
        params.put(WechatpayField.timeStamp, timeStamp);
        params.put(WechatpayField.nonceStr, nonceStr);
        params.put(WechatpayField.PACKAGE, pkg);
        params.put(WechatpayField.SIGN_TYPE, "MD5");
        String signed = doSign(params);

        return new JsPayResponse(appId, timeStamp, nonceStr, pkg, "MD5", signed);
    }

    private String buildWapPayResp(Map<String, String> data) {
        // deeplink
        String nonceStr = RandomStringUtils.randomAlphanumeric(16);
        String timeStamp = String.valueOf(Dates.seconds());

        Map<String, String> params = new HashMap<>();
        params.put(WechatpayField.APP_ID, data.get(WechatpayField.APP_ID));
        params.put(WechatpayField.timestamp, timeStamp);
        params.put(WechatpayField.noncestr, nonceStr);
        params.put(WechatpayField.PACKAGE, TradeType.WAP.name());
        params.put(WechatpayField.prepayid, data.get(WechatpayField.prepay_id));
        buildSignParams(params);

        StringBuilder builder = new StringBuilder();
        try {
            for (Entry<String, String> e : params.entrySet()) {
                String val = URLEncoder.encode(e.getValue().toString(), "UTF-8");
                builder.append(e.getKey()).append("=").append(val).append("&");
            }
            builder.deleteCharAt(builder.length() - 1);
            return "weixin://wap/pay?" + URLEncoder.encode(builder.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new WechatpayException(e);
        }
    }

    private AppPayResponse buildAppPayResp(Map<String, String> data) {
        String appId = data.get(WechatpayField.APP_ID);
        String partnerId = data.get(WechatpayField.APP_ID);
        String nonceStr = RandomStringUtils.randomAlphanumeric(16);
        String timeStamp = String.valueOf(Dates.seconds());
        String prepayId = data.get(WechatpayField.prepay_id);

        // 签名
        Map<String, String> params = new HashMap<>();
        params.put(WechatpayField.APP_ID, appId);
        params.put(WechatpayField.noncestr, nonceStr);
        params.put(WechatpayField.partnerid, partnerId);
        params.put(WechatpayField.prepayid, prepayId);
        params.put(WechatpayField.timestamp, timeStamp);
        params.put(WechatpayField.PACKAGE, WechatpayField.APP_PAY_PACKAGE);
        String signed = doSign(params);

        // 封装返回数据
        return new AppPayResponse(appId, partnerId, prepayId, timeStamp, nonceStr, signed);
    }

    /**
     * 检查支付参数合法性
     * @param request 支付请求对象
     */
    private void checkJsPayParams(JsPayRequest request) {
        checkPayParams(request);
        Preconditions.checkArgument(StringUtils.isNotBlank(request.getOpenId()));
    }

    private void checkPayParams(PayRequest request) {
        Preconditions.checkArgument(request != null, "pay detail can't be null");
        Preconditions.checkArgument(StringUtils.isNotBlank(request.getBody()));
        Preconditions.checkArgument(StringUtils.isNotBlank(request.getOutTradeNo()));
        Integer totalFee = request.getTotalFee();
        Preconditions.checkArgument(totalFee != null && totalFee > 0, "totalFee must > 0");
        Preconditions.checkArgument(StringUtils.isNotBlank(request.getClientIp()));
        Preconditions.checkArgument(StringUtils.isNotBlank(request.getNotifyUrl()));
        Preconditions.checkArgument(request.getFeeType() != null, "feeType can't be null");
        Preconditions.checkArgument(StringUtils.isNotBlank(request.getTimeStart()));
    }

    /**
     * 构建公共支付参数
     * @param request 支付请求对象
     * @param tradeType 交易类型
     * @return 支付MAP参数
     */
    private Map<String, String> buildPayParams(PayRequest request, TradeType tradeType) {
        Map<String, String> payParams = new TreeMap<>();

        // 配置参数
        buildConfigParams(payParams);

        // 业务必需参数
        payParams.put(WechatpayField.BODY, request.getBody());
        payParams.put(WechatpayField.OUT_TRADE_NO, request.getOutTradeNo());
        payParams.put(WechatpayField.TOTAL_FEE, request.getTotalFee() + "");
        payParams.put(WechatpayField.SPBILL_CREATE_IP, request.getClientIp());
        payParams.put(WechatpayField.NOTIFY_URL, request.getNotifyUrl());
        payParams.put(WechatpayField.FEE_TYPE, request.getFeeType().name());
        payParams.put(WechatpayField.nonce_str, RandomStringUtils.randomAlphanumeric(16));
        payParams.put(WechatpayField.TIME_START, request.getTimeStart());
        payParams.put(WechatpayField.TRADE_TYPE, tradeType.name());
        putIfNotEmpty(payParams, WechatpayField.PRODUCT_ID, request.getProductId());

        // 业务可选参数
        putIfNotEmpty(payParams, WechatpayField.DEVICE_INFO, request.getDeviceInfo());
        putIfNotEmpty(payParams, WechatpayField.ATTACH, request.getAttach());
        putIfNotEmpty(payParams, WechatpayField.DETAIL, request.getDetail());
        putIfNotEmpty(payParams, WechatpayField.GOODS_TAG, request.getGoodsTag());
        putIfNotEmpty(payParams, WechatpayField.TIME_EXPIRE, request.getTimeExpire());
        putIfNotEmpty(payParams, WechatpayField.LIMIT_PAY, request.getLimitPay());

        return payParams;
    }
    // ================================private methods============================= //
}