package com.jt.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class RunTimeAOP {

    //所有的业务方式  service
    @Around(value="execution(* com.jt.service..*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) {
        Long startTime = System.currentTimeMillis();
        Object obj = null;
        try {
            obj = joinPoint.proceed();
            Long endTime = System.currentTimeMillis();
            Long runTime = endTime - startTime;
            String targetName = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = joinPoint.getSignature().getName();
            Object[] args = joinPoint.getArgs();
            String kind = joinPoint.getKind();
            Object target = joinPoint.getTarget();
            log.info("类名:"+targetName);
            log.info("方法名:"+methodName);
            log.info("参数信息:"+args);
            log.info("kind:"+kind);
            log.info("目标对象:"+target);
            log.info("执行时间:"+runTime);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException();
        }	//执行目标方法
        return obj;
    }
}
