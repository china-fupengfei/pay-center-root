package code.ponfee.tenpay.core;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import code.ponfee.tenpay.model.notify.NotifyVerifyRequest;
import code.ponfee.tenpay.model.notify.NotifyVerifyResponse;

public class Notifies extends Component {
    private static Logger logger = LoggerFactory.getLogger(Notifies.class);

    private static final String TRADE_SUCCESS = "0"; // 交易成功状态码

    private static final String VERIFY_URL = "https://gw.tenpay.com/gateway/verifynotifyid.xml";

    protected Notifies(Tenpay tenPay) {
        super(tenPay);
    }

    public boolean verifySign(Map<String, String> params) {
        return super.doVerifySign(params);
    }

    /**
     * 验证支付通知ID是否合法
     * @param notifyId 通知ID，后置通知中会有
     */
    public boolean verifyNotify(String notifyId) {
        NotifyVerifyRequest request = new NotifyVerifyRequest(notifyId);
        try {
            NotifyVerifyResponse resp = super.doPost(VERIFY_URL, request, NotifyVerifyResponse.class);
            if (TRADE_SUCCESS.equals(resp.getTradeState())) {
                return true;
            } else {
                return false;
            }
        } catch(Exception e) {
            logger.error("notify id verify error", e);
            return false;
        }
    }
}
