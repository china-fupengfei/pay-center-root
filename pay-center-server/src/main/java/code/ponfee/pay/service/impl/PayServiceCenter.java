package code.ponfee.pay.service.impl;

import static code.ponfee.pay.common.Constants.DEFAULT_DATE_FORMAT;
import static code.ponfee.pay.common.Constants.PARAM_BIZ_TYPE;
import static code.ponfee.pay.common.Constants.PARAM_CHANNEL_TYPE;
import static code.ponfee.pay.common.Constants.PARAM_CLIENT_IP;
import static code.ponfee.pay.common.Constants.PARAM_CREATE_TM;
import static code.ponfee.pay.common.Constants.PARAM_EXPIRE_TIME;
import static code.ponfee.pay.common.Constants.PARAM_EXTRA_DATA;
import static code.ponfee.pay.common.Constants.PARAM_GOODS_BODY;
import static code.ponfee.pay.common.Constants.PARAM_HANDLE_FEE;
import static code.ponfee.pay.common.Constants.PARAM_ORDER_AMT;
import static code.ponfee.pay.common.Constants.PARAM_ORDER_NO;
import static code.ponfee.pay.common.Constants.PARAM_ORDER_TYPE;
import static code.ponfee.pay.common.Constants.PARAM_PAYMENT_NOTIFY_URL;
import static code.ponfee.pay.common.Constants.PARAM_PAYMENT_RETURN_URL;
import static code.ponfee.pay.common.Constants.PARAM_PAY_ID;
import static code.ponfee.pay.common.Constants.PARAM_REFUND_AMT;
import static code.ponfee.pay.common.Constants.PARAM_REFUND_ID;
import static code.ponfee.pay.common.Constants.PARAM_REFUND_NO;
import static code.ponfee.pay.common.Constants.PARAM_REFUND_NOTIFY_URL;
import static code.ponfee.pay.common.Constants.PARAM_REFUND_RATE;
import static code.ponfee.pay.common.Constants.PARAM_REFUND_REASON;
import static code.ponfee.pay.common.Constants.PARAM_REFUND_TYPE;
import static code.ponfee.pay.common.Constants.PARAM_REMARK;
import static code.ponfee.pay.common.Constants.PARAM_SOURCE;
import static code.ponfee.pay.common.Constants.PARAM_THIRD_REFUND_NO;
import static code.ponfee.pay.common.Constants.PARAM_THIRD_TRADE_NO;
import static code.ponfee.pay.common.Constants.PARAM_TRADE_TM;
import static code.ponfee.pay.common.Constants.PARAM_USER_ID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import code.ponfee.commons.constrain.Constraint;
import code.ponfee.commons.constrain.Constraint.Tense;
import code.ponfee.commons.constrain.Constraints;
import code.ponfee.commons.http.HttpParams;
import code.ponfee.commons.jedis.JedisClient;
import code.ponfee.commons.jedis.JedisLock;
import code.ponfee.commons.json.Jsons;
import code.ponfee.commons.log.LogAnnotation;
import code.ponfee.commons.model.Pager;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.util.Dates;
import code.ponfee.pay.common.IdGenerator;
import code.ponfee.pay.common.PayResultCode;
import code.ponfee.pay.dao.IBillDao;
import code.ponfee.pay.dao.IPayDao;
import code.ponfee.pay.dto.PayApplyResult;
import code.ponfee.pay.dto.PayNotifyResult;
import code.ponfee.pay.dto.PayQueryResult;
import code.ponfee.pay.dto.PayReturnResult;
import code.ponfee.pay.dto.RefundApplyResult;
import code.ponfee.pay.dto.RefundNotifyResult;
import code.ponfee.pay.dto.RefundQueryResult;
import code.ponfee.pay.dto.RefundQueryResult.RefundDetail;
import code.ponfee.pay.enums.BizType;
import code.ponfee.pay.enums.ChannelType;
import code.ponfee.pay.enums.PaymentStatus;
import code.ponfee.pay.enums.RefundStatus;
import code.ponfee.pay.enums.TradeType;
import code.ponfee.pay.exception.PayException;
import code.ponfee.pay.model.OrderBill;
import code.ponfee.pay.model.PayChannel;
import code.ponfee.pay.model.Payment;
import code.ponfee.pay.model.Refund;
import code.ponfee.pay.service.IPayService;

/**
 * 支付服务中心
 * @author fupf
 */
@Service("payService")
public class PayServiceCenter implements IPayService {

    private static Logger logger = LoggerFactory.getLogger(PayServiceCenter.class);
    private static final String LOCK_REFUND_PREFIX = "refund:";
    private static final String LOCK_PAYMENT_PREFIX = "payment:";
    private static final int[] PAY_SUCCESS = { PaymentStatus.SUCCESS.status(), PaymentStatus.CLOSED.status() };

    @Resource
    private IBillDao billDao;

    @Resource
    private IPayDao payDao;

    @Resource
    private IdGenerator idGenerator;

    private @Resource JedisClient jedisClient;

    @Value("${payment.notify.url}")
    private String paymentNotifyUrl;

    @Value("${payment.return.url}")
    private String paymentReturnUrl;

    @Value("${refund.notify.url}")
    private String refundNotifyUrl;

    @Value("${payment.charset:UTF-8}")
    private String charset;

    private final Map<String, IPayService> concretePayServices;

    public PayServiceCenter() {
        this.concretePayServices = new HashMap<>();
        for (ChannelType channel : ChannelType.values()) {
            if (channel.clazz() == null) continue;

            if (!concretePayServices.containsKey(channel.channel())) {
                try {
                    IPayService service = (IPayService) Class.forName(channel.clazz()).newInstance();
                    concretePayServices.put(channel.channel(), service);
                } catch (Exception e) {
                    logger.error("instance " + channel.clazz() + " error.", e);
                }
            }
        }
    }

    /**
     * 获取支付渠道类型（用户app端）
     */
    @LogAnnotation(desc = "查询可用的支付渠道类型")
    @Constraints({ @Constraint(notBlank = true, msg = "来源不能为空") })
    public @Override Result<List<PayChannel>> listPayChannelBySource(String source) {
        return Result.success(payDao.listBySource(source));
    }

    // --------------------支付-----------------------
    /**
     * 向第三方支付发起支付申请
     * 
     * <pre>
     *  {
     *    bizType=0,
     *    channelType=WECHAT_NATIVE,
     *    clientIp=183.16.194.73,
     *    orderAmt=1,
     *    orderNo=bizOrderNo,
     *    orderType=1,
     *    remark=remark001,
     *    source=IOS,
     *    userId=1
     *  }
     * </pre>
     */
    @LogAnnotation(desc = "支付请求")
    @Constraints({
        @Constraint(field = PARAM_CHANNEL_TYPE, notBlank = true, msg = "支付渠道类型不能为空"),
        @Constraint(field = PARAM_EXPIRE_TIME, tense = Tense.FUTURE, datePattern = DEFAULT_DATE_FORMAT, msg = "支付超时"),
        @Constraint(field = PARAM_ORDER_TYPE, regExp = "^(\\d{1,2})$", msg = "无效的订单类型"),
        @Constraint(field = PARAM_ORDER_NO, notBlank = true, msg = "订单号不能为空"),
        @Constraint(field = PARAM_ORDER_AMT, regExp = "^\\d{1,}$", msg = "无效的订单金额"),
        @Constraint(field = PARAM_SOURCE, notBlank = true, msg = "来源不能为空"),
        @Constraint(field = PARAM_BIZ_TYPE, regExp = "^(\\d{1,2})$", msg = "无效的业务类型"),
        @Constraint(field = PARAM_USER_ID, notNull = false, regExp = "^(\\d{1,})$", msg = "无效的用户id"),
        @Constraint(field = PARAM_CLIENT_IP, notNull = false, maxLen = 128),
        @Constraint(field = PARAM_GOODS_BODY, notBlank = true, maxLen = 128),
        @Constraint(field = PARAM_REMARK, notNull = false, maxLen = 256),
        @Constraint(field = PARAM_EXTRA_DATA, notNull = false, maxLen = 512)
    })
    public @Override Result<PayApplyResult> pay(Map<String, String> params) {
        String key = LOCK_PAYMENT_PREFIX + params.get(PARAM_ORDER_TYPE) + ":" + params.get(PARAM_ORDER_NO);
        if (!new JedisLock(jedisClient, key, 2).tryLock()) {
            return Result.failure(PayResultCode.PAY_FREQUENTLY_REQ);
        }

        Date createTime = new Date();
        String[] array = { PARAM_CHANNEL_TYPE, params.get(PARAM_CHANNEL_TYPE) };

        BizType bizType = BizType.getEnum(Integer.parseInt(params.get(PARAM_BIZ_TYPE)));
        ChannelType channelType = ChannelType.from(params.get(PARAM_CHANNEL_TYPE));

        try {
            params.put(PARAM_PAYMENT_NOTIFY_URL, HttpParams.buildUrlPath(paymentNotifyUrl, charset, array));
            params.put(PARAM_PAYMENT_RETURN_URL, HttpParams.buildUrlPath(paymentReturnUrl, charset, array));
            params.put(PARAM_PAY_ID, idGenerator.genPaymentId(bizType, channelType));
            Result<PayApplyResult> result = findRealPayService(params.get(PARAM_CHANNEL_TYPE)).pay(params);

            if (result.isSuccess()) {
                Payment pay = new Payment();
                pay.setPayId(params.get(PARAM_PAY_ID));
                pay.setChannelType(params.get(PARAM_CHANNEL_TYPE));
                pay.setOrderType(Integer.parseInt(params.get(PARAM_ORDER_TYPE)));
                pay.setOrderNo(params.get(PARAM_ORDER_NO));
                pay.setOrderAmt(Integer.parseInt(params.get(PARAM_ORDER_AMT)));
                pay.setSource(params.get(PARAM_SOURCE));
                pay.setBizType(Integer.parseInt(params.get(PARAM_BIZ_TYPE)));
                String userId = params.get(PARAM_USER_ID);
                if (StringUtils.isNotBlank(userId)) {
                    pay.setUserId(Integer.parseInt(userId));
                }
                pay.setCreateTime(createTime);
                pay.setClientIp(params.get(PARAM_CLIENT_IP));
                pay.setRemark(params.get(PARAM_REMARK));
                pay.setExtraData(params.get(PARAM_EXTRA_DATA));
                pay.setStatus(result.getData().getStatus().status());
                pay.setThirdTradeNo(result.getData().getThirdTradeNo());
                payDao.insertPayment(pay);

                result.getData().setPayId(params.get(PARAM_PAY_ID));
                result.getData().setChannelType(params.get(PARAM_CHANNEL_TYPE));
            } else {
                logger.warn("支付请求失败-[{}]-[{}]", Jsons.NORMAL.stringify(params), result.getMsg());
            }
            return result;
        } catch (Exception e) {
            logger.error("支付请求异常[{}]", Jsons.NORMAL.stringify(params), e);
            return new Result<>(PayResultCode.PAY_REQUEST_EXCEPTION.getCode(), e.getMessage());
        }
    }

    /**
     * 第三方支付同步跳转
     */
    @LogAnnotation(desc = "支付同步跳转处理")
    public @Override Result<PayReturnResult> payReturn(Map<String, String> params) {
        String channelType = parseChannel(params);
        try {
            Result<PayReturnResult> result = findRealPayService(channelType).payReturn(params);
            if (result.isSuccess()) {
                Payment pay = payDao.getPaymentByPayId(result.getData().getPayId());
                if (pay == null) {
                    throw new PayException("不存在的支付号[" + result.getData().getPayId() + "]");
                }

                result.getData().setChannelType(channelType);
                result.getData().setBizType(pay.getBizType());
                result.getData().setOrderType(pay.getOrderType());
                result.getData().setOrderNo(pay.getOrderNo());
            } else {
                logger.warn("支付同步跳转处理失败-[{}]-[{}]", Jsons.NORMAL.stringify(params), result.getMsg());
            }
            return result;
        } catch (Exception e) {
            logger.error("支付同步跳转处理异常[{}]", Jsons.NORMAL.stringify(params), e);
            return new Result<>(PayResultCode.PAY_RETURN_EXCEPTION.getCode(), e.getMessage());
        }
    }

    /**
     * 第三方支付异步通知回调
     */
    @LogAnnotation(desc = "支付异步通知处理")
    public @Override Result<PayNotifyResult> payNotify(Map<String, String> params) {
        String channelType = parseChannel(params);
        try {
            Result<PayNotifyResult> result = findRealPayService(channelType).payNotify(params);
            if (result.isSuccess()) {
                // 更新支付状态
                Payment pay = payDao.getPaymentByPayId(result.getData().getPayId());
                if (pay == null) {
                    throw new PayException("不存在的支付号[" + result.getData().getPayId() + "]");
                }

                result.getData().setChannelType(channelType);
                result.getData().setBizType(pay.getBizType());
                result.getData().setOrderType(pay.getOrderType());
                result.getData().setOrderNo(pay.getOrderNo());
                result.getData().setExtraData(pay.getExtraData());
                result.getData().setUserId(pay.getUserId());
                result.getData().setClientIp(pay.getClientIp());
                if (result.getData().getStatus() != PaymentStatus.WAITING) {
                    // 更新支付状态
                    pay = new Payment();
                    pay.setPayId(result.getData().getPayId());
                    pay.setTradeTime(result.getData().getTradeTime());
                    pay.setStatus(result.getData().getStatus().status());
                    pay.setThirdTradeNo(result.getData().getThirdTradeNo());
                    payDao.updatePayment(pay);
                }
            } else {
                logger.warn("支付异步通知处理失败-[{}]-[{}]", Jsons.NORMAL.stringify(params), result.getMsg());
            }
            return result;
        } catch (Exception e) {
            logger.error("支付异步通知处理异常[{}]", Jsons.NORMAL.stringify(params), e);
            return new Result<>(PayResultCode.PAY_NOTIFY_EXCEPTION.getCode(), e.getMessage());
        }
    }

    // --------------------------退款---------------------------
    /**
     * 向第三方支付发起退款申请
     * 
     * <pre>
     *  {
     *    payId=TP19070072473698,
     *    refundAmt=1,
     *    refundNo=bizRefundNo,
     *    refundRate=100,
     *    refundReason=退款原因,
     *    refundType=1,
     *    remark=refund remark
     *  }
     * </pre>
     */
    @LogAnnotation(desc = "退款请求")
    @Constraints({
        @Constraint(field = PARAM_PAY_ID, notBlank = true, msg = "支付号不能为空"),
        @Constraint(field = PARAM_REFUND_TYPE, regExp = "^(\\d{1,2})$", msg = "无效的退款类型"),
        @Constraint(field = PARAM_REFUND_NO, notBlank = true, msg = "退款业务单号不能为空"),
        @Constraint(field = PARAM_REFUND_AMT, regExp = "^(\\d{1,})$", msg = "无效的退款金额"),
        @Constraint(field = PARAM_REFUND_RATE, regExp = "^[1-9]\\d?|100$", msg = "退款费率必须介于1~100"),
        @Constraint(field = PARAM_HANDLE_FEE, notNull = false, regExp = "^(0)|([1-9][0-9]*)$"),
        @Constraint(field = PARAM_REFUND_REASON, notNull = false, maxLen = 256),
        @Constraint(field = PARAM_REMARK, notNull = false, maxLen = 256),
        @Constraint(field = PARAM_EXTRA_DATA, notNull = false, maxLen = 512)
    })
    public @Override Result<RefundApplyResult> refund(Map<String, String> params) {
        String payId = params.get(PARAM_PAY_ID);
        Lock lock = new JedisLock(jedisClient, LOCK_REFUND_PREFIX + payId, 2);
        lock.lock();
        try {
            // 查询支付记录
            Payment pay = payDao.getPaymentByPayId(payId);
            if (pay == null) {
                return new Result<>(PayResultCode.ILLEGAL_ARGS_ERR.getCode(), "支付号不存在[" + payId + "]");
            } else if (!ArrayUtils.contains(PAY_SUCCESS, pay.getStatus())) {
                return new Result<>(PayResultCode.ILLEGAL_ARGS_ERR.getCode(), "未支付成功的订单无法申请退款[" + payId + "]");
            }

            params.put(PARAM_USER_ID, String.valueOf(pay.getUserId()));

            // 查询退款记录
            int refundType = Integer.parseInt(params.get(PARAM_REFUND_TYPE));
            String refundNo = params.get(PARAM_REFUND_NO);
            Refund refund = payDao.getRefundByRefundNo(refundType, refundNo);
            if (refund != null) {
                // 已申请过退款
                Result<RefundApplyResult> result = repeatRefundProcess(pay, refund);
                if (result.isSuccess() && result.getData().getStatus() == RefundStatus.FAILURE) {
                    // ignore 继续退款请求：确认上一次退款失败
                } else {
                    // 返回退款结果（发需向第三方发起退款请求）：重复退款处理失败、退款处理中、退款成功
                    return result;
                }
            }

            String channelType = pay.getChannelType();

            // 发起退款申请
            String refundId = idGenerator.genRefundId(BizType.getEnum(pay.getBizType()), ChannelType.from(channelType));
            Refund rfd = new Refund();
            rfd.setCreateTime(new Date());
            rfd.setRefundId(refundId);
            rfd.setPayId(payId);
            rfd.setRefundAmt(Integer.parseInt(params.get(PARAM_REFUND_AMT)));
            rfd.setRefundRate(Integer.parseInt(params.get(PARAM_REFUND_RATE)));
            if (StringUtils.isNotBlank(params.get(PARAM_HANDLE_FEE))) {
                rfd.setHandleFee(Integer.parseInt(params.get(PARAM_HANDLE_FEE)));
            }
            rfd.setRefundReason(params.get(PARAM_REFUND_REASON));
            rfd.setRefundType(refundType);
            rfd.setRefundNo(refundNo);
            rfd.setRemark(params.get(PARAM_REMARK));
            rfd.setExtraData(params.get(PARAM_EXTRA_DATA));

            // 退款参数
            params.put(PARAM_REFUND_ID, refundId);
            params.put(PARAM_ORDER_AMT, String.valueOf(pay.getOrderAmt()));
            params.put(PARAM_CREATE_TM, Dates.format(pay.getCreateTime(), DEFAULT_DATE_FORMAT));
            if (pay.getTradeTime() == null) {
                params.put(PARAM_TRADE_TM, params.get(PARAM_CREATE_TM));
            } else {
                params.put(PARAM_TRADE_TM, Dates.format(pay.getTradeTime(), DEFAULT_DATE_FORMAT));
            }
            params.put(PARAM_THIRD_TRADE_NO, pay.getThirdTradeNo());
            params.put(PARAM_REFUND_NOTIFY_URL, HttpParams.buildUrlPath(refundNotifyUrl, charset, PARAM_CHANNEL_TYPE, channelType));
            params.put(PARAM_CHANNEL_TYPE, channelType);

            // 先查询支付情况
            Result<PayQueryResult> payRes = payQuery(params);
            if (!payRes.isSuccess()) {
                return new Result<>(PayResultCode.REFUND_TRADE_FAILURE.getCode(), "交易不存在或失败");
            } else if (payRes.getData().getStatus() != PaymentStatus.SUCCESS) {
                return new Result<>(PayResultCode.REFUND_TRADE_FAILURE.getCode(), "无法退款的交易状态：" + payRes.getData().getStatus());
            }

            Result<RefundApplyResult> result = findRealPayService(channelType).refund(params);
            if (!result.isSuccess() || result.getData().getStatus() == RefundStatus.FAILURE) {
                logger.warn("退款申请失败-[{}]-[{}]", Jsons.NORMAL.stringify(params), result.getMsg());
            } else {
                RefundApplyResult rs = result.getData();
                rs.setOrderType(pay.getOrderType());
                rs.setOrderNo(pay.getOrderNo());
                rs.setOrderAmt(pay.getOrderAmt());
                rs.setThirdTradeNo(pay.getThirdTradeNo());

                // 保存退款数据
                rfd.setTradeTime(rs.getTradeTime());
                rfd.setStatus(rs.getStatus().status());
                rfd.setThirdRefundNo(rs.getThirdRefundNo());
                payDao.insertRefund(rfd);
            }
            return result;
        } catch (Exception e) {
            logger.error("退款请求异常[{}]", Jsons.NORMAL.stringify(params), e);
            return new Result<>(PayResultCode.REFUND_REQUEST_EXCEPTION.getCode(), e.getMessage());
        } finally {
            //lock.unlock();
        }
    }

    /**
     * 第三方支付退款通知回调
     */
    @LogAnnotation(desc = "退款异步通知处理")
    public @Override Result<RefundNotifyResult> refundNotify(Map<String, String> params) {
        String channelType = parseChannel(params);
        Refund _refund = null;
        try {
            Result<RefundNotifyResult> result = findRealPayService(channelType).refundNotify(params);
            RefundNotifyResult rs = result.getData();
            if (!result.isSuccess() || rs == null) {
                logger.error("通知结果退款失败[{}]", Jsons.NORMAL.stringify(result));
                return result;
            }

            Refund refund = payDao.getRefundByRefundId(rs.getRefundId());
            Payment pay = payDao.getPaymentByPayId(refund.getPayId());
            rs.setPayId(pay.getPayId());
            rs.setOrderType(pay.getOrderType());
            rs.setOrderNo(pay.getOrderNo());
            rs.setOrderAmt(pay.getOrderAmt());
            rs.setThirdTradeNo(pay.getThirdTradeNo());
            rs.setBizType(pay.getBizType());
            rs.setRefundType(refund.getRefundType());
            rs.setRefundNo(refund.getRefundNo());
            rs.setExtraData(refund.getExtraData());

            // 更新状态
            _refund = new Refund();
            _refund.setRefundId(rs.getRefundId());
            _refund.setThirdRefundNo(rs.getThirdRefundNo());
            _refund.setStatus(rs.getStatus().status());
            _refund.setTradeTime(rs.getTradeTime());
            payDao.updateRefund(_refund);

            return result;
        } catch (Exception e) {
            logger.error("退款异步通知处理异常[{}]", Jsons.NORMAL.stringify(params), e);
            return new Result<>(PayResultCode.REFUND_NOTIFY_EXCEPTION.getCode(), e.getMessage());
        }
    }

    // -----------------------------查询--------------------------------
    /**
     * 支付查询
     */
    @LogAnnotation(desc = "支付查询")
    @Constraints({ @Constraint(field = PARAM_PAY_ID, notBlank = true, msg = "必须传入支付号") })
    public @Override Result<PayQueryResult> payQuery(Map<String, String> params) {
        Payment pay = payDao.getPaymentByPayId(params.get(PARAM_PAY_ID));
        if (pay == null) {
            return new Result<>(PayResultCode.PAY_NOT_EXISTS.getCode(), "支付号不存在[" + params.get(PARAM_PAY_ID) + "]");
        }
        Date tradeTime = pay.getTradeTime() == null ? pay.getCreateTime() : pay.getTradeTime();
        params.put(PARAM_THIRD_TRADE_NO, pay.getThirdTradeNo());
        params.put(PARAM_CHANNEL_TYPE, pay.getChannelType());
        params.put(PARAM_TRADE_TM, Dates.format(tradeTime, DEFAULT_DATE_FORMAT));

        try {
            return findRealPayService(pay.getChannelType()).payQuery(params);
        } catch (Exception e) {
            //logger.warn(e.getMessage());
            return new Result<>(PayResultCode.QUERY_TRADE_FAILURE.getCode(), e.getMessage());
        }
    }

    /**
     * 退款查询
     */
    @LogAnnotation(desc = "退款查询")
    @Constraints({ @Constraint(field = PARAM_REFUND_ID, notBlank = true, msg = "必须传入退款号") })
    public @Override Result<RefundQueryResult> refundQuery(Map<String, String> params) {
        Refund refund = payDao.getRefundByRefundId(params.get(PARAM_REFUND_ID));
        if (refund == null) {
            String msg = "退款记录不存在[" + params.get(PARAM_REFUND_ID) + "]";
            logger.error(msg);
            return new Result<>(PayResultCode.REFUND_NOT_EXISTS.getCode(), msg);
        }

        Payment pay = payDao.getPaymentByPayId(refund.getPayId());
        if (pay == null) {
            String msg = "支付记录不存在[" + refund.getPayId() + "]-[" + refund.getRefundId() + "]";
            logger.error(msg);
            return new Result<>(PayResultCode.PAY_NOT_EXISTS.getCode(), msg);
        }

        Date tradeTime = refund.getTradeTime() == null ? refund.getCreateTime() : refund.getTradeTime();
        params.put(PARAM_THIRD_REFUND_NO, refund.getThirdRefundNo());
        params.put(PARAM_PAY_ID, refund.getPayId());
        params.put(PARAM_TRADE_TM, Dates.format(tradeTime, DEFAULT_DATE_FORMAT));
        params.put(PARAM_THIRD_TRADE_NO, pay.getThirdTradeNo());
        params.put(PARAM_CHANNEL_TYPE, pay.getChannelType());

        // 发起查询
        try {
            Result<RefundQueryResult> result = findRealPayService(pay.getChannelType()).refundQuery(params);
            if (result.isSuccess()) {
                RefundQueryResult rs = result.getData();
                if (rs.getOrderAmt() == null) rs.setOrderAmt(pay.getOrderAmt());
                if (rs.getPayId() == null) rs.setPayId(pay.getPayId());
                if (rs.getThirdTradeNo() == null) rs.setThirdTradeNo(pay.getThirdTradeNo());
                rs.setOrderType(pay.getOrderType());
                rs.setOrderNo(pay.getOrderNo());
                rs.setBizType(pay.getBizType());
            }
            return result;
        } catch (Exception e) {
            //logger.warn(e.getMessage());
            return new Result<>(PayResultCode.QUERY_TRADE_FAILURE.getCode(), e.getMessage());
        }
    }

    /**
     * 订单账单（用户mis后台查询）
     */
    @LogAnnotation(desc = "查询订单的支付流水")
    @Constraints({ @Constraint(notBlank = true, msg = "原始支付号不能为空") })
    public @Override Result<List<OrderBill>> orderBill(String originPayId, String... changePayIds) {
        try {
            List<OrderBill> orderBills = new ArrayList<>();

            orderBills.addAll(handleOrderBill(Arrays.asList(originPayId), false));

            if (changePayIds == null) changePayIds = ArrayUtils.EMPTY_STRING_ARRAY;
            orderBills.addAll(handleOrderBill(Arrays.asList(changePayIds), true));

            return Result.success(orderBills);
        } catch (Exception e) {
            logger.error(null, e);
            return new Result<>(PayResultCode.ILLEGAL_ARGS_ERR.getCode(), e.getMessage());
        }
    }

    @LogAnnotation(desc = "查询退款数据")
    @Constraints({
        @Constraint(field = PARAM_REFUND_TYPE, regExp = "^(\\d{1,2})$"),
        @Constraint(field = "beginTime", tense = Tense.PAST)
    })
    public @Override Result<Pager<Refund>> queryRefundsForPage(Map<String, ?> params) {
        int errCode = PayResultCode.ILLEGAL_ARGS_ERR.getCode();
        if (params.get("endTime") == null) {
            return new Result<>(errCode, "结束时间不能为空");
        } else if (!((Date) params.get("endTime")).after((Date) params.get("beginTime"))) {
            return new Result<>(errCode, "结束时间必须大于开始时间");
        }
        return Result.success(billDao.queryRefundsForPage(params));
    }

    // --------------------------private method----------------------------
    /**
     * 获取第三方支付服务类
     * @param channelType
     * @return
     */
    private IPayService findRealPayService(String channelType) {
        ChannelType channel = ChannelType.from(channelType);
        return concretePayServices.get(channel.channel());
    }

    /**
     * 解决TEN_PAY返回channelType="TENPAY_APP?attach="的BUG问题
     * @param params
     * @return
     */
    private String parseChannel(Map<String, String> params) {
        String channelType = params.get(PARAM_CHANNEL_TYPE);
        if (channelType.indexOf("?") > 0) {
            String[] array = channelType.split("\\?");
            params.put(PARAM_CHANNEL_TYPE, array[0]);
            if (array.length > 1) {
                array = array[1].split("=");
                params.put(array[0], array.length == 1 ? "" : array[1]);
            }
        }
        return params.get(PARAM_CHANNEL_TYPE);
    }

    /**
     * 重复退款请求
     * @param pay
     * @param refund
     * @return
     */
    private Result<RefundApplyResult> repeatRefundProcess(Payment pay, Refund refund) {
        // 退款成功，不再做查询处理
        if (refund.getStatus() == RefundStatus.SUCCESS.status()) {
            RefundApplyResult result = new RefundApplyResult();
            result.setOrderAmt(pay.getOrderAmt());
            result.setOrderType(pay.getOrderType());
            result.setOrderNo(pay.getOrderNo());
            result.setThirdTradeNo(pay.getThirdTradeNo());
            result.setRefundId(refund.getRefundId());
            result.setThirdRefundNo(refund.getThirdRefundNo());
            result.setStatus(RefundStatus.SUCCESS);
            result.setTradeTime(refund.getTradeTime());
            return Result.success(result);
        }

        // 其它退款状态
        Map<String, String> params = new HashMap<>();
        params.put("refundId", refund.getRefundId());
        Result<RefundQueryResult> result = this.refundQuery(params);
        if (result.isSuccess()) {
            for (RefundDetail detail : result.getData().getDetails()) {
                if (detail.getRefundId().equals(refund.getRefundId())) {
                    if (detail.getStatus().status() != refund.getStatus()) {
                        // 更新状态
                        Refund _refund = new Refund();
                        _refund.setRefundId(detail.getRefundId());
                        _refund.setThirdRefundNo(detail.getThirdRefundNo());
                        _refund.setStatus(detail.getStatus().status());
                        _refund.setTradeTime(detail.getTradeTime());
                        payDao.updateRefund(_refund);
                    }

                    // 返回退款结果
                    RefundApplyResult result0 = new RefundApplyResult();
                    result0.setRefundId(detail.getRefundId());
                    result0.setOrderAmt(pay.getOrderAmt());
                    result0.setOrderType(pay.getOrderType());
                    result0.setOrderNo(pay.getOrderNo());
                    result0.setThirdTradeNo(pay.getThirdTradeNo());
                    result0.setThirdRefundNo(detail.getThirdRefundNo());
                    result0.setStatus(detail.getStatus());
                    result0.setTradeTime(detail.getTradeTime());
                    return Result.success(result0);
                }
            }

            String msg = "在第三方支付中未查询到退款记录[" + refund.getRefundId() + "]";
            return new Result<>(PayResultCode.REFUND_NOT_EXISTS.getCode(), msg);
        } else {
            return new Result<>(result.getCode(), result.getMsg());
        }
    }

    /**
     * 订单账单解析
     * @param payIds
     * @param isChange
     * @return
     */
    private List<OrderBill> handleOrderBill(List<String> payIds, boolean isChange) {
        if (payIds == null || payIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<OrderBill> bills = new ArrayList<>();
        for (String payId : payIds) {
            if (StringUtils.isBlank(payId)) {
                //bills.add(null);
                continue;
            }

            OrderBill payBill = billDao.getPayBill(payId);
            if (payBill == null) {
                throw new IllegalArgumentException("不存在的支付号[" + payId + "]");
            }

            payBill.setPayChannel(ChannelType.from(payBill.getPayChannel()).channelName());
            payBill.setTradeType(TradeType.PAY.type());
            payBill.setChange(isChange);
            bills.add(payBill);

            List<OrderBill> refundBills = billDao.listRefundBill(payId);
            for (OrderBill bill : refundBills) {
                bill.setTradeType(TradeType.REFUND.type());
                bill.setChange(isChange);
                bill.setBizType(payBill.getBizType());
                bill.setPayChannel(payBill.getPayChannel());
                bill.setUserId(payBill.getUserId());
            }
            bills.addAll(refundBills);
        }

        return bills;
    }

}
