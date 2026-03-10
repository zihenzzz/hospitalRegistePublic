package com.pipilin.common.core.domain.model;

import java.io.Serializable;
import java.util.Set;
import com.pipilin.common.core.domain.entity.SysUser;

/**
 * 登录用户身份权限
 */
public class LoginUser implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Long userId;
    private Long deptId;
    private String token;
    private Long loginTime;
    private Long expireTime;
    private String ipaddr;
    private String loginLocation;
    private String browser;
    private String os;
    private Set<String> permissions;
    private SysUser user;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Long getLoginTime() { return loginTime; }
    public void setLoginTime(Long loginTime) { this.loginTime = loginTime; }
    public Long getExpireTime() { return expireTime; }
    public void setExpireTime(Long expireTime) { this.expireTime = expireTime; }
    public String getIpaddr() { return ipaddr; }
    public void setIpaddr(String ipaddr) { this.ipaddr = ipaddr; }
    public String getLoginLocation() { return loginLocation; }
    public void setLoginLocation(String loginLocation) { this.loginLocation = loginLocation; }
    public String getBrowser() { return browser; }
    public void setBrowser(String browser) { this.browser = browser; }
    public String getOs() { return os; }
    public void setOs(String os) { this.os = os; }
    public Set<String> getPermissions() { return permissions; }
    public void setPermissions(Set<String> permissions) { this.permissions = permissions; }
    public SysUser getUser() { return user; }
    public void setUser(SysUser user) { this.user = user; }
    public String getPassword() { return user != null ? user.getPassword() : null; }
    public String getUsername() { return user != null ? user.getUserName() : null; }
}
