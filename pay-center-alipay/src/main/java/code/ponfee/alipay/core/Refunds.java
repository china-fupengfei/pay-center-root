package code.ponfee.alipay.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

import code.ponfee.alipay.exception.AlipayException;
import code.ponfee.alipay.model.enums.AlipayField;
import code.ponfee.alipay.model.enums.PayType;
import code.ponfee.alipay.model.refund.RefundDetailData;
import code.ponfee.alipay.model.refund.RefundQueryDetail;
import code.ponfee.alipay.model.refund.RefundRequest;
import code.ponfee.commons.http.Http;
import code.ponfee.commons.http.HttpParams;
import code.ponfee.commons.util.Dates;
import code.ponfee.commons.util.ObjectUtils;
import code.ponfee.commons.xml.XmlException;
import code.ponfee.commons.xml.XmlReader;

/**
 * 退款组件
 */
public class Refunds extends Component {
    private final Map<String, String> refundConfig;

    Refunds(Alipay alipay) {
        super(alipay);
        refundConfig = new HashMap<>();
        refundConfig.put(AlipayField.PARTNER.field(), alipay.partner);
        refundConfig.put(AlipayField.INPUT_CHARSET.field(), alipay.inputCharset);
        refundConfig.put(AlipayField.SELLER_USER_ID.field(), alipay.partner);
    }

    /**
     * <pre>
     *   <alipay>
     *     <is_success>T</is_success>
     *   </alipay>
     * </pre>
     * 
     * 发起退款请求
     * @param request 退款明细
     * @return 退款是否提交成功(不表示实际退款是否, 需从支付宝退款通知中来确认)
     */
    public boolean apply(RefundRequest request) {
        String resp = null;
        try {
            String url = Alipay.GATEWAY + "_input_charset=" + alipay.inputCharset;
            String params = HttpParams.buildParams(buildRefundParams(request), alipay.inputCharset);
            resp = Http.post(url).data(params).request();
            XmlReader reader = XmlReader.create(resp);
            return "T".equals(reader.getNodeText("is_success"));
        } catch (XmlException e) {
            throw new AlipayException("invalid ali refund response xml:" + resp, e);
        } catch (Exception e) {
            throw new AlipayException(e);
        }
    }

    public List<RefundQueryDetail> query(String thirdTradeNo, String batchNo) {
        Map<String, String> params = new HashMap<>();
        params.put(AlipayField.SERVICE.field(), "refund_fastpay_query");
        params.put(AlipayField.PARTNER.field(), alipay.partner);
        params.put(AlipayField.INPUT_CHARSET.field(), alipay.inputCharset);
        params.put(AlipayField.TRADE_NO.field(), thirdTradeNo);
        params.put(AlipayField.BATCH_NO.field(), batchNo);

        super.buildSignParams(params); // 构建签名参数
        Http http = Http.post(Alipay.GATEWAY + AlipayField.INPUT_CHARSET.field() + "=" + alipay.inputCharset);
        String respStr = http.addParam(params).request();
        Map<String, String> map = process(respStr);
        if ("T".equals(map.get("is_success"))) {
            List<RefundQueryDetail> list = new ArrayList<>();
            String array[] = map.get("result_details").split("\\#");
            for (String str : array) {
                RefundQueryDetail detail = new RefundQueryDetail();
                String[] item = str.split("\\^");
                detail.setOutRefundNo(item[0]);
                detail.setTradeNo(item[1]);
                detail.setRefundFee(item[2]);
                detail.setStatus(item[3]);
                list.add(detail);
            }
            return list;
        } else {
            throw new AlipayException("查询退款结果失败：" + map.get("error_code"));
        }
    }

    private Map<String, String> buildRefundParams(RefundRequest refundDetail) {

        Map<String, String> refundParams = new HashMap<>();

        // 配置参数
        refundParams.putAll(refundConfig);

        // 接口名
        refundParams.put(AlipayField.SERVICE.field(), PayType.REFUND_NO_PWD.value());

        if (!StringUtils.isEmpty(alipay.sellerEmail)) {
            refundParams.put(AlipayField.SELLER_EMAIL.field(), alipay.sellerEmail);
        }

        // 通知URL
        if (!StringUtils.isEmpty(refundDetail.getNotifyUrl())) {
            refundParams.put(AlipayField.NOTIFY_URL.field(), refundDetail.getNotifyUrl());
        }

        // 卖家ID
        refundParams.put(AlipayField.SELLER_USER_ID.field(), alipay.partner);

        // 退款日期
        refundParams.put(AlipayField.REFUND_DATE.field(), Dates.now("yyyy-MM-dd HH:mm:ss"));

        // 退款批次号
        Preconditions.checkArgument(StringUtils.isNotBlank(refundDetail.getBatchNo()));
        refundParams.put(AlipayField.BATCH_NO.field(), refundDetail.getBatchNo());

        // 退款明细
        List<RefundDetailData> detailDatas = refundDetail.getDetailDatas();
        Preconditions.checkArgument(!ObjectUtils.isEmpty(detailDatas));
        refundParams.put(AlipayField.BATCH_NUM.field(), Integer.toString(detailDatas.size()));
        refundParams.put(AlipayField.DETAIL_DATA.field(), refundDetail.formatDetailDatas());

        // md5签名参数
        buildSignParams(refundParams);

        return refundParams;
    }

    private Map<String, String> process(String str) {
        Map<String, String> map = new HashMap<>();
        for (String s : str.split("\\&")) {
            String[] array = s.split("\\=");
            map.put(array[0], array[1]);
        }
        return map;
    }
}
