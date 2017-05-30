package code.ponfee.unionpay.model.order;

import code.ponfee.unionpay.model.UnionpayRequest;

public class OrderQueryRequest extends UnionpayRequest {

    private static final long serialVersionUID = -1014325293916446945L;

    public OrderQueryRequest() {}

    public OrderQueryRequest(String orderNumber) {
        super.setOrderNumber(orderNumber);
    }
}