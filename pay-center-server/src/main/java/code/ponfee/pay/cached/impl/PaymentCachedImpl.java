package code.ponfee.pay.cached.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import code.ponfee.commons.jedis.JedisClient;
import code.ponfee.commons.util.MessageFormats;
import code.ponfee.pay.cached.IPaymentCached;
import code.ponfee.pay.model.PayChannel;

/**
 * redis缓存
 * @author fupf
 */
@Repository("paymentCached")
public class PaymentCachedImpl implements IPaymentCached {

    private @Resource JedisClient jedisClient;

    @Value("${jedis.default.cache.seconds:2592000}")
    private int defaultCacheSeconds;

    @Override
    public void cachePayChannelBySource(List<PayChannel> list, String source) {
        String key = MessageFormats.format(CACHE_KEY, source);
        jedisClient.valueOps().setObject(key.getBytes(), list, defaultCacheSeconds);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PayChannel> getPayChannelBySource(String source) {
        String key = MessageFormats.format(CACHE_KEY, source);
        return (List<PayChannel>) jedisClient.valueOps().getObject(key.getBytes(), ArrayList.class, defaultCacheSeconds);
    }

}
