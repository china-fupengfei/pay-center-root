package code.ponfee.tenpay.core;

import code.ponfee.tenpay.model.order.OrderQueryRequest;
import code.ponfee.tenpay.model.order.OrderQueryResponse;

public class Orders extends Component {
    private static final String ORDER_QUERY = "https://gw.tenpay.com/gateway/normalorderquery.xml";

    protected Orders(Tenpay tenPay) {
        super(tenPay);
    }

    public OrderQueryResponse queryOrder(OrderQueryRequest request) {
        return doPost(ORDER_QUERY, request, OrderQueryResponse.class);
    }
}
