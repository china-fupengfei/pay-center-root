package code.ponfee.alipay.model.pay;

import java.io.Serializable;
import java.util.List;

/**
 * 批量付款到支付宝账户
 */
public class BatchPayRequest implements Serializable {

    private static final long serialVersionUID = 4693336414464335776L;

    private String accountName;

    private List<BatchDetailData> detailDatas;

    private String batchNo;

    private String batchNum;

    private String batchFee;

    private String email;

    private String payDate;

    private String notifyUrl;

    private String buyerAccountName;

    private String extendParam;
    
    private String expireTime; // it_b_pay

    public BatchPayRequest() {}

    public BatchPayRequest(String accountName, List<BatchDetailData> detailDatas, String batchNo, String batchNum, String batchFee, String email,
        String payDate) {
        super();
        this.accountName = accountName;
        this.detailDatas = detailDatas;
        this.batchNo = batchNo;
        this.batchNum = batchNum;
        this.batchFee = batchFee;
        this.email = email;
        this.payDate = payDate;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(String batchNum) {
        this.batchNum = batchNum;
    }

    public String getBatchFee() {
        return batchFee;
    }

    public void setBatchFee(String batchFee) {
        this.batchFee = batchFee;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getBuyerAccountName() {
        return buyerAccountName;
    }

    public void setBuyerAccountName(String buyerAccountName) {
        this.buyerAccountName = buyerAccountName;
    }

    public String getExtendParam() {
        return extendParam;
    }

    public void setExtendParam(String extendParam) {
        this.extendParam = extendParam;
    }

    /**
     * 格式化退款数据 {@link code.ponfee.alipay.model.enums.AlipayField#DETAIL_DATA}
     * @return 退款数据
     */
    public String formatDetailDatas() {
        if (detailDatas == null || detailDatas.isEmpty()) return null;

        StringBuilder details = new StringBuilder();
        for (BatchDetailData data : detailDatas) {
            details.append(data.format()).append("|");
        }
        details.deleteCharAt(details.length() - 1);
        return details.toString();
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return "TransPayDetail {accountName=" + accountName + ", detailDatas=" + formatDetailDatas() + ", batchNo=" + batchNo + ", batchNum=" + batchNum
            + ", batchFee=" + batchFee + ", email=" + email + ", payDate=" + payDate + ", notifyUrl=" + notifyUrl + ", buyerAccountName=" + buyerAccountName
            + ", extendParam=" + extendParam + "}";
    }

    /**
     * 付款详细数据
     */
    public static class BatchDetailData implements Serializable {

        private static final long serialVersionUID = -6083031724670548954L;

        private String detailNo; // 流水号

        private String payeeAccount; // 付款方账号

        private String payeeName; // 付款方姓名

        private String payAmt; // 付款金额

        private String remark; // 备注

        public BatchDetailData(String detailNo, String payeeAccount, String payeeName, String payAmt, String remark) {
            this.detailNo = detailNo;
            this.payeeAccount = payeeAccount;
            this.payeeName = payeeName;
            this.payAmt = payAmt;
            this.remark = remark;
        }

        /**
         * 格式化为支付宝需要的格式
         * @return
         */
        public String format() {
            return detailNo + "^" + payeeAccount + "^" + payeeName + "^" + payAmt + "^" + remark;
        }
    }
}