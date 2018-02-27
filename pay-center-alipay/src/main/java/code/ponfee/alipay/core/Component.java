package code.ponfee.alipay.core;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import code.ponfee.alipay.exception.AlipayException;
import code.ponfee.alipay.model.enums.AlipayField;
import code.ponfee.alipay.model.enums.SignType;
import code.ponfee.commons.http.HttpParams;
import code.ponfee.commons.jce.digest.DigestUtils;
import code.ponfee.commons.jce.security.RSACryptor;

/**
 * 抽象组件
 */
public abstract class Component {
    static final String[] SIGN_EXCLUDES = { AlipayField.SIGN.field(), AlipayField.SIGN_TYPE.field() };

    protected Alipay alipay;

    protected Component(Alipay alipay) {
        this.alipay = alipay;
    }

    /**
     * 构建签名
     * @param payParams
     * @return
     */
    protected Object buildSignParams(Map<String, String> payParams) {
        String signing;
        switch (alipay.signType) {
            case MD5:
                signing = HttpParams.buildSigning(payParams, SIGN_EXCLUDES);
                payParams.put(AlipayField.SIGN_TYPE.field(), SignType.MD5.name());
                payParams.put(AlipayField.SIGN.field(), md5(signing));
                return payParams;
            case RSA:
                try {
                    signing = HttpParams.buildSigning(payParams, "\"", SIGN_EXCLUDES);
                    byte[] signedBytes = RSACryptor.signSha1(signing.getBytes(alipay.inputCharset), alipay.ptnPriKey);
                    String signed = URLEncoder.encode(Base64.getEncoder().encodeToString(signedBytes), alipay.inputCharset);
                    return signing + "&sign_type=\"" + SignType.RSA.name() + "\"&sign=\"" + signed + "\"";
                } catch(Exception e) {
                    throw new AlipayException(e);
                }
            case DSA:
                throw new UnsupportedOperationException("unsupported DSA sign.");
            default:
                throw new IllegalArgumentException("invalid signType: " + alipay.signType);
        }

    }

    protected String md5(String payString) {
        try {
            return DigestUtils.md5Hex((payString + alipay.md5Key).getBytes(alipay.inputCharset));
        } catch(UnsupportedEncodingException e) {
            throw new AlipayException(e);
        }
    }

    protected void putIfNotEmpty(Map<String, String> map, AlipayField field, String paramValue) {
        putIfNotEmpty(map, field.field(), paramValue);
    }

    protected void putIfNotEmpty(Map<String, String> map, String field, String paramValue) {
        if (!StringUtils.isEmpty(paramValue)) {
            map.put(field, paramValue);
        }
    }

}