package com.pipilin.web.controller.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pipilin.common.constant.CacheConstants;
import com.pipilin.common.core.domain.AjaxResult;
import com.pipilin.system.domain.SysCache;

/**
 * 缓存监控（已移除Redis，返回空数据）
 */
@RestController
@RequestMapping("/monitor/cache")
public class CacheController
{
    private final static List<SysCache> caches = new ArrayList<SysCache>();
    {
        caches.add(new SysCache(CacheConstants.LOGIN_TOKEN_KEY, "用户信息"));
        caches.add(new SysCache(CacheConstants.SYS_CONFIG_KEY, "配置信息"));
        caches.add(new SysCache(CacheConstants.SYS_DICT_KEY, "数据字典"));
        caches.add(new SysCache(CacheConstants.CAPTCHA_CODE_KEY, "验证码"));
        caches.add(new SysCache(CacheConstants.REPEAT_SUBMIT_KEY, "防重提交"));
        caches.add(new SysCache(CacheConstants.RATE_LIMIT_KEY, "限流处理"));
        caches.add(new SysCache(CacheConstants.PWD_ERR_CNT_KEY, "密码错误次数"));
    }
    
    @GetMapping()
    public AjaxResult getInfo() throws Exception
    {
        Map<String, Object> result = new HashMap<>(3);
        result.put("info", new HashMap<>());
        result.put("dbSize", 0);
        result.put("commandStats", new ArrayList<>());
        return AjaxResult.success(result);
    }
    
    @GetMapping("/getNames")
    public AjaxResult cache()
    {
        return AjaxResult.success(caches);
    }
    
    @GetMapping("/getKeys/{cacheName}")
    public AjaxResult getCacheKeys(@PathVariable String cacheName)
    {
        Set<String> cacheKeys = new HashSet<>();
        return AjaxResult.success(cacheKeys);
    }
    
    @GetMapping("/getValue/{cacheName}/{cacheKey}")
    public AjaxResult getCacheValue(@PathVariable String cacheName, @PathVariable String cacheKey)
    {
        SysCache sysCache = new SysCache(cacheName, cacheKey, "");
        return AjaxResult.success(sysCache);
    }
    
    @DeleteMapping("/clearCacheName/{cacheName}")
    public AjaxResult clearCacheName(@PathVariable String cacheName)
    {
        return AjaxResult.success();
    }
    
    @DeleteMapping("/clearCacheKey/{cacheKey}")
    public AjaxResult clearCacheKey(@PathVariable String cacheKey)
    {
        return AjaxResult.success();
    }
    
    @DeleteMapping("/clearCacheAll")
    public AjaxResult clearCacheAll()
    {
        return AjaxResult.success();
    }
}
