package code.ponfee.alipay.core;

import java.util.Base64;
import java.util.Map;
import java.util.Objects;

import code.ponfee.alipay.exception.AlipayException;
import code.ponfee.alipay.model.enums.AlipayField;
import code.ponfee.alipay.model.enums.PayType;
import code.ponfee.alipay.model.enums.SignType;
import code.ponfee.commons.http.Http;
import code.ponfee.commons.http.HttpParams;
import code.ponfee.commons.jce.security.RSACryptor;

/**
 * 验证组件
 */
public class Verifies extends Component {

    Verifies(Alipay alipay) {
        super(alipay);
    }

    /**
     * 验证支付通知ID是否合法
     * @param notifyId 通知ID，后置通知中会有
     * @return 合法返回true，反之false
     */
    public boolean verifyNotify(String notifyId) {
        String url = Alipay.GATEWAY + AlipayField.SERVICE.field() + "=" + PayType.NOTIFY_VERIFY.value() + "&" 
                    + AlipayField.PARTNER.field() + "=" + alipay.partner + "&" + AlipayField.NOTIFY_ID.field() 
                    + "=" + notifyId;
        String resp = Http.get(url).request();
        return "true".equalsIgnoreCase(resp);
    }
    
    /**
     * 验证签名
     * @param notifyParams
     * @return
     */
    public boolean verifySign(Map<String, String> notifyParams) {
        String signing;
        switch (SignType.from(notifyParams.get(AlipayField.SIGN_TYPE.field()))) {
            case MD5:
                signing = HttpParams.buildSigning(notifyParams, SIGN_EXCLUDES);
                return Objects.equals(notifyParams.get(AlipayField.SIGN.field()), md5(signing));
            case RSA:
                byte[] expectSigned = Base64.getDecoder().decode(notifyParams.get(AlipayField.SIGN.field()));
                signing = HttpParams.buildSigning(notifyParams, SIGN_EXCLUDES);
                try {
                    return RSACryptor.verifySha1(signing.getBytes(alipay.inputCharset), alipay.aliPubKey, expectSigned);
                } catch(Exception e) {
                    throw new AlipayException("verify sign occurs error", e);
                }
            case DSA:
                throw new UnsupportedOperationException("unsupported DSA signature algorithm.");
            default:
                throw new IllegalArgumentException("invalid signType: " + alipay.signType);
        }
    }
}
