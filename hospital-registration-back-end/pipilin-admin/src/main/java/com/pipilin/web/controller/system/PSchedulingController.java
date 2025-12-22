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
import com.pipilin.system.domain.PScheduling;
import com.pipilin.system.service.IPSchedulingService;
import com.pipilin.common.utils.poi.ExcelUtil;
import com.pipilin.common.core.page.TableDataInfo;

/**
 * 排班Controller
 * 
 * @author pipilin
 * @date 2024-12-22
 */
@RestController
@RequestMapping("/system/scheduling")
public class PSchedulingController extends BaseController
{
    @Autowired
    private IPSchedulingService pSchedulingService;

    /**
     * 查询排班列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PScheduling pScheduling)
    {
        startPage();
        List<PScheduling> list = pSchedulingService.selectPSchedulingList(pScheduling);
        return getDataTable(list);
    }

    /**
     * 导出排班列表
     */
    @Log(title = "排班", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PScheduling pScheduling)
    {
        List<PScheduling> list = pSchedulingService.selectPSchedulingList(pScheduling);
        ExcelUtil<PScheduling> util = new ExcelUtil<PScheduling>(PScheduling.class);
        util.exportExcel(response, list, "排班数据");
    }

    /**
     * 获取排班详细信息
     */
    @GetMapping(value = "/{schedulingId}")
    public AjaxResult getInfo(@PathVariable("schedulingId") Long schedulingId)
    {
        return success(pSchedulingService.selectPSchedulingBySchedulingId(schedulingId));
    }

    /**
     * 新增排班
     */
    @Log(title = "排班", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PScheduling pScheduling)
    {
        return toAjax(pSchedulingService.insertPScheduling(pScheduling));
    }

    /**
     * 修改排班
     */
    @Log(title = "排班", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PScheduling pScheduling)
    {
        return toAjax(pSchedulingService.updatePScheduling(pScheduling));
    }

    /**
     * 删除排班
     */
    @Log(title = "排班", businessType = BusinessType.DELETE)
    @DeleteMapping("/{schedulingIds}")
    public AjaxResult remove(@PathVariable Long[] schedulingIds)
    {
        return toAjax(pSchedulingService.deletePSchedulingBySchedulingIds(schedulingIds));
    }
}

