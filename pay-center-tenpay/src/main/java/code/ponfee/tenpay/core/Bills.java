package code.ponfee.tenpay.core;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import code.ponfee.commons.http.Http;
import code.ponfee.commons.http.HttpParams;
import code.ponfee.commons.xml.XmlReader;
import code.ponfee.tenpay.exception.TenPayException;

/**
 * 账单
 * @author fupf
 */
public class Bills extends Component {

    private static final String DOWN_URL = "http://api.mch.tenpay.com/cgi-bin/mchdown_real_new.cgi";

    protected Bills(Tenpay tenpay) {
        super(tenpay);
    }

    /**
     * 查询账单
     * @param mchtype
     * @param transTime
     * @return
     */
    public Map<String, Object> query(int mchtype, String transTime) {
        Map<String, String> params = new HashMap<>();
        params.put("spid", super.tenpay.getPartner());
        params.put("stamp", String.valueOf(System.currentTimeMillis()));
        params.put("cft_signtype", "0");
        params.put("mchtype", String.valueOf(mchtype)); // 下载对账单类型：0-默认值，所有订单；1-成功支付订单；2-退款订单
        params.put("trans_time", transTime);

        super.buildSignParam(params);
        try {
            String requestBody = HttpParams.buildParams(params, super.tenpay.getInputCharset());
            String response = Http.post(DOWN_URL).data(requestBody).request();
            List<String> lines = IOUtils.readLines(new StringReader(response));

            Map<String, Object> result = new LinkedHashMap<>();
            if ("html".equalsIgnoreCase(lines.get(0).trim())) {
                XmlReader read = XmlReader.create(response);
                String[] msg = read.getNodeText("body").split(":");
                result.put("code", msg[0]);
                result.put("msg", msg[1]);
            } else {
                String[] thead = lines.get(0).split(",");
                List<String[]> tbody = new ArrayList<>();
                for (int i = 1; i < lines.size(); i++) {
                    String[] values = lines.get(i).split(",`");
                    values[0] = values[0].substring(1, values[0].length() - 1);
                    tbody.add(values);
                }
                result.put("code", "0");
                result.put("msg", "success");
                result.put("thead", thead);
                result.put("tbody", tbody);
            }
            return result;
        } catch(IOException e) {
            throw new TenPayException(e);
        }
    }
}
