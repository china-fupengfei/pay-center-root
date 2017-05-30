package code.ponfee.pay.job;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import code.ponfee.commons.jedis.JedisClient;
import code.ponfee.commons.jedis.JedisLock;
import code.ponfee.pay.dao.IPayDao;

/**
 * 任务调度：删除超时支付数据
 * @author fupf
 */
@Component
public class TimeoutPaymentCleaner {

    private static Logger logger = LoggerFactory.getLogger(TimeoutPaymentCleaner.class);

    @Resource
    private IPayDao payDao;

    private @Resource JedisClient jedisClient;

    /**
     * 每天的 3:00 AM定时清除
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void scheduleClear() {
        if (new JedisLock(jedisClient, "task:payment:clear", 3600).tryLock()) {
            int count = payDao.clearTimeoutPayment();
            logger.info("清除超时支付数据[{}]行", count);
        }
    }

}
