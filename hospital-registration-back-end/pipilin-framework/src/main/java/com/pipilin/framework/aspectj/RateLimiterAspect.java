package com.pipilin.framework.aspectj;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.pipilin.common.annotation.RateLimiter;
import com.pipilin.common.enums.LimitType;
import com.pipilin.common.exception.ServiceException;
import com.pipilin.common.utils.ServletUtils;
import com.pipilin.common.utils.ip.IpUtils;

/**
 * 限流处理（内存存储版）
 */
@Aspect
@Component
public class RateLimiterAspect
{
    private static final Logger log = LoggerFactory.getLogger(RateLimiterAspect.class);

    // 限流计数器
    private final Map<String, RateLimitEntry> limitCache = new ConcurrentHashMap<>();

    private static class RateLimitEntry {
        AtomicInteger count = new AtomicInteger(0);
        long startTime = System.currentTimeMillis();
    }

    @Before("@annotation(rateLimiter)")
    public void doBefore(JoinPoint point, RateLimiter rateLimiter) throws Throwable
    {
        int time = rateLimiter.time();
        int count = rateLimiter.count();

        String combineKey = getCombineKey(rateLimiter, point);
        
        try
        {
            RateLimitEntry entry = limitCache.computeIfAbsent(combineKey, k -> new RateLimitEntry());
            
            long now = System.currentTimeMillis();
            // 如果超过时间窗口，重置计数器
            if (now - entry.startTime > time * 1000L) {
                entry.count.set(0);
                entry.startTime = now;
            }
            
            int current = entry.count.incrementAndGet();
            
            if (current > count)
            {
                throw new ServiceException("访问过于频繁，请稍候再试");
            }
            log.info("限制请求'{}',当前请求'{}',缓存key'{}'", count, current, combineKey);
        }
        catch (ServiceException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new RuntimeException("服务器限流异常，请稍候再试");
        }
    }

    public String getCombineKey(RateLimiter rateLimiter, JoinPoint point)
    {
        StringBuffer stringBuffer = new StringBuffer(rateLimiter.key());
        if (rateLimiter.limitType() == LimitType.IP)
        {
            stringBuffer.append(IpUtils.getIpAddr(ServletUtils.getRequest())).append("-");
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        stringBuffer.append(targetClass.getName()).append("-").append(method.getName());
        return stringBuffer.toString();
    }
}
