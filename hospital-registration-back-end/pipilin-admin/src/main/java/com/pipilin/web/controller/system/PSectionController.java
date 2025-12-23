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
import com.pipilin.system.domain.PSection;
import com.pipilin.system.service.IPSectionService;
import com.pipilin.common.utils.poi.ExcelUtil;
import com.pipilin.common.core.page.TableDataInfo;

/**
 * 科室Controller
 * 
 * @author pipilin
 * @date 2024-12-22
 */
@RestController
@RequestMapping("/system/section")
public class PSectionController extends BaseController
{
    @Autowired
    private IPSectionService pSectionService;

    /**
     * 查询科室列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PSection pSection)
    {
        startPage();
        List<PSection> list = pSectionService.selectPSectionList(pSection);
        return getDataTable(list);
    }

    /**
     * 导出科室列表
     */
    @Log(title = "科室", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PSection pSection)
    {
        List<PSection> list = pSectionService.selectPSectionList(pSection);
        ExcelUtil<PSection> util = new ExcelUtil<PSection>(PSection.class);
        util.exportExcel(response, list, "科室数据");
    }

    /**
     * 获取科室详细信息
     */
    @GetMapping(value = "/{sectionId}")
    public AjaxResult getInfo(@PathVariable("sectionId") Long sectionId)
    {
        return success(pSectionService.selectPSectionBySectionId(sectionId));
    }

    /**
     * 新增科室
     */
    @Log(title = "科室", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PSection pSection)
    {
        return toAjax(pSectionService.insertPSection(pSection));
    }

    /**
     * 修改科室
     */
    @Log(title = "科室", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PSection pSection)
    {
        return toAjax(pSectionService.updatePSection(pSection));
    }

    /**
     * 删除科室
     */
    @Log(title = "科室", businessType = BusinessType.DELETE)
    @DeleteMapping("/{sectionIds}")
    public AjaxResult remove(@PathVariable Long[] sectionIds)
    {
        return toAjax(pSectionService.deletePSectionBySectionIds(sectionIds));
    }
}


