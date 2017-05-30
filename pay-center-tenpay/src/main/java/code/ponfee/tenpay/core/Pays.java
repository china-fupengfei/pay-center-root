package code.ponfee.tenpay.core;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import code.ponfee.commons.http.Http;
import code.ponfee.commons.http.HttpParams;
import code.ponfee.commons.json.Jsons;
import code.ponfee.commons.reflect.GenericUtils;
import code.ponfee.commons.xml.XmlMaps;
import code.ponfee.tenpay.model.TenpayField;
import code.ponfee.tenpay.model.pay.AppPayRequest;
import code.ponfee.tenpay.model.pay.AppPayResponse;
import code.ponfee.tenpay.model.pay.WebPayRequest;

public class Pays extends Component {

    private static final String WEB_PAY_URL = "https://gw.tenpay.com/gateway/pay.htm";
    private static final String APP_PAY_URL = "http://myun.tenpay.com/cgi-bin/wappayv2.0/wappay_init.cgi";

    Pays(Tenpay tenpay) {
        super(tenpay);
    }

    public String webPay(WebPayRequest request) {
        request.setTimeStart(new Date());
        Map<String, String> params = super.buildReqParams(request);
        return HttpParams.buildForm(WEB_PAY_URL, params);
    }

    /**
     * app支付
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public AppPayResponse appPay(AppPayRequest request) {
        request.setBargainorId(tenpay.getPartner());
        request.setVer("2.0");
        request.setCharset("1"); // 1-UTF-8; 2-GB2312 默认为1

        // 构建请求参数
        String json = Jsons.NON_EMPTY.stringify(request);
        Map<String, String> reqMap = GenericUtils.covariant(Jsons.NORMAL.parse(json, Map.class));

        super.buildSignParam(reqMap);
        reqMap.remove(TenpayField.SIGN_TYPE);
        String requestBody = HttpParams.buildParams(reqMap, tenpay.getInputCharset());
        Http http = Http.post(APP_PAY_URL).data(requestBody);
        Map<String, String> respMap = new XmlMaps(http.request()).toMap();

        // 判断是否成功
        if (StringUtils.isNotEmpty(respMap.get(TenpayField.TOKEN_ID))) {
            respMap.put(TenpayField.APP_ID, tenpay.getAppId());
            respMap.put(TenpayField.BARGAINOR_ID, tenpay.getPartner());
            super.buildSignParam(respMap, tenpay.getAppSecret());
            respMap.remove(TenpayField.SIGN_TYPE);
        }

        return Jsons.NORMAL.parse(Jsons.NON_EMPTY.stringify(respMap), AppPayResponse.class);
    }

}
