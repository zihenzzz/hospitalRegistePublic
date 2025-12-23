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
import com.pipilin.system.domain.PPay;
import com.pipilin.system.service.IPPayService;
import com.pipilin.common.utils.poi.ExcelUtil;
import com.pipilin.common.core.page.TableDataInfo;

/**
 * 充值记录Controller
 * 
 * @author pipilin
 * @date 2024-12-22
 */
@RestController
@RequestMapping("/system/pay")
public class PPayController extends BaseController
{
    @Autowired
    private IPPayService pPayService;

    /**
     * 查询充值记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PPay pPay)
    {
        startPage();
        List<PPay> list = pPayService.selectPPayList(pPay);
        return getDataTable(list);
    }

    /**
     * 导出充值记录列表
     */
    @Log(title = "充值记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PPay pPay)
    {
        List<PPay> list = pPayService.selectPPayList(pPay);
        ExcelUtil<PPay> util = new ExcelUtil<PPay>(PPay.class);
        util.exportExcel(response, list, "充值记录数据");
    }

    /**
     * 获取充值记录详细信息
     */
    @GetMapping(value = "/{payId}")
    public AjaxResult getInfo(@PathVariable("payId") Long payId)
    {
        return success(pPayService.selectPPayByPayId(payId));
    }

    /**
     * 新增充值记录
     */
    @Log(title = "充值记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PPay pPay)
    {
        return toAjax(pPayService.insertPPay(pPay));
    }

    /**
     * 修改充值记录
     */
    @Log(title = "充值记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PPay pPay)
    {
        return toAjax(pPayService.updatePPay(pPay));
    }

    /**
     * 删除充值记录
     */
    @Log(title = "充值记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{payIds}")
    public AjaxResult remove(@PathVariable Long[] payIds)
    {
        return toAjax(pPayService.deletePPayByPayIds(payIds));
    }
}


