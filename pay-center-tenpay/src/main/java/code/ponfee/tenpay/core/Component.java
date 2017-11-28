package code.ponfee.tenpay.core;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Objects;

import code.ponfee.commons.http.Http;
import code.ponfee.commons.http.HttpParams;
import code.ponfee.commons.jce.hash.HashUtils;
import code.ponfee.commons.json.Jsons;
import code.ponfee.commons.reflect.GenericUtils;
import code.ponfee.commons.xml.XmlMap;
import code.ponfee.commons.xml.XmlReader;
import code.ponfee.tenpay.exception.TenPayException;
import code.ponfee.tenpay.model.TenpayField;
import code.ponfee.tenpay.model.TenpayRequest;
import code.ponfee.tenpay.model.TenpayResponse;
import code.ponfee.tenpay.model.enums.SignType;

/**
 * 公用组件
 */
public abstract class Component {
    private static final String[] SIGN_EXCLUDES = { TenpayField.SIGN };

    private static final int SUCCESS_CODE = 0;

    protected Tenpay tenpay;

    protected Component(Tenpay tenpay) {
        this.tenpay = tenpay;
    }

    /**
     * http post请求
     * @param url
     * @param params
     * @return
     */
    protected Map<String, String> doPost(String url, Map<String, String> params) {
        //String requestBody = HttpParams.buildParams(params, tenpay.getInputCharset());
        String respXml = Http.post(url).addParam(params).request();
        return process(respXml);
    }

    /**
     * http post请求
     * @param url
     * @param request
     * @param respClazz
     * @return
     */
    protected <T extends TenpayResponse> T doPost(String url, TenpayRequest request, Class<T> respClazz) {
        Map<String, String> respMap = doPost(url, buildReqParams(request));
        return Jsons.NORMAL.parse(Jsons.NORMAL.stringify(respMap), respClazz);
    }

    /**
     * https post
     * @param url
     * @param params
     * @return
     */
    protected Map<String, String> doHttpsPost(String url, Map<String, String> params) {
        //String requestBody = HttpParams.buildParams(params, tenpay.getInputCharset());
        //Https post = Https.post(url).bodyCharset(tenpay.getInputCharset()).body(requestBody);
        //String respXml = post.ssLSocketFactory(tenpay.getSocketFactory()).request();
        String respXml = Http.post(url).addParam(params).setSSLSocketFactory(tenpay.getSocketFactory()).request();
        return process(respXml);
    }

    /**
     * https post请求
     * @param url
     * @param request
     * @param respClazz
     * @return
     */
    protected <T extends TenpayResponse> T doHttpsPost(String url, TenpayRequest request, Class<T> respClazz) {
        Map<String, String> respMap = doHttpsPost(url, buildReqParams(request));
        return Jsons.NORMAL.parse(Jsons.NORMAL.stringify(respMap), respClazz);
    }

    /**
     * 构建签名
     * @param payParams
     * @return
     */
    protected void buildSignParam(Map<String, String> payParams) {
        this.buildSignParam(payParams, tenpay.getMd5Key());
    }

    protected void buildSignParam(Map<String, String> payParams, String key) {
        String signing = HttpParams.buildSigning(payParams, SIGN_EXCLUDES);
        switch (tenpay.getSignType()) {
            case MD5:
                payParams.put(TenpayField.SIGN_TYPE, SignType.MD5.name());
                payParams.put(TenpayField.SIGN, md5(signing, key));
                break;
            default:
                throw new UnsupportedOperationException("unsupported " + tenpay.getSignType().name() + " sign.");
        }
    }

    /**
     * 验证签名
     * @param notifyParams
     * @return
     */
    protected boolean doVerifySign(Map<String, String> notifyParams) {
        return doVerifySign(notifyParams, tenpay.getMd5Key());
    }

    protected boolean doVerifySign(Map<String, String> notifyParams, String key) {
        SignType signType;
        if (notifyParams.get(TenpayField.SIGN_TYPE) == null) {
            // app支付
            signType = SignType.MD5;
        } else {
            signType = SignType.from(notifyParams.get(TenpayField.SIGN_TYPE));
        }

        String actualSign = notifyParams.get(TenpayField.SIGN);
        String signing;
        switch (signType) {
            case MD5:
                signing = HttpParams.buildSigning(notifyParams, SIGN_EXCLUDES);
                return Objects.equals(actualSign, md5(signing, key));
            default:
                throw new UnsupportedOperationException("unsupported " + tenpay.getSignType().name() + " sign.");
        }
    }

    /**
     * 构建请求参数
     * @param request
     */
    @SuppressWarnings("unchecked")
    protected Map<String, String> buildReqParams(TenpayRequest request) {
        request.setInputCharset(tenpay.getInputCharset());
        request.setSignType(tenpay.getSignType());
        request.setSignKeyIndex(tenpay.getSignKeyIndex());
        request.setPartner(tenpay.getPartner());

        String json = Jsons.NORMAL.stringify(request);
        Map<String, String> params = GenericUtils.covariant(Jsons.NORMAL.parse(json, Map.class));

        buildSignParam(params);
        return params;
    }

    /**
     * 解析返回数据
     * @param tenpay接口响应数据
     * @return
     */
    private Map<String, String> process(final String resp) {
        XmlReader readers = XmlReader.create(resp);

        // 判断返回结果
        if (SUCCESS_CODE != readers.getNodeInt(TenpayField.RET_CODE)) {
            throw new TenPayException(readers.getNodeInt(TenpayField.RET_CODE), readers.getNodeText(TenpayField.RET_MSG));
        }

        // 验签
        Map<String, String> respMap = new XmlMap(readers).toMap();
        if (!doVerifySign(respMap)) {
            int code = Integer.parseInt(respMap.get(TenpayField.RET_CODE).toString());
            throw new TenPayException(code, respMap.get(TenpayField.RET_MSG));
        }
        return respMap;
    }

    /**
     * MD5哈希
     * @param signing
     * @return
     */
    private String md5(String signing, String key) {
        try {
            byte[] bytes = (signing + "&key=" + key).getBytes(tenpay.getInputCharset());
            return HashUtils.md5Hex(bytes).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            throw new TenPayException(e);
        }
    }

}
