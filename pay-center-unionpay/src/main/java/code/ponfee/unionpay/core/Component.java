package code.ponfee.unionpay.core;

import static java.net.URLDecoder.decode;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import code.ponfee.commons.http.Http;
import code.ponfee.commons.http.HttpParams;
import code.ponfee.commons.jce.hash.HashUtils;
import code.ponfee.commons.jce.security.RSACryptor;
import code.ponfee.commons.json.Jsons;
import code.ponfee.commons.reflect.GenericUtils;
import code.ponfee.commons.util.Bytes;
import code.ponfee.unionpay.exception.UnionpayException;
import code.ponfee.unionpay.model.UnionpayField;
import code.ponfee.unionpay.model.UnionpayRequest;
import code.ponfee.unionpay.model.order.OrderQueryRequest;
import code.ponfee.unionpay.model.order.OrderQueryResponse;
import code.ponfee.unionpay.model.pay.AppPayRequest;
import code.ponfee.unionpay.model.pay.WebPayRequest;
import code.ponfee.unionpay.model.refund.RefundRequest;
import code.ponfee.unionpay.model.refund.RefundResponse;

/**
 * 抽象组件
 * @author fupf
 */
public class Component {

    private static final List<String> SIGN_EXCLUDES = Arrays.asList(UnionpayField.SIGN, UnionpayField.SIGN_METHOD);
    private static final String WEB_PAY_URL = "https://unionpaysecure.com/api/Pay.action";
    private static final String APP_PAY_URL = "https://mgate.unionpay.com/gateway/merchant/trade";

    private static final String REFUND_URL = "https://besvr.unionpaysecure.com/api/BSPay.action";
    private static final String QUERY_URL = "https://query.unionpaysecure.com/api/Query.action";

    protected final Unionpay pay;

    private final boolean signIgnoreEmpty; // 是否忽略空值签名

    public Component(Unionpay pay, boolean signIgnoreEmpty) {
        this.pay = pay;
        this.signIgnoreEmpty = signIgnoreEmpty;
    }

    /**
     * web支付
     * @param request
     * @return
     */
    public String webPay(WebPayRequest request) {
        request.setOrderTime(new Date());
        Map<String, String> params = buildReqParams(request);
        return HttpParams.buildForm(WEB_PAY_URL, params);
    }

    /**
     * app支付
     * @param request
     * @return
     */
    public String appPay(AppPayRequest request) {
        request.setOrderTime(new Date());
        Map<String, String> params = buildReqParams(request);
        Map<String, String> resp = doPost(APP_PAY_URL, params);
        return resp.get(UnionpayField.APP_TN);
    }

    /**
     * 构建请求参数
     * @param request
     */
    @SuppressWarnings("unchecked")
    protected Map<String, String> buildReqParams(UnionpayRequest request) {
        // 组装参数
        request.setCharset(pay.getCharset());
        request.setMerId(pay.getMerId());
        request.setSignMethod(pay.getSignMethod().name());

        String json;
        if (signIgnoreEmpty) {
            json = Jsons.NON_EMPTY.stringify(request);
        } else {
            json = Jsons.NORMAL.stringify(request);
        }
        Map<String, String> params = GenericUtils.covariant(Jsons.NORMAL.parse(json, Map.class));
        buildSignParams(params);
        return params;
    }

    /**
     * 构建签名参数
     * @param params 支付参数
     */
    protected void buildSignParams(final Map<String, String> params) {
        String sign = doSignParam(params);
        params.put(UnionpayField.SIGN, sign);
    }

    /**
     * http post请求
     * @param url
     * @param params
     * @return
     */
    protected Map<String, String> doPost(String url, Map<String, String> params) {
        String respStr = Http.post(url).data(params, pay.getCharset()).request();
        Map<String, String> respMap = parseResp(respStr);
        if (StringUtils.isNotBlank(respMap.get(UnionpayField.RESP_MSG))) {
            try {
                String respMsg = decode(respMap.get(UnionpayField.RESP_MSG), pay.getCharset());
                respMap.put(UnionpayField.RESP_MSG, respMsg);
            } catch (UnsupportedEncodingException e) {
                throw new UnionpayException("url decode error: " + respMap.get(UnionpayField.RESP_MSG));
            }
        }

        if (!"00".equals(respMap.get(UnionpayField.RESP_CODE))) {
            throw new UnionpayException(respMap.get(UnionpayField.RESP_MSG));
        }

        if (!verifySign(respMap)) {
            throw new UnionpayException("invalid sign.");
        }
        return respMap;
    }

    /**
     * http post请求
     * @param url
     * @param params
     * @param clazz
     * @return
     */
    protected <T> T doPost(String url, Map<String, String> params, Class<T> clazz) {
        Map<String, String> resp = doPost(url, params);
        return Jsons.NORMAL.parse(Jsons.NORMAL.stringify(resp), clazz);
    }

    private Map<String, String> parseResp(String resp) {
        Map<String, String> result = new HashMap<>();

        String regex = "(.*?" + UnionpayField.CUP_RESERVED + "\\=)(\\{[^}]+\\})(.*)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(resp);
        for (String str : resp.replaceFirst(regex, "$1$3").split("&")) {
            if ((UnionpayField.CUP_RESERVED + "=").equals(str)) {
                result.put(UnionpayField.CUP_RESERVED, matcher.find() ? matcher.group(2) : "");
            } else {
                String[] params = str.split("=");
                result.put(params[0], params.length > 1 ? str.substring(params[0].length() + 1) : "");
            }
        }

        return result;
    }

    /**
     * 支付请求前签名
     * @param params 参数(已经升序, 排出非空值和sign)
     * @return MD5的签名字符串(大写)
     */
    private String doSignParam(final Map<String, String> params) {
        String signing = buildSigning(params) + "&" + md5(pay.getMd5Key());
        String signed = md5(signing);
        switch (params.get(UnionpayField.SIGN_METHOD)) {
            case "MD5":
                return signed;
            case "SHA1withRSA":
                try {
                    byte[] bytes = RSACryptor.signSha1(signed.getBytes(pay.getCharset()), pay.getMerPrivateKey());
                    return Bytes.base64Encode(bytes);
                } catch (Exception e) {
                    throw new UnionpayException(e);
                }
            default:
                throw new UnsupportedOperationException("unknown sign method " + params.get(UnionpayField.SIGN_METHOD));
        }
    }

    /**
     * 验证签名
     * @param notifyParams
     * @return
     */
    public boolean verifySign(Map<String, String> map) {
        String signMethod = map.get(UnionpayField.SIGN_METHOD);
        String actualSign = map.get(UnionpayField.SIGN);
        String signing = buildSigning(map) + "&" + md5(pay.getMd5Key());
        switch (signMethod) {
            case "MD5":
                return Objects.equals(actualSign, md5(signing));
            case "SHA1withRSA":
                try {
                    byte[] bytes = HashUtils.md5(signing.getBytes(pay.getCharset()));
                    RSACryptor.verifySha1(bytes, pay.getUpopPublicKey(), actualSign.getBytes(pay.getCharset()));
                } catch (Exception e) {
                    throw new UnionpayException(e);
                }
            default:
                throw new UnsupportedOperationException("unsupported " + signMethod + " sign.");
        }
    }

    private String buildSigning(Map<String, String> params) {
        // 过滤参数
        TreeMap<String, String> signingMap = new TreeMap<>();
        for (Map.Entry<String, String> p : params.entrySet()) {
            if (!SIGN_EXCLUDES.contains(p.getKey()) ||
                (this.signIgnoreEmpty && StringUtils.isEmpty(p.getValue()))) {
                signingMap.put(p.getKey(), p.getValue());
            }
        }
        if (signingMap.isEmpty()) {
            throw new IllegalArgumentException("sign origin data can't be empty.");
        }

        // 拼接待签名串
        StringBuilder signing = new StringBuilder();
        for (Map.Entry<String, String> entry : signingMap.entrySet()) {
            signing.append(entry.getKey()).append('=').append(StringUtils.defaultString(entry.getValue())).append("&");
        }
        signing.deleteCharAt(signing.length() - 1);
        return signing.toString();
    }

    private String md5(String plain) {
        Charset charset = Charset.forName(pay.getCharset());
        return HashUtils.md5Hex(plain.toString().getBytes(charset));
    }

    /**
     * 交易查询
     * @param request
     * @return
     */
    public OrderQueryResponse queryTrade(OrderQueryRequest request) {
        request.setTransType("01");
        Map<String, String> params = buildReqParams(request);
        OrderQueryResponse resp = doPost(QUERY_URL, params, OrderQueryResponse.class);
        if ("0".equals(resp.getQueryResult()) || "2".equals(resp.getQueryResult())) {
            return resp;
        } else {
            throw new UnionpayException("query trade fail[" + resp.getRespCode() + "-" + resp.getRespMsg() + "]");
        }
    }

    /**
     * <pre>
     *  <span>http请求参数</span>
     *  {
     *    acqCode=null,
     *    merCode=null,
     *    origQid=201608131617280934228,
     *    commodityUrl=null,
     *    version=1.0.0,
     *    transTimeout=300000,
     *    signMethod=MD5,
     *    backEndUrl=http: //f945d5c9.ngrok.io/pay-center-demo/refund/notify?channelType=UNIONPAY_WEB,
     *    commodityQuantity=1,
     *    merAbbr=null,
     *    orderAmount=1,
     *    transferFee=0,
     *    frontEndUrl=null,
     *    commodityDiscount=0,
     *    signature=bbe8ef5b1c96fc87bf9c299a5ccbb4dd,
     *    defaultBankNumber=null,
     *    customerName=null,
     *    defaultPayType=null,
     *    merId=898111141120101,
     *    charset=UTF-8,
     *    orderTime=null,
     *    commodityName=null,
     *    orderCurrency=156,
     *    customerIp=null,
     *    commodityUnitPrice=0,
     *    transType=04,
     *    merReserved={
     *      refundId=2016081320350888463556
     *    },
     *    orderNumber=null
     *  }
     * </pre>
     * 
     * <pre>
     *  <span>http返回数据</span>
     *  {
     *    respCode=00,
     *    cupReserved=,
     *    exchangeRate=,
     *    qid=201608131628280099358,
     *    charset=UTF-8,
     *    merId=898111141120101,
     *    settleDate=,
     *    orderCurrency=156,
     *    version=1.0.0,
     *    settleCurrency=,
     *    transType=04,
     *    signMethod=MD5,
     *    traceNumber=,
     *    settleAmount=,
     *    merAbbr=,
     *    orderAmount=1,
     *    orderNumber=201608131628289192,
     *    traceTime=,
     *    respMsg=操作成功,
     *    respTime=20160813162828,
     *    signature=24be3a8358fdcbb49eac71e3e65cff81,
     *    exchangeDate=
     *  }
     * </pre>
     * 
     * 退款
     * @param request
     * @return
     */
    public RefundResponse refund(RefundRequest request) {
        request.setTransType("04");
        Map<String, String> params = buildReqParams(request);
        RefundResponse resp = doPost(REFUND_URL, params, RefundResponse.class);
        if ("00".equals(resp.getRespCode())) {
            return resp;
        } else {
            throw new UnionpayException("unionpay refund fail: " + Jsons.NORMAL.stringify(resp));
        }
    }

    public static void main(String[] args) {
        String s = Jsons.NORMAL.stringify(new WebPayRequest());
        System.out.println(s);
        Map<?, ?> map = Jsons.NORMAL.parse(s, Map.class);
        System.out.println(map.containsKey("charset"));
    }

}
