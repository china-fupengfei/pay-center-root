package code.ponfee.qpay.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import code.ponfee.commons.json.Jsons;
import code.ponfee.qpay.model.QpayFields;
import code.ponfee.qpay.model.enums.RefundChannel;
import code.ponfee.qpay.model.enums.RefundStatus;
import code.ponfee.qpay.model.refund.RefundApplyRequest;
import code.ponfee.qpay.model.refund.RefundApplyResponse;
import code.ponfee.qpay.model.refund.RefundQueryRequest;
import code.ponfee.qpay.model.refund.RefundQueryResponse;
import code.ponfee.qpay.model.refund.RefundQueryResponse.RefundItem;

/**
 * 退款组件
 * @author fupf
 */
public final class Refunds extends Component {

    /** 申请退款 */
    private static final String APPLY = "https://api.qpay.qq.com/cgi-bin/pay/qpay_refund.cgi";

    /** 查询退款 */
    private static final String QUERY = "https://qpay.qq.com/cgi-bin/pay/qpay_refund_query.cgi";

    Refunds(Qpay qpay) {
        super(qpay);
    }

    /**
     * 申请退款
     * @param req
     * @return RefundApplyResponse 退款申请响应，或抛QpayException
     */
    public RefundApplyResponse apply(RefundApplyRequest req) {
        req.setOpUserId(qpay.getOpUserId());
        req.setOpUserPasswd(qpay.getOpUserPwd());
        return super.doHttpsPost(APPLY, req, RefundApplyResponse.class);
    }

    /**
     * @param req
     * @return RefundQueryResponse 退款查询响应，或抛QpayException
     */
    public RefundQueryResponse query(RefundQueryRequest req) {
        Map<String, String> map = super.doHttpPost(QUERY, req);
        RefundQueryResponse resp = Jsons.NORMAL.parse(Jsons.NORMAL.stringify(map), RefundQueryResponse.class);

        List<RefundItem> refundItems = new ArrayList<>();
        for (int i = 0;; i++) {
            if (map.get(QpayFields.REFUND_ID_$ + i) == null) break;
            RefundItem item = new RefundItem();
            item.setRefundId(map.get(QpayFields.REFUND_ID_$ + i));
            item.setOutRefundNo(map.get(QpayFields.OUT_REFUND_NO_$ + i));
            item.setRefundChannel(RefundChannel.from(map.get(QpayFields.REFUND_CHANNEL_$ + i)));
            item.setRefundFee(NumberUtils.toInt(map.get(QpayFields.REFUND_FEE_$ + i)));
            item.setCouponRefundFee(NumberUtils.toInt(map.get(QpayFields.COUPON_REFUND_FEE_$ + i)));
            item.setCashRefundFee(NumberUtils.toInt(map.get(QpayFields.CASH_REFUND_FEE_$ + i)));
            item.setRefundStatus(RefundStatus.from(map.get(QpayFields.REFUND_STATUS_$ + i)));
            refundItems.add(item);
        }
        resp.setRefundItems(refundItems);

        return resp;
    }

}
