package com.pipilin.framework.web.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.pipilin.common.core.domain.model.LoginUser;

/**
 * token验证处理（内存存储版）
 */
@Component
public class TokenService
{
    @Value("${token.expireTime:30}")
    private int expireTime;

    protected static final long MILLIS_MINUTE = 60 * 1000;

    // 使用内存存储token（生产环境建议使用Redis或数据库）
    private final Map<String, LoginUser> tokenStore = new ConcurrentHashMap<>();

    public String createToken(LoginUser loginUser)
    {
        String token = UUID.randomUUID().toString().replace("-", "");
        loginUser.setToken(token);
        refreshToken(loginUser);
        return token;
    }

    public void refreshToken(LoginUser loginUser)
    {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        tokenStore.put(loginUser.getToken(), loginUser);
    }

    public LoginUser getLoginUser(String token)
    {
        if (token == null || token.isEmpty()) {
            return null;
        }
        LoginUser loginUser = tokenStore.get(token);
        if (loginUser == null) {
            return null;
        }
        // 检查是否过期
        if (System.currentTimeMillis() > loginUser.getExpireTime()) {
            tokenStore.remove(token);
            return null;
        }
        return loginUser;
    }
    
    public void removeToken(String token)
    {
        if (token != null) {
            tokenStore.remove(token);
        }
    }
}
