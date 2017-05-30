package code.ponfee.pay.cached;

/**
 * 键名
 * @author fupf
 */
public interface ICacheKey {

    static final String KEY_PREFIX = "payment:";
    static final String CACHE_KEY = KEY_PREFIX + "config:cache:#{source}";
}