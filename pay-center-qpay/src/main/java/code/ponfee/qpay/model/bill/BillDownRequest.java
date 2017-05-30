package code.ponfee.qpay.model.bill;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import code.ponfee.qpay.model.QpayFields;
import code.ponfee.qpay.model.QpayRequest;
import code.ponfee.qpay.model.enums.BillType;

/**
 * 账单下载请求
 * @author fupf
 */
public class BillDownRequest extends QpayRequest {
    private static final long serialVersionUID = -5340525819853768134L;

    public BillDownRequest() {}

    public BillDownRequest(Date billDate, BillType billType) {
        this.billDate = billDate;
        this.billType = billType;
    }

    @JsonProperty(QpayFields.BILL_DATE)
    @JsonFormat(pattern = "yyyyMMdd", timezone = "GMT+8")
    private Date billDate;

    @JsonProperty(QpayFields.BILL_TYPE)
    private BillType billType;

    public Date getBillDate() {
        return billDate;
    }

    public BillType getBillType() {
        return billType;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public void setBillType(BillType billType) {
        this.billType = billType;
    }

}
