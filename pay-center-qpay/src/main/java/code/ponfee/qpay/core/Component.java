package code.ponfee.qpay.core;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.RandomStringUtils;

import code.ponfee.commons.http.Http;
import code.ponfee.commons.http.HttpParams;
import code.ponfee.commons.jce.hash.HashUtils;
import code.ponfee.commons.json.Jsons;
import code.ponfee.commons.reflect.GenericUtils;
import code.ponfee.commons.xml.XmlMap;
import code.ponfee.commons.xml.XmlReader;
import code.ponfee.qpay.exception.QpayException;
import code.ponfee.qpay.model.QpayFields;
import code.ponfee.qpay.model.QpayRequest;
import code.ponfee.qpay.model.QpayResponse;
import code.ponfee.qpay.model.enums.ResultCode;
import code.ponfee.qpay.model.enums.ReturnCode;

/**
 * 公用组件
 * @author fupf
 */
public abstract class Component {
    private static final String[] SIGN_EXCLUDES = { QpayFields.SIGN };

    protected final Qpay qpay;

    protected Component(Qpay qpay) {
        this.qpay = qpay;
    }

    /**
     * 构建请求参数
     * @param request
     */
    @SuppressWarnings("unchecked")
    protected final Map<String, String> buildReqParams(QpayRequest request) {
        request.setAppid(qpay.getAppid());
        request.setMchId(qpay.getMchId());
        request.setNonceStr(RandomStringUtils.randomAlphanumeric(16));

        String json = Jsons.NORMAL.stringify(request);
        Map<String, String> params = GenericUtils.covariant(Jsons.NORMAL.parse(json, Map.class));
        this.doBuildSign(params, qpay.getMd5Key());
        return params;
    }

    /**
     * 构建签名
     * @param req
     */
    protected final void doBuildSign(Map<String, String> req) {
        this.doBuildSign(req, qpay.getMd5Key());
    }

    /**
     * 构建签名
     * @param payParams
     * @param key
     */
    protected final void doBuildSign(Map<String, String> payParams, String key) {
        String signing = HttpParams.buildSigning(payParams, SIGN_EXCLUDES);
        payParams.put(QpayFields.SIGN, this.md5(signing, key));
    }

    /**
     * 验证签名
     * @param resp
     * @return
     */
    protected final boolean doVerifySign(Map<String, String> resp) {
        return this.doVerifySign(resp, qpay.getMd5Key());
    }

    /**
     * 验证签名
     * @param resp
     * @param key
     * @return
     */
    protected final boolean doVerifySign(Map<String, String> resp, String key) {
        String actualSign = resp.get(QpayFields.SIGN);
        String signing = HttpParams.buildSigning(resp, SIGN_EXCLUDES);
        return Objects.equals(actualSign, md5(signing, key));
    }

    /**
     * http post请求
     * @param url
     * @param req
     * @param clazz
     * @return
     */
    protected final <T extends QpayResponse> T doHttpPost(String url, QpayRequest req, Class<T> clazz) {
        String data = new XmlMap(buildReqParams(req)).toXml();
        String resp = Http.post(url).data(data).request();
        return process(resp, clazz);
    }

    /**
     * http post请求
     * @param url
     * @param req
     * @return
     */
    protected final Map<String, String> doHttpPost(String url, QpayRequest req) {
        String data = new XmlMap(buildReqParams(req)).toXml();
        String resp = Http.post(url).data(data).request();
        return process(resp);
    }

    /**
     * http post请求（要求客户端证书）
     * @param url
     * @param req
     * @param clazz
     * @return
     */
    protected final <T extends QpayResponse> T doHttpsPost(String url, QpayRequest req, Class<T> clazz) {
        String data = new XmlMap(buildReqParams(req)).toXml();
        String resp = Http.post(url).data(data).setSSLSocketFactory(qpay.getSocketFactory()).request();
        return process(resp, clazz);
    }

    /**
     * 解析返回数据
     * @param resp
     * @return
     */
    private Map<String, String> process(final String resp) {
        Map<String, String> respMap = new XmlMap(XmlReader.create(resp)).toMap();

        if (!respMap.get(QpayFields.RETURN_CODE).equals(ReturnCode.SUCCESS.name()) ||
            !respMap.get(QpayFields.RESULT_CODE).equals(ResultCode.SUCCESS.name())) {
            throw new QpayException("return or result code fail: " + resp);
        }

        if (!doVerifySign(respMap)) {
            throw new QpayException("invalid signature: " + resp);
        }

        return respMap;
    }

    /**
     * 解析返回数据
     * @param qpay接口响应数据
     * @return
     */
    private <T extends QpayResponse> T process(final String resp, Class<T> respClazz) {
        Map<String, String> respMap = this.process(resp);
        return Jsons.NORMAL.parse(Jsons.NORMAL.stringify(respMap), respClazz);
    }

    /**
     * md5 hash
     * @param signing
     * @param key
     * @return
     */
    private String md5(String signing, String key) {
        byte[] bytes = (signing + "&key=" + key).getBytes();
        return HashUtils.md5Hex(bytes).toUpperCase();
    }

}
