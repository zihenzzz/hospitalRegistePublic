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
import com.pipilin.system.domain.POutpatien;
import com.pipilin.system.service.IPOutpatienService;
import com.pipilin.common.utils.poi.ExcelUtil;
import com.pipilin.common.core.page.TableDataInfo;

/**
 * 门诊Controller
 * 
 * @author pipilin
 * @date 2024-12-22
 */
@RestController
@RequestMapping("/system/outpatien")
public class POutpatienController extends BaseController
{
    @Autowired
    private IPOutpatienService pOutpatienService;

    /**
     * 查询门诊列表
     */
    @GetMapping("/list")
    public TableDataInfo list(POutpatien pOutpatien)
    {
        startPage();
        List<POutpatien> list = pOutpatienService.selectPOutpatienList(pOutpatien);
        return getDataTable(list);
    }

    /**
     * 导出门诊列表
     */
    @Log(title = "门诊", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, POutpatien pOutpatien)
    {
        List<POutpatien> list = pOutpatienService.selectPOutpatienList(pOutpatien);
        ExcelUtil<POutpatien> util = new ExcelUtil<POutpatien>(POutpatien.class);
        util.exportExcel(response, list, "门诊数据");
    }

    /**
     * 获取门诊详细信息
     */
    @GetMapping(value = "/{outpatienId}")
    public AjaxResult getInfo(@PathVariable("outpatienId") Long outpatienId)
    {
        return success(pOutpatienService.selectPOutpatienByOutpatienId(outpatienId));
    }

    /**
     * 新增门诊
     */
    @Log(title = "门诊", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody POutpatien pOutpatien)
    {
        return toAjax(pOutpatienService.insertPOutpatien(pOutpatien));
    }

    /**
     * 修改门诊
     */
    @Log(title = "门诊", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody POutpatien pOutpatien)
    {
        return toAjax(pOutpatienService.updatePOutpatien(pOutpatien));
    }

    /**
     * 删除门诊
     */
    @Log(title = "门诊", businessType = BusinessType.DELETE)
    @DeleteMapping("/{outpatienIds}")
    public AjaxResult remove(@PathVariable Long[] outpatienIds)
    {
        return toAjax(pOutpatienService.deletePOutpatienByOutpatienIds(outpatienIds));
    }
}

