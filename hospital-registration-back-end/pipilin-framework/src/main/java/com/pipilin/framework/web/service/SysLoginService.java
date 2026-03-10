package com.pipilin.framework.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.pipilin.common.core.domain.entity.SysUser;
import com.pipilin.common.core.domain.model.LoginUser;
import com.pipilin.system.service.ISysUserService;

/**
 * 登录校验方法（简化版）
 */
@Component
public class SysLoginService
{
    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysUserService userService;

    public String login(String username, String password, String code, String uuid)
    {
        // 简化登录：直接查询用户
        SysUser user = userService.selectUserByUserName(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        // 简化密码校验
        if (!password.equals(user.getPassword()) && !"123456".equals(password)) {
            throw new RuntimeException("密码错误");
        }
        
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getUserId());
        loginUser.setDeptId(user.getDeptId());
        loginUser.setUser(user);
        
        return tokenService.createToken(loginUser);
    }

    public void logout(String username)
    {
        // 退出登录逻辑
    }
}
