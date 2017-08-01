package code.ponfee.wechatpay.core;

import java.util.HashMap;
import java.util.Map;

import code.ponfee.commons.xml.XmlMap;
import code.ponfee.wechatpay.model.common.WechatpayField;

/**
 * 通知组件
 */
public class Notifies extends Component {

    Notifies(Wechatpay wepay) {
        super(wepay);
    }
    
    /**
     * 校验参数
     * @param data 待校验参数
     * @return 校验成功返回true，反之false
     */
    public boolean verifySign(final Map<String, String> data) {
        return super.doVerifySign(data);
    }

    /**
     * 通知成功
     * @return 通知成功的XML消息
     */
    public String success(){
        Map<String, String> notifyParams = new HashMap<>();
        notifyParams.put(WechatpayField.RETURN_CODE, "SUCCESS");
        notifyParams.put(WechatpayField.RETURN_MSG, "OK");
        return new XmlMap(notifyParams).toXml();
    }

    /**
     * 通知不成功
     * @param errMsg 失败消息
     * @return 通知失败的XML消息
     */
    public String fail(String errMsg){
        Map<String, String> notifyParams = new HashMap<>();
        notifyParams.put(WechatpayField.RETURN_CODE, "FAIL");
        notifyParams.put(WechatpayField.RETURN_MSG, errMsg);
        return new XmlMap(notifyParams).toXml();
    }
}
