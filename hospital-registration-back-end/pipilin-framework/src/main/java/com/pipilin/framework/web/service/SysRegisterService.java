package com.pipilin.framework.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.pipilin.common.constant.UserConstants;
import com.pipilin.common.core.domain.entity.SysUser;
import com.pipilin.common.core.domain.model.RegisterBody;
import com.pipilin.common.utils.SecurityUtils;
import com.pipilin.common.utils.StringUtils;
import com.pipilin.system.service.ISysUserService;

/**
 * 注册校验方法
 */
@Component
public class SysRegisterService
{
    @Autowired
    private ISysUserService userService;

    public String register(RegisterBody registerBody)
    {
        String username = registerBody.getUsername();
        String password = registerBody.getPassword();

        if (StringUtils.isEmpty(username)) {
            return "用户名不能为空";
        }
        if (StringUtils.isEmpty(password)) {
            return "用户密码不能为空";
        }

        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        
        if (!UserConstants.UNIQUE.equals(userService.checkUserNameUnique(sysUser))) {
            return "注册用户'" + username + "'失败，账号已存在";
        }
        
        boolean regFlag = userService.registerUser(sysUser);
        if (!regFlag) {
            return "注册失败,请联系系统管理人员";
        }
        return "";
    }
}
