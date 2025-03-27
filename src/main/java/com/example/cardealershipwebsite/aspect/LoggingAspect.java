package com.example.cardealershipwebsite.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/** Class to create logs. */
@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    /** Function to create logs after method execution. */
    @AfterReturning(pointcut = "execution(* com.example.cardealershipwebsite..*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        if (result instanceof ResponseEntity<?> responseEntity && responseEntity.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            String methodSignature = joinPoint.getSignature().toShortString();  // Получаем строку сигнатуры метода
            logger.warn("400 Error: {} returned from method: {}", result, methodSignature);
        }
    }




    /** Function to create logs after exception throwing. */
    @AfterThrowing(pointcut = "execution(* com.example.cardealershipwebsite..*(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        if (logger.isErrorEnabled()) {
            logger.error("Exception in: {} with cause: {}", joinPoint.getSignature().toShortString(), error.getMessage());
        }
    }
}