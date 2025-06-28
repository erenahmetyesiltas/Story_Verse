package com.sweproject.storyVerse.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.sweproject.storyVerse.service..*(..))")
    public void serviceMethods() {}

    @Around("serviceMethods()")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.info(">>> Calling " + className + "." + methodName + " with args: " + Arrays.toString(args));

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed(args);
            long duration = System.currentTimeMillis() - startTime;
            logger.info("<<< " + className + "." + methodName + " executed in" + duration + "ms, return " + result);
            return result;
        }
        catch (Throwable err) {
            logger.error("!!! Exception in " + className + "." + methodName +":" + err.getMessage() +" !!!");
            throw err;
        }
    }
}
