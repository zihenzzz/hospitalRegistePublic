package com.pipilin.web.controller.portal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pipilin.common.annotation.Anonymous;
import com.pipilin.common.core.controller.BaseController;
import com.pipilin.common.core.domain.AjaxResult;
import com.pipilin.common.core.domain.entity.SysUser;
import com.pipilin.common.core.page.TableDataInfo;
import com.pipilin.common.utils.SecurityUtils;
import com.pipilin.system.domain.PSection;
import com.pipilin.system.domain.PScheduling;
import com.pipilin.system.domain.PRegistration;
import com.pipilin.system.service.IPSectionService;
import com.pipilin.system.service.IPSchedulingService;
import com.pipilin.system.service.IPRegistrationService;
import com.pipilin.system.service.ISysUserService;

/**
 * 用户门户公开接口Controller
 * 
 * @author pipilin
 */
@RestController
@RequestMapping("/portal")
public class PortalController extends BaseController
{
    @Autowired
    private IPSectionService sectionService;

    @Autowired
    private IPSchedulingService schedulingService;

    @Autowired
    private IPRegistrationService registrationService;

    @Autowired
    private ISysUserService userService;

    /**
     * 获取科室列表（公开）
     */
    @Anonymous
    @GetMapping("/section/list")
    public TableDataInfo getSectionList(PSection section)
    {
        startPage();
        section.setStatus("0"); // 只查询正常状态
        List<PSection> list = sectionService.selectPSectionList(section);
        return getDataTable(list);
    }

    /**
     * 获取医生列表（公开）
     */
    @Anonymous
    @GetMapping("/doctor/list")
    public TableDataInfo getDoctorList(SysUser user)
    {
        startPage();
        user.setStatus("0"); // 只查询正常状态
        user.setUserType("02"); // 医生类型
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    /**
     * 获取排班列表（公开）
     */
    @Anonymous
    @GetMapping("/scheduling/list")
    public TableDataInfo getSchedulingList(PScheduling scheduling)
    {
        startPage();
        scheduling.setStatus("0"); // 只查询正常状态
        List<PScheduling> list = schedulingService.selectPSchedulingList(scheduling);
        return getDataTable(list);
    }

    /**
     * 创建挂号（需登录）
     */
    @PostMapping("/registration")
    public AjaxResult createRegistration(@RequestBody PRegistration registration)
    {
        registration.setUserId(SecurityUtils.getUserId());
        registration.setStatus("0"); // 待就诊
        return toAjax(registrationService.insertPRegistration(registration));
    }

    /**
     * 获取我的挂号记录（需登录）
     */
    @GetMapping("/registration/my")
    public TableDataInfo getMyRegistrations(PRegistration registration)
    {
        startPage();
        registration.setUserId(SecurityUtils.getUserId());
        List<PRegistration> list = registrationService.selectPRegistrationList(registration);
        return getDataTable(list);
    }

    /**
     * 取消挂号（需登录）
     */
    @PutMapping("/registration/cancel/{id}")
    public AjaxResult cancelRegistration(@PathVariable("id") Long id)
    {
        PRegistration registration = registrationService.selectPRegistrationByRegistrationId(id);
        if (registration == null) {
            return error("挂号记录不存在");
        }
        if (!registration.getUserId().equals(SecurityUtils.getUserId())) {
            return error("无权操作此记录");
        }
        if (!"0".equals(registration.getStatus())) {
            return error("只能取消待就诊的挂号");
        }
        registration.setStatus("2"); // 已取消
        return toAjax(registrationService.updatePRegistration(registration));
    }
}
