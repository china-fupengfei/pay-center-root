package code.ponfee.alipay.core;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import code.ponfee.commons.http.Http;
import code.ponfee.commons.http.HttpParams;
import code.ponfee.commons.util.Preconditions;
import code.ponfee.commons.xml.XmlReaders;
import code.ponfee.alipay.exception.AlipayException;
import code.ponfee.alipay.model.enums.AlipayField;
import code.ponfee.alipay.model.enums.PayType;
import code.ponfee.alipay.model.pay.AppPayRequest;
import code.ponfee.alipay.model.pay.BatchPayRequest;
import code.ponfee.alipay.model.pay.PayQueryResponse;
import code.ponfee.alipay.model.pay.PayRequest;
import code.ponfee.alipay.model.pay.WapPayRequest;
import code.ponfee.alipay.model.pay.WebPayRequest;

/**
 * 支付组件
 */
public class Pays extends Component {
    private final Map<String, String> payConfig;

    Pays(Alipay alipay) {
        super(alipay);
        payConfig = new HashMap<>();
        payConfig.put(AlipayField.PARTNER.field(), alipay.partner);
        payConfig.put(AlipayField.SELLER_ID.field(), alipay.partner);
        payConfig.put(AlipayField.PAYMENT_TYPE.field(), alipay.paymentType);
        payConfig.put(AlipayField.PAY_METHOD.field(), alipay.payMethod);
        payConfig.put(AlipayField.INPUT_CHARSET.field(), alipay.inputCharset);
    }

    /**
     * web支付
     * @param webPayDetail 支付字段信息
     * @return 自动提交表单(可直接输出到浏览器)
     */
    public String webPay(WebPayRequest webPayDetail) {
        // 公共参数
        Map<String, String> webPayParams = buildPayParams(webPayDetail, PayType.WEB_PAY);

        // web特有参数
        putIfNotEmpty(webPayParams, AlipayField.EXTER_INVOKE_IP, webPayDetail.getExterInvokeIp());
        putIfNotEmpty(webPayParams, AlipayField.PAY_METHOD, webPayDetail.getPayMethod());
        putIfNotEmpty(webPayParams, AlipayField.DEFAULT_BANK, webPayDetail.getDefaultBank());
        putIfNotEmpty(webPayParams, AlipayField.EXTRA_COMMON_PARAM, webPayDetail.getExtraCommonParam());

        // 参数签名
        buildSignParams(webPayParams);

        return HttpParams.buildForm(Alipay.GATEWAY + AlipayField.INPUT_CHARSET.field() + "=" + alipay.inputCharset, webPayParams);
    }

    /**
     * WAP支付
     * @param wapPayDetail 支付字段信息
     * @return 自动提交表单(可直接输出到浏览器)
     */
    public String wapPay(WapPayRequest wapPayDetail) {
        // 公共参数
        Map<String, String> wapPayParams = buildPayParams(wapPayDetail, PayType.WAP_PAY);

        // WAP特有参数
        putIfNotEmpty(wapPayParams, AlipayField.SHOW_URL, wapPayDetail.getShowUrl());
        putIfNotEmpty(wapPayParams, AlipayField.RN_CHECK, wapPayDetail.getRnCheck());
        putIfNotEmpty(wapPayParams, AlipayField.AIR_TICKET, wapPayDetail.getAirTicket());
        putIfNotEmpty(wapPayParams, AlipayField.BUYER_CERT_NO, wapPayDetail.getBuyerCertNo());
        putIfNotEmpty(wapPayParams, AlipayField.BUYER_REAL_NAME, wapPayDetail.getBuyerRealName());
        putIfNotEmpty(wapPayParams, AlipayField.EXTERN_TOKEN, wapPayDetail.getExternToken());
        putIfNotEmpty(wapPayParams, AlipayField.OTHER_FEE, wapPayDetail.getOtherFee());
        putIfNotEmpty(wapPayParams, AlipayField.SCENE, wapPayDetail.getScene());

        // 参数签名
        buildSignParams(wapPayParams);

        return HttpParams.buildForm(Alipay.GATEWAY + AlipayField.INPUT_CHARSET.field() + "=" + alipay.inputCharset, wapPayParams);
    }

    /**
     * 批量付款到支付宝账户(有密接口)
     * @param wapPayDetail
     * @return
     */
    public String batchPay(BatchPayRequest batchPayDetail) {
        Map<String, String> payParams = new HashMap<>();
        payParams.put(AlipayField.SERVICE.field(), PayType.BATCH_TRANS_NOTIFY.value());
        payParams.put(AlipayField.PARTNER.field(), super.alipay.partner);
        payParams.put(AlipayField.INPUT_CHARSET.field(), super.alipay.inputCharset);
        payParams.put(AlipayField.ACCOUNT_NAME.field(), batchPayDetail.getAccountName());
        payParams.put(AlipayField.DETAIL_DATA.field(), batchPayDetail.formatDetailDatas());
        payParams.put(AlipayField.BATCH_NO.field(), batchPayDetail.getBatchNo());
        payParams.put(AlipayField.BATCH_NUM.field(), batchPayDetail.getBatchNum());
        payParams.put(AlipayField.BATCH_FEE.field(), batchPayDetail.getBatchFee());
        payParams.put(AlipayField.EMAIL.field(), batchPayDetail.getEmail());
        payParams.put(AlipayField.PAY_DATE.field(), batchPayDetail.getPayDate());

        putIfNotEmpty(payParams, AlipayField.NOTIFY_URL, batchPayDetail.getNotifyUrl());
        putIfNotEmpty(payParams, AlipayField.BUYER_ACCOUNT_NAME, batchPayDetail.getBuyerAccountName());
        putIfNotEmpty(payParams, AlipayField.EXTEND_PARAM, batchPayDetail.getExtendParam());

        putIfNotEmpty(payParams, AlipayField.IT_B_PAY, batchPayDetail.getExpireTime());
        // 参数签名
        buildSignParams(payParams);

        return HttpParams.buildForm(Alipay.GATEWAY + AlipayField.INPUT_CHARSET.field() + "=" + alipay.inputCharset, payParams);
    }

    /**
     * APP支付
     * @param appPayDetail 支付字段信息
     * @return APP支付字符串
     */
    public String appPay(AppPayRequest appPayDetail) {
        Map<String, String> appPayParams = buildPayParams(appPayDetail, PayType.APP_PAY); // 公共参数
        appPayParams.remove(AlipayField.RETURN_URL.field()); // APP支付无return_url

        // APP特有参数
        putIfNotEmpty(appPayParams, AlipayField.APP_ID, appPayDetail.getAppId());
        putIfNotEmpty(appPayParams, AlipayField.APPENV, appPayDetail.getAppenv());
        putIfNotEmpty(appPayParams, AlipayField.EXTERN_TOKEN, appPayDetail.getExternToken());
        putIfNotEmpty(appPayParams, AlipayField.OUT_CONTEXT, appPayDetail.getOutContext());
        putIfNotEmpty(appPayParams, AlipayField.RN_CHECK, appPayDetail.getRnCheck());
        if (appPayDetail.getGoodsType() != null) {
            appPayParams.put(AlipayField.GOODS_TYPE.field(), appPayDetail.getGoodsType().value());
        }

        return (String) buildSignParams(appPayParams);
    }

    /**
     * 单笔交易查询
     * @param outTradeNo
     * @param tradeNo
     * @return
     */
    public PayQueryResponse query(String outTradeNo, String tradeNo) {
        Map<String, String> params = new HashMap<>();
        params.put(AlipayField.SERVICE.field(), "single_trade_query");
        params.put(AlipayField.PARTNER.field(), alipay.partner);
        params.put(AlipayField.INPUT_CHARSET.field(), alipay.inputCharset);
        params.put(AlipayField.OUT_TRADE_NO.field(), outTradeNo);
        params.put(AlipayField.TRADE_NO.field(), tradeNo);
        super.buildSignParams(params);

        Http http = Http.post(Alipay.GATEWAY + AlipayField.INPUT_CHARSET.field() + "=" + alipay.inputCharset);
        String respStr = http.params(params).request();
        XmlReaders reader = XmlReaders.create(respStr);
        String tradeXPath = "/alipay/response/trade/";

        if ("T".equals(reader.getNodeText("is_success"))) {
            PayQueryResponse resp = new PayQueryResponse();
            resp.setTradeStatus(reader.evaluate(tradeXPath + "trade_status"));
            resp.setOutTradeNo(reader.evaluate(tradeXPath + "out_trade_no"));
            resp.setTotalFee(reader.evaluate(tradeXPath + "total_fee"));
            resp.setTradeNo(reader.evaluate(tradeXPath + "trade_no"));
            resp.setGmtPayment(reader.evaluate(tradeXPath + "gmt_payment"));
            resp.setRefundId(reader.evaluate(tradeXPath + "refund_id"));
            resp.setRefundStatus(reader.evaluate(tradeXPath + "refund_status"));
            resp.setRefundFee(reader.evaluate(tradeXPath + "refund_fee"));
            String refundTime = reader.evaluate(tradeXPath + "gmt_refund");
            if (StringUtils.isNotBlank(refundTime)) {
                resp.setGmtRefund(refundTime.substring(0, 19));
            }
            return resp;
        } else {
            throw new AlipayException(reader.getNodeText("error"));
        }
    }

    /**
     * 构建支付公共参数
     * @param payDetail 字段
     * @param service 服务接口
     * @return PC和WAP公共支付参数
     */
    private Map<String, String> buildPayParams(PayRequest payDetail, PayType service) {
        Map<String, String> payParams = new HashMap<>();

        // 配置参数
        payParams.putAll(payConfig);

        // 业务参数
        payParams.put(AlipayField.SERVICE.field(), service.value());

        Preconditions.checkNotEmpty(payDetail.getOutTradeNo(), "outTradeNo");
        payParams.put(AlipayField.OUT_TRADE_NO.field(), payDetail.getOutTradeNo());

        Preconditions.checkNotEmpty(payDetail.getSubject(), "subject");
        payParams.put(AlipayField.SUBJECT.field(), payDetail.getSubject());

        Preconditions.checkNotEmpty(payDetail.getTotalFee(), "totalFee");
        payParams.put(AlipayField.TOTAL_FEE.field(), payDetail.getTotalFee());

        putIfNotEmpty(payParams, AlipayField.BODY, payDetail.getBody());
        putIfNotEmpty(payParams, AlipayField.NOTIFY_URL, payDetail.getNotifyUrl());
        putIfNotEmpty(payParams, AlipayField.RETURN_URL, payDetail.getReturnUrl());

        putIfNotEmpty(payParams, AlipayField.IT_B_PAY, payDetail.getExpireTime());
        return payParams;
    }
}
