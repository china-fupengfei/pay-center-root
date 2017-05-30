package code.ponfee.wechatpay.core;

import static code.ponfee.commons.util.Preconditions.checkNotEmpty;
import static code.ponfee.commons.util.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.RandomStringUtils;

import code.ponfee.commons.http.Http;
import code.ponfee.commons.json.Jsons;
import code.ponfee.commons.xml.XmlMaps;
import code.ponfee.commons.xml.XmlReaders;
import code.ponfee.wechatpay.exception.WechatpayException;
import code.ponfee.wechatpay.model.bill.Bill;
import code.ponfee.wechatpay.model.bill.BillCount;
import code.ponfee.wechatpay.model.bill.BillDetail;
import code.ponfee.wechatpay.model.bill.BillFields;
import code.ponfee.wechatpay.model.bill.CommonBill;
import code.ponfee.wechatpay.model.bill.RefundBill;
import code.ponfee.wechatpay.model.common.WechatpayField;
import code.ponfee.wechatpay.model.enums.BillType;

/**
 * 账单组件
 */
public class Bills extends Component {

    /**
     * 下载账单
     */
    private static final String DOWNLOAD = "https://api.mch.weixin.qq.com/pay/downloadbill";
    private static final String LINE_SEPARATOR = "\\n";
    private static final String FIELD_SEPARATOR = ",`";

    Bills(Wechatpay wepay) {
        super(wepay);
    }

    /**
     * 查询所有账单
     * @param deviceInfo 微信支付分配的终端设备号，填写此字段，只下载该设备号的对账单
     * @param date 账单的日期
     * @return 账单明细
     */
    public BillDetail<CommonBill> queryAll(String deviceInfo, String date) {
        String data = query(deviceInfo, date, BillType.ALL);
        return renderBillDetail(data, BillFields.ALL, CommonBill.class);
    }

    /**
     * 查询交易成功账单
     * @param deviceInfo 微信支付分配的终端设备号，填写此字段，只下载该设备号的对账单
     * @param date 账单的日期
     * @return 账单明细
     */
    public BillDetail<Bill> querySuccess(String deviceInfo, String date) {
        String data = query(deviceInfo, date, BillType.SUCCESS);
        return renderBillDetail(data, BillFields.SUCCESS, Bill.class);
    }

    /**
     * 查询退款账单
     * @param deviceInfo 微信支付分配的终端设备号，填写此字段，只下载该设备号的对账单
     * @param date 账单的日期
     * @return 账单明细
     */
    public BillDetail<RefundBill> queryRefund(String deviceInfo, String date) {
        String data = query(deviceInfo, date, BillType.REFUND);
        return renderBillDetail(data, BillFields.REFUND, RefundBill.class);
    }

    @SuppressWarnings("unchecked")
    private <T extends Bill> BillDetail<T> renderBillDetail(String billData, List<String> fields, Class<T> billClazz) {
        String[] dataLines = billData.split(LINE_SEPARATOR);
        if (dataLines.length > 0) {
            List<T> bills = new ArrayList<>();
            T bill;
            for (int i = 1; i < dataLines.length - 1; i++) {
                bill = renderBill(dataLines[i], fields, billClazz);
                bills.add(bill);
            }
            String countData = dataLines[dataLines.length - 1];
            BillCount count = renderCount(countData);
            return new BillDetail<>(bills, count);
        }
        return BillDetail.empty();
    }

    private BillCount renderCount(String countData) {
        // remove first `
        countData = countData.substring(1).replaceAll("\\r", "");
        String[] fieldVals = countData.split(FIELD_SEPARATOR);
        Map<String, Object> billCount = new HashMap<>();
        List<String> fieldNames = BillFields.COUNT;
        for (int i = 0; i < fieldNames.size(); i++) {
            billCount.put(fieldNames.get(i), fieldVals[i]);
        }
        return Jsons.NORMAL.parse(Jsons.NORMAL.stringify(billCount), BillCount.class);
    }

    private <T extends Bill> T renderBill(String dataLine, List<String> fieldNames, Class<T> billClazz) {
        // remove first `
        dataLine = dataLine.substring(1).replaceAll("\\r", "");
        String[] fieldVals = dataLine.split(FIELD_SEPARATOR);
        Map<String, Object> billData = new HashMap<>();
        for (int i = 0; i < fieldNames.size(); i++) {
            billData.put(fieldNames.get(i), fieldVals[i]);
        }
        return Jsons.NORMAL.parse(Jsons.NORMAL.stringify(billData), billClazz);
    }

    /**
     * 查询账单
     * @param deviceInfo 微信支付分配的终端设备号，填写此字段，只下载该设备号的对账单
     * @param date 账单的日期
     * @param type 账单类型
     *             @see code.ponfee.wechatpay.model.enums.BillType
     * @return 账单数据
     */
    public String query(String deviceInfo, String date, BillType type) {
        checkNotEmpty(date, "date");
        checkNotNull(type, "bill type can't be null");
        Map<String, String> down = buildDownloadParams(deviceInfo, date, type);
        String billData = Http.post(DOWNLOAD).data(new XmlMaps(down).toXml()).request();
        if (billData.startsWith("<xml>")) {
            XmlReaders readers = XmlReaders.create(billData);
            throw new WechatpayException(readers.getNodeText(WechatpayField.RETURN_CODE), readers.getNodeText(WechatpayField.RETURN_MSG));
        }
        return billData;
    }

    /**
     * 参数构建
     * @param deviceInfo
     * @param date
     * @param type
     * @return
     */
    private Map<String, String> buildDownloadParams(String deviceInfo, String date, BillType type) {
        Map<String, String> downloadParams = new TreeMap<>();

        buildConfigParams(downloadParams);

        downloadParams.put(WechatpayField.nonce_str, RandomStringUtils.randomAlphanumeric(16));
        downloadParams.put(WechatpayField.BILL_TYPE, type.name());
        downloadParams.put(WechatpayField.BILL_DATE, date);

        putIfNotEmpty(downloadParams, WechatpayField.DEVICE_INFO, deviceInfo);

        buildSignParams(downloadParams);

        return downloadParams;
    }
}
