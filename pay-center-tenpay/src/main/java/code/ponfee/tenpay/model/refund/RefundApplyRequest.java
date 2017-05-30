package code.ponfee.tenpay.model.refund;

import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.commons.json.Jsons;
import code.ponfee.tenpay.model.TenpayField;
import code.ponfee.tenpay.model.TenpayRequest;

/**
 * 退款请求数据
 * @author fupf
 */
public class RefundApplyRequest extends TenpayRequest {

    private static final long serialVersionUID = 793535761898127898L;

    @JsonProperty(TenpayField.TRANSACTION_ID)
    private String transactionId;

    @JsonProperty(TenpayField.OUT_REFUND_NO)
    private String outRefundNo;

    @JsonProperty(TenpayField.TOTAL_FEE)
    private int totalFee;

    @JsonProperty(TenpayField.REFUND_FEE)
    private int refundFee;

    @JsonProperty(TenpayField.OP_USER_ID)
    private String opUserId;

    @JsonProperty(TenpayField.OP_USER_PASSWD)
    private String opUserPasswd;

    @JsonProperty(TenpayField.RECV_USER_ID)
    private String recvUserId;

    @JsonProperty(TenpayField.RECCV_USER_NAME)
    private String reccvUserName;

    //private String use_spbill_no_flag;

    @JsonProperty(TenpayField.REFUND_TYPE)
    private String refundType;

    @JsonProperty(TenpayField.NOTIFY_URL)
    private String notifyUrl;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public int getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(int refundFee) {
        this.refundFee = refundFee;
    }

    public String getOpUserId() {
        return opUserId;
    }

    public void setOpUserId(String opUserId) {
        this.opUserId = opUserId;
    }

    public String getOpUserPasswd() {
        return opUserPasswd;
    }

    public void setOpUserPasswd(String opUserPasswd) {
        this.opUserPasswd = opUserPasswd;
    }

    public String getRecvUserId() {
        return recvUserId;
    }

    public void setRecvUserId(String recvUserId) {
        this.recvUserId = recvUserId;
    }

    public String getReccvUserName() {
        return reccvUserName;
    }

    public void setReccvUserName(String reccvUserName) {
        this.reccvUserName = reccvUserName;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public static void main(String[] args) {
        RefundApplyRequest request = new RefundApplyRequest();
        request.setTotalFee(1);
        System.out.println(Jsons.NON_EMPTY.stringify(request));
        
        String s = "{\"sign_key_index\":\"1\",\"total_fee\":100,\"refund_fee\":10,\"refund_type\":1}";
        request = Jsons.NORMAL.parse(s, RefundApplyRequest.class);
        System.out.println(request.getSignKeyIndex());
        System.out.println(request.getRefundType());
    }

}
