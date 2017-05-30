//package code.ponfee.pay.job;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import org.apache.commons.lang3.ObjectUtils.Null;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import cn.commons.lang.spring.SpringBeanUtils;
//import cn.insurance.service.IInsuranceService;
//import cn.order.bus.service.IBusOrderInnerService;
//import cn.order.service.IOrderService;
//import cn.purse.service.IPurseService;
//import code.ponfee.commons.jedis.JedisClient;
//import code.ponfee.commons.jedis.JedisLock;
//import code.ponfee.commons.json.Jsons;
//import code.ponfee.commons.model.Result;
//import code.ponfee.commons.util.Dates;
//import code.ponfee.commons.util.SpringContextHolder;
//import code.ponfee.pay.alipay.model.enums.RefundStatus;
//import code.ponfee.pay.dao.IPayDao;
//import code.ponfee.pay.common.Constants;
//import code.ponfee.pay.dto.RefundQueryResult;
//import code.ponfee.pay.dto.RefundQueryResult.RefundDetail;
//import code.ponfee.pay.enums.BizType;
//import code.ponfee.pay.enums.RefundType;
//import code.ponfee.pay.model.Refund;
//import code.ponfee.pay.service.IPayService;
//
///**
// * 任务调度：（未接收到异步通知的）退款状态定时更新
// * @author fupf
// */
//@Component
//public class RefundTaskScanner {
//    private static final String KEY_PREFIX = "task:refund:";
//    private static Logger logger = LoggerFactory.getLogger(RefundTaskScanner.class);
//
//    @Resource(name = "scanExecutor")
//    private TaskExecutor scanExecutor;
//
//    private @Resource JedisClient jedisClient;
//
//    //@Scheduled(cron = "0 0/1 * * * ?")
//    public void scanTest() {
//        Date endDate = Dates.addMinutes(new Date(), -0);
//        Date beginDate = Dates.addMinutes(endDate, -60);
//        process(60, beginDate, endDate, "0,2", 0);
//    }
//
//    /**
//     * 扫描：前15小时 ~ 前3小时
//     */
//    @Scheduled(cron = "0 0 0/2 * * ?")
//    public void scan1st() {
//        Date endDate = Dates.addHours(new Date(), -3);
//        Date beginDate = Dates.addHours(endDate, -12);
//        process(2 * 60 * 60, beginDate, endDate, "0,2", 0);
//    }
//
//    /**
//     * 扫描：前1天半 ~ 前9小时
//     */
//    @Scheduled(cron = "0 0 0/3 * * ?")
//    public void scan2nd() {
//        Date endDate = Dates.addHours(new Date(), -9);
//        Date beginDate = Dates.addHours(endDate, -27);
//        process(3 * 60 * 60, beginDate, endDate, "0,2", 1);
//    }
//
//    /**
//     * 扫描：前2天 ~ 前1天
//     */
//    @Scheduled(cron = "0 0 0/4 * * ?")
//    public void scan3rd() {
//        Date endDate = Dates.addDays(new Date(), -1);
//        Date beginDate = Dates.addDays(endDate, -1);
//        process(4 * 60 * 60, beginDate, endDate, "0,2", 2);
//    }
//
//    /**
//     * 扫描：前2天半 ~ 前1天半
//     */
//    @Scheduled(cron = "0 0 0/4 * * ?")
//    public void scan4th() {
//        Date endDate = Dates.addHours(new Date(), -36);
//        Date beginDate = Dates.addDays(endDate, -1);
//        process(4 * 60 * 60, beginDate, endDate, "0,2", 3);
//    }
//
//    /**
//     * 扫描：前3天半 ~ 前2天
//     */
//    @Scheduled(cron = "0 0 0/6 * * ?")
//    public void scan5th() {
//        Date endDate = Dates.addDays(new Date(), -2);
//        Date beginDate = Dates.addHours(endDate, -36);
//        process(6 * 60 * 60, beginDate, endDate, "0,2", 4);
//    }
//
//    /**
//     * 扫描：前4天半 ~ 前3天
//     */
//    @Scheduled(cron = "0 0 0/6 * * ?")
//    public void scan6th() {
//        Date endDate = Dates.addDays(new Date(), -3);
//        Date beginDate = Dates.addHours(endDate, -36);
//        process(6 * 60 * 60, beginDate, endDate, "0,2", 5);
//    }
//
//    /**
//     * 扫描：前5天半 ~ 前4天
//     */
//    @Scheduled(cron = "0 0 0/6 * * ?")
//    public void scan7th() {
//        Date endDate = Dates.addDays(new Date(), -4);
//        Date beginDate = Dates.addHours(endDate, -36);
//        process(6 * 60 * 60, beginDate, endDate, "0,2", 6);
//    }
//
//    /**
//     * 扫描：前7天 ~ 前5天
//     */
//    @Scheduled(cron = "0 0 0/12 * * ?")
//    public void scan8th() {
//        Date endDate = Dates.addDays(new Date(), -5);
//        Date beginDate = Dates.addDays(endDate, -2);
//        process(12 * 60 * 60, beginDate, endDate, "0,2", 7);
//    }
//
//    private void process(int timeout, Date beginTime, Date endTime, String status, int retryCount) {
//        String key = KEY_PREFIX + Thread.currentThread().getStackTrace()[2].getMethodName();
//        if (new JedisLock(jedisClient, key, timeout - 5).tryLock()) {
//            logger.info("退款定时扫描任务[{}]", key);
//            Map<String, Object> params = new HashMap<>();
//            params.put("beginTime", beginTime);
//            params.put("endTime", endTime);
//            params.put("status", status);
//            params.put("retryCount", retryCount);
//            for (final Refund refund : SpringContextHolder.getBean(IPayDao.class).listRefundTaskScan(params)) {
//                scanExecutor.execute(new RefundThreadTask(refund));
//            }
//        }
//    }
//
//    /**
//     * 线程处理类
//     */
//    private static class RefundThreadTask implements Runnable {
//        private final Refund refund;
//
//        private RefundThreadTask(Refund refund) {
//            this.refund = refund;
//        }
//
//        @Override
//        public void run() {
//            Map<String, String> params = new HashMap<>();
//            params.put(Constants.PARAM_REFUND_ID, refund.getRefundId());
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("refundId", refund.getRefundId());
//            map.put("originStatus", refund.getStatus());
//            map.put("turnStatus", refund.getStatus());
//            map.put("retryCount", refund.getRetryCount());
//
//            // 退款查询
//            Result<RefundQueryResult> result = SpringContextHolder.getBean(IPayService.class).refundQuery(params);
//            if (!result.isSuccess()) {
//                SpringContextHolder.getBean(IPayDao.class).updateRefundTaskScan(map);
//                return;
//            }
//
//            RefundQueryResult rs = result.getData();
//            for (RefundDetail detail : rs.getDetails()) {
//                if (!detail.getRefundId().equals(refund.getRefundId())) continue;
//
//                map.put("thirdRefundNo", detail.getThirdRefundNo());
//                map.put("tradeTime", detail.getTradeTime());
//                map.put("turnStatus", detail.getStatus().status());
//
//                if (detail.getStatus().status() == refund.getStatus() ||
//                    detail.getStatus() == code.ponfee.pay.enums.RefundStatus.HANDLING) {
//                    break; // 无需处理
//                }
//
//                // 状态改变，通知  订单/保险/钱包  处理
//                try {
//                    RefundType type = RefundType.from(refund.getRefundType());
//                    switch (type) {
//                        case PURSE: // 钱包提现
//                            IPurseService purseService = SpringBeanUtils.getBean(IPurseService.class);
//                            Result<?> _rs = purseService.withdrawResult(refund.getRefundId(), type, refund.getRefundNo(), detail.getStatus(), result.getMsg());
//                            if (!_rs.isSuccess()) logger.error("退款定时调度通知钱包失败：{}", _rs.getMsg());
//                            break;
//                        case INSURANCE: // 保险
//                            Map<String, String> _params = new HashMap<>();
//                            _params.put("orderId", refund.getRefundNo());
//                            _params.put("status", String.valueOf(detail.getStatus().status()));
//                            _params.put("refundOrderId", refund.getRefundId());
//                            try {
//                                IInsuranceService insuranceService = SpringBeanUtils.getBean(IInsuranceService.class);
//                                Result<Boolean> _result = insuranceService.insuranceRefundNotify(_params);
//                                if (!_result.isSuccess() || !_result.getResult()) {
//                                    logger.error("退款定时调度通知保险失败-[{}]-[{}]", Jsons.NORMAL.toJson(_params), _result.getMsg());
//                                }
//                            } catch (Exception e) {
//                                logger.error("退款定时调度通知保险异常-[{}]", Jsons.NORMAL.toJson(_params), e);
//                            }
//                            break;
//                        case ORDER: // 订单
//                        case TICKET:
//                        case CHANGE:
//                            Result<Null> _r = null;
//                            boolean isSuccess = detail.getStatus() == code.ponfee.pay.enums.RefundStatus.SUCCESS;
//                            switch (BizType.getEnum(rs.getBizType())) {
//                                case TRAIN:
//                                    IOrderService trainService = SpringBeanUtils.getBean(IOrderService.class);
//                                    _r = trainService.refundResult(refund.getRefundNo(), refund.getRefundType(), isSuccess ? 0 : 1, result.getMsg());
//                                    break;
//                                default:
//                                    int st = isSuccess ? RefundStatus.REFUND_SUCCESS.getCode() : RefundStatus.REFUND_FAIL.getCode();
//                                    IBusOrderInnerService busService = SpringBeanUtils.getBean(IBusOrderInnerService.class);
//                                    _r = busService.refundResult(rs.getOrderNo(), refund.getRefundNo(), refund.getRefundAmt(), refund.getRefundType(), st, result.getMsg());
//                                    break;
//                            }
//                            if (!_r.isSuccess()) logger.error("退款异步通知订单服务失败：{}", _r.getMsg());
//                            break;
//                        default:
//                            logger.error("无效的退款类型-[{}]", refund.getRefundType());
//                            break;
//                    }
//                } catch (Exception e) {
//                    logger.error("退款定时调度通知服务接口异常-[{}]", refund.getRefundNo(), e);
//                }
//                break;
//            }
//
//            SpringContextHolder.getBean(IPayDao.class).updateRefundTaskScan(map);
//        }
//    }
//
//    public static void main(String[] args) {
//        Date endDate = Dates.addDays(new Date(), -4);
//        Date beginDate = Dates.addHours(endDate, -36);
//        System.out.println(Dates.format(beginDate));
//        System.out.println(Dates.format(endDate));
//    }
//}