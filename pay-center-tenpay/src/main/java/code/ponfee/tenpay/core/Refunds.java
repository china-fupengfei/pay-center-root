package code.ponfee.tenpay.core;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import code.ponfee.tenpay.model.TenpayField;
import code.ponfee.tenpay.model.refund.RefundApplyRequest;
import code.ponfee.tenpay.model.refund.RefundApplyResponse;
import code.ponfee.tenpay.model.refund.RefundQueryRequest;
import code.ponfee.tenpay.model.refund.RefundQueryResponse;
import code.ponfee.tenpay.model.refund.RefundQueryResponse.RefundItem;

/**
 * 退款
 * @author fupf
 */
public class Refunds extends Component {

    public static final List<Integer> REFUND_SUCCESS = Arrays.asList(4, 10);

    private static final String APPLY_URL = "https://api.mch.tenpay.com/refundapi/gateway/refund.xml";

    private static final String QUERY_URL = "https://gw.tenpay.com/gateway/normalrefundquery.xml";

    
    protected Refunds(Tenpay tenpay) {
        super(tenpay);
    }
    
    /**
     * 申请退款
     * @param request
     * @return
     */
    public RefundApplyResponse apply(RefundApplyRequest request) {
        request.setPartner(tenpay.getPartner());
        request.setOpUserId(tenpay.getPartner());
        request.setOpUserPasswd(tenpay.getOpUserPwd());
        request.setServiceVersion("1.1");
        return doHttpsPost(APPLY_URL, request, RefundApplyResponse.class);
    }

    /**
     * 查询退款明细
     * @param request
     * @return
     */
    public RefundQueryResponse query(RefundQueryRequest request) {
        Map<String, String> reqMap = buildReqParams(request);
        Map<String, String> respMap = super.doPost(QUERY_URL, reqMap);
        int count = Integer.parseInt(respMap.get(TenpayField.REFUND_COUNT).toString());

        RefundQueryResponse response = new RefundQueryResponse();
        response.setOutTradeNo(respMap.get(TenpayField.OUT_TRADE_NO));
        response.setTransactionId(respMap.get(TenpayField.TRANSACTION_ID));
        response.setRefundCount(count);

        // 退款明细数据
        FastDateFormat format = FastDateFormat.getInstance("yyyyMMddhhmmss");
        List<RefundItem> details = new ArrayList<>();
        String item;
        for (int i = 0; i < count; i++) {
            item = "_" + i;
            String outRefundNo = respMap.get(TenpayField.OUT_REFUND_NO + item);
            String refundId = respMap.get(TenpayField.REFUND_ID + item);
            String refundChannel = respMap.get(TenpayField.REFUND_CHANNEL + item);
            String refundFee = respMap.get(TenpayField.REFUND_FEE + item);
            String state = respMap.get(TenpayField.REFUND_STATE + item);
            String recvUserId = respMap.get(TenpayField.RECV_USER_ID + item);
            String reccvUserName = respMap.get(TenpayField.RECCV_USER_NAME + item);
            String refundTimeBegin = respMap.get(TenpayField.REFUND_TIME_BEGIN + item);
            String refundTime = respMap.get(TenpayField.REFUND_TIME + item);

            RefundItem detail = new RefundItem();
            detail.setOutRefundNo(outRefundNo);
            detail.setRefundId(refundId);
            detail.setRefundChannel(refundChannel);
            detail.setRefundFee(Integer.parseInt(refundFee));
            detail.setState(state);
            detail.setRecvUserId(recvUserId);
            detail.setReccvUserName(reccvUserName);
            if (StringUtils.isNotBlank(refundTimeBegin)) try {
                detail.setRefundTimeBegin(format.parse(refundTimeBegin));
            } catch(ParseException e) {
                e.printStackTrace();
            }
            if (StringUtils.isNotBlank(refundTime)) try {
                detail.setRefundTime(format.parse(refundTime));
            } catch(ParseException e) {
                e.printStackTrace();
            }
            details.add(detail);
        }
        response.setDetails(details);
        return response;
    }
}
