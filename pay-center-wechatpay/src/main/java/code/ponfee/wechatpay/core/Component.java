package code.ponfee.wechatpay.core;

import static code.ponfee.wechatpay.model.common.WechatpayField.APP_ID;
import static code.ponfee.wechatpay.model.common.WechatpayField.ERR_CODE;
import static code.ponfee.wechatpay.model.common.WechatpayField.ERR_CODE_DES;
import static code.ponfee.wechatpay.model.common.WechatpayField.MCH_ID;
import static code.ponfee.wechatpay.model.common.WechatpayField.RESULT_CODE;
import static code.ponfee.wechatpay.model.common.WechatpayField.RETURN_CODE;
import static code.ponfee.wechatpay.model.common.WechatpayField.RETURN_MSG;
import static code.ponfee.wechatpay.model.common.WechatpayField.SIGN;
import static code.ponfee.wechatpay.model.common.WechatpayField.SUCCESS;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import code.ponfee.commons.http.Http;
import code.ponfee.commons.http.HttpParams;
import code.ponfee.commons.jce.digest.DigestUtils;
import code.ponfee.commons.json.Jsons;
import code.ponfee.commons.xml.XmlMap;
import code.ponfee.commons.xml.XmlReader;
import code.ponfee.wechatpay.exception.WechatpayException;

/**
 * 公用组件
 */
public abstract class Component {

    protected Wechatpay wechatpay;

    protected Component(Wechatpay wepay) {
        this.wechatpay = wepay;
    }

    protected Map<String, String> doPost(final String url, final Map<String, String> params) {
        String requestBody = new XmlMap(params).toXml();
        String resp = Http.post(url).data(requestBody).request();
        Map<String, String> respMap = process(resp);
        return respMap;
    }

    protected <T> T doHttpsPost(final String url, final Map<String, String> params, Class<T> respClazz) {
        String requestBody = new XmlMap(params).toXml();
        String resp = Http.post(url).data(requestBody).setSSLSocketFactory(wechatpay.getSocketFactory()).request();
        Map<String, String> respMap = process(resp);
        return Jsons.NORMAL.parse(Jsons.NORMAL.stringify(respMap), respClazz);
    }

    /**
     * 读取微信xml响应
     * @param xml xml字符串
     * @return 若成功，返回对应Reader，反之抛WepayException
     */
    private Map<String, String> process(final String xml) {
        XmlReader readers = XmlReader.create(xml);
        if (!SUCCESS.equals(readers.getNodeText(RETURN_CODE))) {
            throw new WechatpayException(readers.getNodeText(RETURN_CODE), readers.getNodeText(RETURN_MSG));
        }

        if (!SUCCESS.equals(readers.getNodeText(RESULT_CODE))) {
            throw new WechatpayException(readers.getNodeText(ERR_CODE), readers.getNodeText(ERR_CODE_DES));
        }

        Map<String, String> resp = new XmlMap(readers).toMap();
        if (!doVerifySign(resp)) {
            throw new WechatpayException("invalid signature: ", xml);
        }

        return resp;
    }

    /**
     * 构建配置参数
     * @param params 参数
     */
    protected void buildConfigParams(final Map<String, String> params) {
        params.put(APP_ID, wechatpay.getAppId());
        params.put(MCH_ID, wechatpay.getMchId());
    }

    /**
     * 构建签名参数
     * @param params 支付参数
     */
    protected void buildSignParams(final Map<String, String> params) {
        String sign = doSign(params);
        params.put(SIGN, sign);
    }

    /**
     * 支付请求前签名
     * @param params 参数(已经升序, 排出非空值和sign)
     * @return MD5的签名字符串(大写)
     */
    protected String doSign(final Map<String, String> params) {
        String signing = HttpParams.buildSigning(params, new String[] { SIGN });
        signing += "&key=" + wechatpay.getAppKey();
        // md5 sign
        try {
            return DigestUtils.md5Hex(signing.toString().getBytes("UTF-8")).toUpperCase();
        } catch(UnsupportedEncodingException e) {
            throw new WechatpayException(e);
        }
    }

    /**
     * 校验参数
     * @param data 待校验参数
     * @return 校验成功返回true，反之false
     */
    protected boolean doVerifySign(final Map<String, String> data) {
        String actualSign = data.get(SIGN);
        String expectSign = doSign(data);
        return expectSign.equals(actualSign);
    }

    protected void putIfNotEmpty(final Map<String, String> map, String field, String paramValue) {
        if (!StringUtils.isEmpty(paramValue)) {
            map.put(field, paramValue);
        }
    }

}
