//package code.ponfee.pay.job;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import code.ponfee.commons.jedis.JedisClient;
//import code.ponfee.commons.jedis.JedisLock;
//import code.ponfee.commons.model.Result;
//import code.ponfee.commons.util.Dates;
//import code.ponfee.commons.util.SpringContextHolder;
//import code.ponfee.pay.dao.IPayDao;
//import code.ponfee.pay.common.Constants;
//import code.ponfee.pay.dto.PayQueryResult;
//import code.ponfee.pay.enums.BizType;
//import code.ponfee.pay.enums.PaymentStatus;
//import code.ponfee.pay.model.Payment;
//import code.ponfee.pay.service.IPayService;
//
///**
// * 任务调度：（未接收到异步通知的）支付状态定时更新
// * @author fupf
// */
//@Component
//public class PaymentTaskScanner {
//    private static final String KEY_PREFIX = "task:payment:";
//    private static Logger logger = LoggerFactory.getLogger(PaymentTaskScanner.class);
//
//    @Resource(name = "scanExecutor")
//    private TaskExecutor scanExecutor;
//
//    private @Resource JedisClient jedisClient;
//
//    //@Scheduled(cron = "0 0/1 * * * ?")
//    public void scanTest() {
//        Date endDate = Dates.addMinutes(new Date(), -1);
//        Date beginDate = Dates.addMinutes(endDate, -500);
//        process(60, beginDate, endDate, "0", 0);
//    }
//
//    /**
//     * 扫描：前1小时 ~ 前15分钟
//     */
//    @Scheduled(cron = "0 0/5 * * * ?")
//    public void scan1st() {
//        Date endDate = Dates.addMinutes(new Date(), -15);
//        Date beginDate = Dates.addMinutes(endDate, -45);
//        process(5 * 60, beginDate, endDate, "0", 0);
//    }
//
//    /**
//     * 扫描：前3小时半 ~ 前30分钟
//     */
//    @Scheduled(cron = "0 0/30 * * * ?")
//    public void scan2nd() {
//        Date endDate = Dates.addMinutes(new Date(), -30);
//        Date beginDate = Dates.addHours(endDate, -3);
//        process(30 * 60, beginDate, endDate, "0", 1);
//    }
//
//    /**
//     * 扫描：前12小时 ~ 前2小时
//     */
//    @Scheduled(cron = "0 0 0/1 * * ?")
//    public void scan3rd() {
//        Date endDate = Dates.addHours(new Date(), -2);
//        Date beginDate = Dates.addHours(endDate, -10);
//        process(1 * 60 * 60, beginDate, endDate, "0", 2);
//    }
//
//    /**
//     * 扫描： 前1天 ~ 前6小时
//     */
//    @Scheduled(cron = "0 0 0/2 * * ?")
//    public void scan4th() {
//        Date endDate = Dates.addHours(new Date(), -6);
//        Date beginDate = Dates.addHours(endDate, -20);
//        process(2 * 60 * 60, beginDate, endDate, "0", 3);
//    }
//
//    private void process(int timeout, Date beginTime, Date endTime, String status, int retryCount) {
//        String key = KEY_PREFIX + Thread.currentThread().getStackTrace()[2].getMethodName();
//        if (new JedisLock(jedisClient, key, timeout - 5).tryLock()) {
//            logger.info("支付定时扫描任务[{}]", key);
//            Map<String, Object> params = new HashMap<>();
//            params.put("beginTime", beginTime);
//            params.put("endTime", endTime);
//            params.put("status", status);
//            params.put("retryCount", retryCount);
//            for (final Payment payment : SpringContextHolder.getBean(IPayDao.class).listPaymentTaskScan(params)) {
//                scanExecutor.execute(new PaymentThreadTask(payment));
//            }
//        }
//    }
//
//    /**
//     * 线程处理类
//     */
//    private static class PaymentThreadTask implements Runnable {
//        private final Payment pay;
//
//        private PaymentThreadTask(Payment pay) {
//            this.pay = pay;
//        }
//
//        @Override
//        public void run() {
//            Map<String, String> params = new HashMap<>();
//            params.put(Constants.PARAM_PAY_ID, pay.getPayId());
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("payId", pay.getPayId());
//            map.put("originStatus", pay.getStatus());
//            map.put("turnStatus", pay.getStatus());
//            map.put("retryCount", pay.getRetryCount());
//            Result<PayQueryResult> result = SpringContextHolder.getBean(IPayService.class).payQuery(params);
//            // 1、通知订单处理
//            if (result.isSuccess()) {
//                PayQueryResult rs = result.getData();
//                if (rs.getStatus().status() != pay.getStatus()) {
//                    try {
//                        BizType biz = BizType.getEnum(pay.getBizType());
//                        boolean isSuccess = rs.getStatus() == PaymentStatus.SUCCESS;
//                        Result<?> _result = null;
//                        switch (biz) {
//                            case PURSE: // 钱包充值
//                                if (!isSuccess) break;
//                                PurseChargeRequest req = new PurseChargeRequest(pay.getUserId(), pay.getPayId(), pay.getChannelType(), pay.getOrderAmt(), biz.getCode(), pay.getExtraData());
//                                req.setClientIp(pay.getClientIp());
//                                IPurseService purseService = SpringBeanUtils.getBean(IPurseService.class);
//                                _result = purseService.charge(req);
//                                break;
//                            case TRAIN:
//                                IOrderService trainService = SpringBeanUtils.getBean(IOrderService.class);
//                                PayResult st1 = isSuccess ? PayResult.PAY_SUCCESS : PayResult.PAY_FAIL;
//                                _result = trainService.payResult(pay.getOrderNo(), pay.getPayId(), st1.getCode(), pay.getOrderAmt());
//                                break;
//                            default:
//                                IBusOrderInnerService busService = SpringBeanUtils.getBean(IBusOrderInnerService.class);
//                                int code = isSuccess ? OrderStatus.PAY_SUCCESS.getCode() : OrderStatus.PAY_FAIL.getCode();
//                                _result = busService.payResult(pay.getOrderNo(), pay.getPayId(), code, pay.getOrderAmt(), pay.getTradeTime());
//                                break;
//                        }
//                        
//                        if (_result != null && !_result.isSuccess()) {
//                            logger.error("支付定时调度通知订单失败-[{}]-[{}]", pay.getPayId(), _result.getMsg());
//                        }
//                    } catch (Exception e) {
//                        logger.error("支付定时调度通知订单接口服务异常", e);
//                    }
//                }
//
//                map.put("thirdTradeNo", rs.getThirdTradeNo());
//                map.put("turnStatus", rs.getStatus().status());
//                map.put("tradeTime", rs.getTradeTime());
//            }
//
//            // 2、更新状态与重试次数
//            SpringContextHolder.getBean(IPayDao.class).updatePaymentTaskScan(map);
//        }
//    }
//
//}