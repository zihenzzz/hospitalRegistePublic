package com.pipilin.framework.interceptor.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson2.JSON;
import com.pipilin.common.annotation.RepeatSubmit;
import com.pipilin.common.filter.RepeatedlyRequestWrapper;
import com.pipilin.common.utils.StringUtils;
import com.pipilin.common.utils.http.HttpHelper;
import com.pipilin.framework.interceptor.RepeatSubmitInterceptor;

/**
 * 判断请求url和数据是否和上一次相同（内存存储版）
 */
@Component
public class SameUrlDataInterceptor extends RepeatSubmitInterceptor
{
    public final String REPEAT_PARAMS = "repeatParams";
    public final String REPEAT_TIME = "repeatTime";

    @Value("${token.header}")
    private String header;

    // 使用内存存储
    private final Map<String, Map<String, Object>> repeatCache = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation)
    {
        String nowParams = "";
        if (request instanceof RepeatedlyRequestWrapper)
        {
            RepeatedlyRequestWrapper repeatedlyRequest = (RepeatedlyRequestWrapper) request;
            nowParams = HttpHelper.getBodyString(repeatedlyRequest);
        }

        if (StringUtils.isEmpty(nowParams))
        {
            nowParams = JSON.toJSONString(request.getParameterMap());
        }
        
        Map<String, Object> nowDataMap = new HashMap<>();
        nowDataMap.put(REPEAT_PARAMS, nowParams);
        nowDataMap.put(REPEAT_TIME, System.currentTimeMillis());

        String url = request.getRequestURI();
        String submitKey = StringUtils.trimToEmpty(request.getHeader(header));
        String cacheRepeatKey = url + submitKey;

        Map<String, Object> sessionMap = repeatCache.get(cacheRepeatKey);
        if (sessionMap != null && sessionMap.containsKey(url))
        {
            Map<String, Object> preDataMap = (Map<String, Object>) sessionMap.get(url);
            if (compareParams(nowDataMap, preDataMap) && compareTime(nowDataMap, preDataMap, annotation.interval()))
            {
                return true;
            }
        }
        
        Map<String, Object> cacheMap = new HashMap<>();
        cacheMap.put(url, nowDataMap);
        repeatCache.put(cacheRepeatKey, cacheMap);
        
        // 简单清理过期数据
        cleanExpiredData(annotation.interval());
        
        return false;
    }

    private boolean compareParams(Map<String, Object> nowMap, Map<String, Object> preMap)
    {
        String nowParams = (String) nowMap.get(REPEAT_PARAMS);
        String preParams = (String) preMap.get(REPEAT_PARAMS);
        return nowParams.equals(preParams);
    }

    private boolean compareTime(Map<String, Object> nowMap, Map<String, Object> preMap, int interval)
    {
        long time1 = (Long) nowMap.get(REPEAT_TIME);
        long time2 = (Long) preMap.get(REPEAT_TIME);
        return (time1 - time2) < interval;
    }
    
    private void cleanExpiredData(int interval)
    {
        long now = System.currentTimeMillis();
        repeatCache.entrySet().removeIf(entry -> {
            Map<String, Object> map = entry.getValue();
            for (Object value : map.values()) {
                if (value instanceof Map) {
                    Long time = (Long) ((Map<?, ?>) value).get(REPEAT_TIME);
                    if (time != null && (now - time) > interval * 2) {
                        return true;
                    }
                }
            }
            return false;
        });
    }
}
