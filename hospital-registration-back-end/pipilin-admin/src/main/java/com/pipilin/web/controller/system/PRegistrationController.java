package com.pipilin.web.controller.system;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pipilin.common.annotation.Log;
import com.pipilin.common.core.controller.BaseController;
import com.pipilin.common.core.domain.AjaxResult;
import com.pipilin.common.enums.BusinessType;
import com.pipilin.system.domain.PRegistration;
import com.pipilin.system.service.IPRegistrationService;
import com.pipilin.common.utils.poi.ExcelUtil;
import com.pipilin.common.core.page.TableDataInfo;

/**
 * 挂号Controller
 * 
 * @author pipilin
 * @date 2024-12-22
 */
@RestController
@RequestMapping("/system/registration")
public class PRegistrationController extends BaseController
{
    @Autowired
    private IPRegistrationService pRegistrationService;

    /**
     * 查询挂号列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PRegistration pRegistration)
    {
        startPage();
        List<PRegistration> list = pRegistrationService.selectPRegistrationList(pRegistration);
        return getDataTable(list);
    }

    /**
     * 导出挂号列表
     */
    @Log(title = "挂号", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PRegistration pRegistration)
    {
        List<PRegistration> list = pRegistrationService.selectPRegistrationList(pRegistration);
        ExcelUtil<PRegistration> util = new ExcelUtil<PRegistration>(PRegistration.class);
        util.exportExcel(response, list, "挂号数据");
    }

    /**
     * 获取挂号详细信息
     */
    @GetMapping(value = "/{registrationId}")
    public AjaxResult getInfo(@PathVariable("registrationId") Long registrationId)
    {
        return success(pRegistrationService.selectPRegistrationByRegistrationId(registrationId));
    }

    /**
     * 新增挂号
     */
    @Log(title = "挂号", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PRegistration pRegistration)
    {
        return toAjax(pRegistrationService.insertPRegistration(pRegistration));
    }

    /**
     * 修改挂号
     */
    @Log(title = "挂号", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PRegistration pRegistration)
    {
        return toAjax(pRegistrationService.updatePRegistration(pRegistration));
    }

    /**
     * 删除挂号
     */
    @Log(title = "挂号", businessType = BusinessType.DELETE)
    @DeleteMapping("/{registrationIds}")
    public AjaxResult remove(@PathVariable Long[] registrationIds)
    {
        return toAjax(pRegistrationService.deletePRegistrationByRegistrationIds(registrationIds));
    }
}

