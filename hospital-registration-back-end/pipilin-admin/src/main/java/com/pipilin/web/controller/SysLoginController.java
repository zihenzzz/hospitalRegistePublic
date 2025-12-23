package com.pipilin.web.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.pipilin.common.constant.Constants;
import com.pipilin.common.core.domain.AjaxResult;
import com.pipilin.common.core.domain.entity.SysMenu;
import com.pipilin.common.core.domain.entity.SysUser;
import com.pipilin.common.core.domain.model.LoginBody;
import com.pipilin.framework.web.service.SysLoginService;
import com.pipilin.framework.web.service.SysPermissionService;
import com.pipilin.system.service.ISysMenuService;
import com.pipilin.system.service.ISysUserService;

/**
 * 登录验证（简化版）
 */
@RestController
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysUserService userService;

    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), 
                loginBody.getCode(), loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        // 简化：返回默认管理员信息
        SysUser user = userService.selectUserByUserName("admin");
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        Set<String> permissions = new HashSet<>();
        permissions.add("*:*:*");
        
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        // 简化：返回管理员的所有菜单
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(1L);
        return AjaxResult.success(menuService.buildMenus(menus));
    }

    @PostMapping("/logout")
    public AjaxResult logout()
    {
        return AjaxResult.success();
    }
}
