package com.ethan.project.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @Description:请求相应日志AOP
 * @Auther: http://www.0808.icu
 */
@Aspect
@Slf4j
@Component
public class LogIntercepctor {

    /**
     * 执行拦截
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.eh.porject.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable{
        //计时
        StopWatch stopWatch=new StopWatch();
        stopWatch.start();
        //获取请求路径
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        //生成唯一ID
        String requestId = UUID.randomUUID().toString();
        String url = httpServletRequest.getRequestURI();
        //获取请求参数
        Object[] args = point.getArgs();
        String reqPram="["+ StringUtils.join(args,",")+"]";
        //输出请求日志
        log.info("request start,id:{},path:{},ip:{},params:{}",
                requestId,url,httpServletRequest.getRemoteHost(),reqPram);
        //执行原方法
        Object result = point.proceed();
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        //输出相应日志
        log.info("request end,id:{},cost:{}ms",requestId,totalTimeMillis);
        return result;
    }
}
