package code.ponfee.pay.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import code.ponfee.commons.log.LogAnnotation;
import code.ponfee.commons.log.LoggerAspect;

/**
 * 支付服务日志
 * @author fupf
 */
@Component
@Aspect
public class PayCenterLogger extends LoggerAspect {

    @Around(value = "execution(public * code.ponfee.pay.service.impl.*.*(..)) && @annotation(log)", argNames = "pjp,log")
    @Override
    public Object around(ProceedingJoinPoint pjp, LogAnnotation log) throws Throwable {
        return super.around(pjp, log);
    }

}