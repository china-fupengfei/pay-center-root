package code.ponfee.qpay.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import code.ponfee.commons.http.Http;
import code.ponfee.commons.xml.XmlMaps;
import code.ponfee.qpay.exception.QpayException;
import code.ponfee.qpay.model.bill.BillDownRequest;
import code.ponfee.qpay.model.bill.BillDownResult;
import code.ponfee.qpay.model.enums.BillType;

/**
 * 账单下载
 * @author fupf
 */
public final class Bills extends Component {

    /** 账单下载接口 */
    private static final String DOWN_URL = "https://qpay.qq.com/cgi-bin/sp_download/qpay_mch_statement_down.cgi";

    private static final String LINE_SEPARATOR = "\\n";
    private static final String COMMA_SEPARATOR = ",";
    private static final String FIELD_SEPARATOR = ",`";

    /*private String[] ALL_HEADS = { "交易时间", "商户号", "商户APPID", "子商户号", "子商户APPID", "用户标识", "设备号", "支付方式", "商户订单号", "QQ钱包订单号",
        "付款银行", "货币种类", "订单金额", "商户优惠金额", "商户应收金额", "QQ钱包优惠金额", "用户支付金额", "交易状态", "退款提交时间", "商户退款订单号", "QQ钱包退款订单号",
        "退款金额", "退还QQ钱包优惠金额", "退款状态", "退款成功时间", "退款方式", "商品名称", "商户数据包", "手续费金额", "费率" };
    
    private String[] SUCCESS_HEADS = { "交易时间", "商户号", "商户APPID", "子商户号", "子商户APPID", "用户标识", "设备号", "支付方式", "商户订单号", "QQ钱包订单号",
        "付款银行", "货币种类", "订单金额", "商户优惠金额", "商户应收金额", "QQ钱包优惠金额", "用户支付金额", "交易状态", "商品名称", "商户数据包", "手续费金额", "费率" };
    
    private String[] REFUND_HEADS = { "交易时间", "商户号", "商户APPID", "子商户号", "子商户APPID", "用户标识", "设备号", "支付方式", "商户订单号", "QQ钱包订单号",
        "付款银行", "货币种类", "订单金额", "商户优惠金额", "商户应收金额", "QQ钱包优惠金额", "用户支付金额", "交易状态", "退款提交时间", "商户退款订单号", "QQ钱包退款订单号",
        "退款金额", "退还QQ钱包优惠金额", "退款状态", "退款成功时间", "退款方式", "商品名称", "商户数据包", "手续费金额", "费率" };*/

    protected Bills(Qpay qpay) {
        super(qpay);
    }

    /**
     * 下载所有账单
     * @param billDate
     * @return
     */
    public BillDownResult downAll(Date billDate) {
        return download(new BillDownRequest(billDate, BillType.ALL));
    }

    /**
     * 下载支付成功账单
     * @param billDate
     * @return
     */
    public BillDownResult downSuccess(Date billDate) {
        return download(new BillDownRequest(billDate, BillType.SUCCESS));
    }

    /**
     * 下载退款账单
     * @param billDate
     * @return
     */
    public BillDownResult downRefund(Date billDate) {
        return download(new BillDownRequest(billDate, BillType.REFUND));
    }

    private BillDownResult download(BillDownRequest request) {
        String data = new XmlMaps(super.buildReqParams(request)).toXml();
        String billData = Http.post(DOWN_URL).data(data).request();
        if (billData.startsWith("<xml>")) {
            throw new QpayException("download bill fail: " + billData);
        }

        String[] dataLines = billData.split(LINE_SEPARATOR);
        BillDownResult result = new BillDownResult();
        result.setHeads(dataLines[0].split(COMMA_SEPARATOR));
        List<Object[]> bodyList = new ArrayList<>();
        for (int i = 1; i < dataLines.length - 1; i++) {
            String billLine = dataLines[i].replaceAll("\\r", "");
            bodyList.add(billLine.split(FIELD_SEPARATOR));
        }
        result.setFoots(dataLines[dataLines.length - 1].split(COMMA_SEPARATOR));
        return result;
    }

}
