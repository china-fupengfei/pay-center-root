package code.ponfee.tenpay.model.notify;

import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.tenpay.model.TenpayField;
import code.ponfee.tenpay.model.TenpayRequest;

/**
 * 通知验证请求数据
 * @author fupf
 */
public class NotifyVerifyRequest extends TenpayRequest {

    private static final long serialVersionUID = -2163006419171972591L;

    @JsonProperty(TenpayField.NOTIFY_ID)
    private final String notifyId;

    public NotifyVerifyRequest(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getNotifyId() {
        return notifyId;
    }

    @Override
    public void setOutTradeNo(String outTradeNo) {
        throw new UnsupportedOperationException();
    }
}