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
import com.pipilin.system.domain.PEvaluate;
import com.pipilin.system.service.IPEvaluateService;
import com.pipilin.common.utils.poi.ExcelUtil;
import com.pipilin.common.core.page.TableDataInfo;

/**
 * 评价Controller
 * 
 * @author pipilin
 * @date 2024-12-22
 */
@RestController
@RequestMapping("/system/evaluate")
public class PEvaluateController extends BaseController
{
    @Autowired
    private IPEvaluateService pEvaluateService;

    /**
     * 查询评价列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PEvaluate pEvaluate)
    {
        startPage();
        List<PEvaluate> list = pEvaluateService.selectPEvaluateList(pEvaluate);
        return getDataTable(list);
    }

    /**
     * 导出评价列表
     */
    @Log(title = "评价", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PEvaluate pEvaluate)
    {
        List<PEvaluate> list = pEvaluateService.selectPEvaluateList(pEvaluate);
        ExcelUtil<PEvaluate> util = new ExcelUtil<PEvaluate>(PEvaluate.class);
        util.exportExcel(response, list, "评价数据");
    }

    /**
     * 获取评价详细信息
     */
    @GetMapping(value = "/{evaluateId}")
    public AjaxResult getInfo(@PathVariable("evaluateId") Long evaluateId)
    {
        return success(pEvaluateService.selectPEvaluateByEvaluateId(evaluateId));
    }

    /**
     * 新增评价
     */
    @Log(title = "评价", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PEvaluate pEvaluate)
    {
        return toAjax(pEvaluateService.insertPEvaluate(pEvaluate));
    }

    /**
     * 修改评价
     */
    @Log(title = "评价", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PEvaluate pEvaluate)
    {
        return toAjax(pEvaluateService.updatePEvaluate(pEvaluate));
    }

    /**
     * 删除评价
     */
    @Log(title = "评价", businessType = BusinessType.DELETE)
    @DeleteMapping("/{evaluateIds}")
    public AjaxResult remove(@PathVariable Long[] evaluateIds)
    {
        return toAjax(pEvaluateService.deletePEvaluateByEvaluateIds(evaluateIds));
    }
}

