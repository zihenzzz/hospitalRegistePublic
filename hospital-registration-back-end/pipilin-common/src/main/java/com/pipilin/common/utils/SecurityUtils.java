package com.pipilin.common.utils;

import com.pipilin.common.core.domain.model.LoginUser;

/**
 * 安全服务工具类（简化版，无Spring Security）
 */
public class SecurityUtils
{
    private static final ThreadLocal<LoginUser> loginUserHolder = new ThreadLocal<>();

    public static void setLoginUser(LoginUser loginUser) {
        loginUserHolder.set(loginUser);
    }

    public static Long getUserId()
    {
        LoginUser user = getLoginUser();
        return user != null ? user.getUserId() : 1L;
    }

    public static Long getDeptId()
    {
        LoginUser user = getLoginUser();
        return user != null ? user.getDeptId() : 100L;
    }

    public static String getUsername()
    {
        LoginUser user = getLoginUser();
        return user != null ? user.getUsername() : "admin";
    }

    public static LoginUser getLoginUser()
    {
        return loginUserHolder.get();
    }

    public static String encryptPassword(String password)
    {
        // 简化版：直接返回原密码（生产环境应使用加密）
        return password;
    }

    public static boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        return rawPassword != null && rawPassword.equals(encodedPassword);
    }

    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }
}
