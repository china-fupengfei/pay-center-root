package code.ponfee.wechatpay.model.bill;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 账单信息
 */
public class BillDetail<T extends Bill> implements Serializable {

    private static final long serialVersionUID = 2763506940046963037L;

    /**
     * 账单记录
     */
    private List<T> bills;

    /**
     * 账单统计
     */
    private BillCount count;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static final BillDetail<?> EMPTY = new BillDetail(Collections.emptyList(), null);

    public BillDetail(List<T> bills, BillCount count) {
        this.bills = bills;
        this.count = count;
    }

    public List<T> getBills() {
        return bills;
    }

    public void setBills(List<T> bills) {
        this.bills = bills;
    }

    public BillCount getCount() {
        return count;
    }

    public void setCount(BillCount count) {
        this.count = count;
    }

    @SuppressWarnings("rawtypes")
    public static BillDetail empty() {
        return EMPTY;
    }

    @Override
    public String toString() {
        return "BillDetail{" + "bills=" + bills + ", count=" + count + '}';
    }
}
