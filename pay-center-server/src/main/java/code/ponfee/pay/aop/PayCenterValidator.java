package code.ponfee.pay.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import code.ponfee.commons.constrain.Constraints;
import code.ponfee.commons.constrain.MethodConstraint;

/**
 * 参数验证
 * @author fupf
 */
@Component("payCenterValidator")
@Aspect
public class PayCenterValidator extends MethodConstraint {

    /**
     * 针对@Constraints注解的方法
     * @param joinPoint
     * @param validator
     * @return
     * @throws Throwable
     */
    @Override
    @Around(value = "execution(public * code.ponfee.pay.service.impl.*.*(..)) && @annotation(validator)", argNames = "joinPoint,validator")
    public Object constrain(ProceedingJoinPoint joinPoint, Constraints validator) throws Throwable {
        return super.constrain(joinPoint, validator);
    }

}