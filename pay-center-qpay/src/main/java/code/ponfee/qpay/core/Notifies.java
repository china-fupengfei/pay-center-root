package code.ponfee.qpay.core;

import java.util.HashMap;
import java.util.Map;

import code.ponfee.commons.xml.XmlMaps;
import code.ponfee.qpay.model.QpayFields;
import code.ponfee.qpay.model.enums.ReturnCode;

/**
 * 异步通知处理
 * @author fupf
 */
public class Notifies extends Component {

    Notifies(Qpay qpay) {
        super(qpay);
    }

    /**
     * 校验参数
     * @param data 待校验参数
     * @return 校验成功返回true，反之false
     */
    public boolean verifySign(final Map<String, String> data) {
        return super.doVerifySign(data, qpay.getMd5Key());
    }

    /**
     * 通知成功
     * @return 通知成功的XML消息
     */
    public String success() {
        Map<String, String> resp = new HashMap<>();
        resp.put(QpayFields.RETURN_CODE, ReturnCode.SUCCESS.name());
        //resp.put(QpayFields.RETURN_MSG, "OK");

        return new XmlMaps(resp).toXml();
    }

    /**
     * 通知不成功
     * @param errMsg 失败消息
     * @return 通知失败的XML消息
     */
    public String fail(String errMsg) {
        Map<String, String> resp = new HashMap<>();
        resp.put(QpayFields.RETURN_CODE, ReturnCode.FAIL.name());
        resp.put(QpayFields.RETURN_MSG, errMsg);

        return new XmlMaps(resp).toXml();
    }
}
