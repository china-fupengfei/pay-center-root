package code.ponfee.qpay.model.bill;

import java.io.Serializable;
import java.util.List;

/**
 * 账单下载结果
 * @author fupf
 */
public class BillDownResult implements Serializable {
    private static final long serialVersionUID = -7071090984809080168L;

    private String[] heads;
    private List<Object[]> bodysList;
    private Object[] foots;

    public String[] getHeads() {
        return heads;
    }

    public List<Object[]> getBodysList() {
        return bodysList;
    }

    public Object[] getFoots() {
        return foots;
    }

    public void setHeads(String[] heads) {
        this.heads = heads;
    }

    public void setBodysList(List<Object[]> bodysList) {
        this.bodysList = bodysList;
    }

    public void setFoots(Object[] foots) {
        this.foots = foots;
    }

}
