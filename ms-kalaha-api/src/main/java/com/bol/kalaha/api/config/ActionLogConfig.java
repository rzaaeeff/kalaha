package com.bol.kalaha.api.config;

import com.bol.kalaha.api.logger.RzLogger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class ActionLogConfig {

    @Before("execution(* com.bol.kalaha.api.controller.*.*(..))" +
            "&& !execution(* com.bol.kalaha.api.controller.ErrorHandler.*(..))")
    public void beforeController(JoinPoint joinPoint) {
        RzLogger dpLogger = RzLogger.getLogger(joinPoint.getSignature().getDeclaringType());
        dpLogger.info("ActionLog." + joinPoint.getSignature().getName() + ".start");
    }

    @AfterReturning("execution(* com.bol.kalaha.api.controller.*.*(..))" +
            "&& !execution(* com.bol.kalaha.api.controller.ErrorHandler.*(..))")
    public void afterController(JoinPoint joinPoint) {
        RzLogger dpLogger = RzLogger.getLogger(joinPoint.getSignature().getDeclaringType());
        dpLogger.info("ActionLog." + joinPoint.getSignature().getName() + ".success");
    }

}
