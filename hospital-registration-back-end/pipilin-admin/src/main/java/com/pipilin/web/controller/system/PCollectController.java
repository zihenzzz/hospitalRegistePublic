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
import com.pipilin.system.domain.PCollect;
import com.pipilin.system.service.IPCollectService;
import com.pipilin.common.utils.poi.ExcelUtil;
import com.pipilin.common.core.page.TableDataInfo;

/**
 * 收藏Controller
 * 
 * @author pipilin
 * @date 2024-12-22
 */
@RestController
@RequestMapping("/system/collect")
public class PCollectController extends BaseController
{
    @Autowired
    private IPCollectService pCollectService;

    /**
     * 查询收藏列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PCollect pCollect)
    {
        startPage();
        List<PCollect> list = pCollectService.selectPCollectList(pCollect);
        return getDataTable(list);
    }

    /**
     * 导出收藏列表
     */
    @Log(title = "收藏", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PCollect pCollect)
    {
        List<PCollect> list = pCollectService.selectPCollectList(pCollect);
        ExcelUtil<PCollect> util = new ExcelUtil<PCollect>(PCollect.class);
        util.exportExcel(response, list, "收藏数据");
    }

    /**
     * 获取收藏详细信息
     */
    @GetMapping(value = "/{collectId}")
    public AjaxResult getInfo(@PathVariable("collectId") Long collectId)
    {
        return success(pCollectService.selectPCollectByCollectId(collectId));
    }

    /**
     * 新增收藏
     */
    @Log(title = "收藏", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PCollect pCollect)
    {
        return toAjax(pCollectService.insertPCollect(pCollect));
    }

    /**
     * 修改收藏
     */
    @Log(title = "收藏", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PCollect pCollect)
    {
        return toAjax(pCollectService.updatePCollect(pCollect));
    }

    /**
     * 删除收藏
     */
    @Log(title = "收藏", businessType = BusinessType.DELETE)
    @DeleteMapping("/{collectIds}")
    public AjaxResult remove(@PathVariable Long[] collectIds)
    {
        return toAjax(pCollectService.deletePCollectByCollectIds(collectIds));
    }
}

