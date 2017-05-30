package code.ponfee.pay.cached;

import java.util.List;

import code.ponfee.pay.model.PayChannel;

/**
 * redis缓存接口
 * @author fupf
 */
public interface IPaymentCached extends ICacheKey {

    /**
     * 设置缓存
     * @param config
     */
    void cachePayChannelBySource(List<PayChannel> list, String source);

    /**
     * 取缓存数据
     * @param source
     * @return
     */
    List<PayChannel> getPayChannelBySource(String source);

}