# 医院预约挂号系统 - Controller生成脚本

$controllers = @(
    @{Name="PSection"; ChineseName="科室"; IdField="sectionId"; IdType="Long"},
    @{Name="POutpatien"; ChineseName="门诊"; IdField="outpatienId"; IdType="Long"},
    @{Name="PScheduling"; ChineseName="排班"; IdField="schedulingId"; IdType="Long"},
    @{Name="PRegistration"; ChineseName="挂号"; IdField="registrationId"; IdType="Long"},
    @{Name="PPay"; ChineseName="充值记录"; IdField="payId"; IdType="Long"},
    @{Name="PCollect"; ChineseName="收藏"; IdField="collectId"; IdType="Long"},
    @{Name="PEvaluate"; ChineseName="评价"; IdField="evaluateId"; IdType="Long"}
)

foreach ($controller in $controllers) {
    $name = $controller.Name
    $chineseName = $controller.ChineseName
    $idField = $controller.IdField
    $idType = $controller.IdType
    $lowerName = $name.Substring(0,1).ToLower() + $name.Substring(1)
    $urlPath = $name.ToLower()
    
    # Generate Controller
    $controllerPath = "pipilin-admin\src\main\java\com\pipilin\web\controller\system\${name}Controller.java"
    $controllerContent = @"
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
import com.pipilin.system.domain.$name;
import com.pipilin.system.service.I${name}Service;
import com.pipilin.common.utils.poi.ExcelUtil;
import com.pipilin.common.core.page.TableDataInfo;

/**
 * ${chineseName}Controller
 * 
 * @author pipilin
 * @date 2024-12-22
 */
@RestController
@RequestMapping("/system/$lowerName")
public class ${name}Controller extends BaseController
{
    @Autowired
    private I${name}Service ${lowerName}Service;

    /**
     * 查询${chineseName}列表
     */
    @GetMapping("/list")
    public TableDataInfo list($name $lowerName)
    {
        startPage();
        List<$name> list = ${lowerName}Service.select${name}List($lowerName);
        return getDataTable(list);
    }

    /**
     * 导出${chineseName}列表
     */
    @Log(title = "${chineseName}", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, $name $lowerName)
    {
        List<$name> list = ${lowerName}Service.select${name}List($lowerName);
        ExcelUtil<$name> util = new ExcelUtil<$name>($name.class);
        util.exportExcel(response, list, "${chineseName}数据");
    }

    /**
     * 获取${chineseName}详细信息
     */
    @GetMapping(value = "/{$idField}")
    public AjaxResult getInfo(@PathVariable("$idField") $idType $idField)
    {
        return success(${lowerName}Service.select${name}By${idField.Substring(0,1).ToUpper() + $idField.Substring(1)}($idField));
    }

    /**
     * 新增${chineseName}
     */
    @Log(title = "${chineseName}", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody $name $lowerName)
    {
        return toAjax(${lowerName}Service.insert${name}($lowerName));
    }

    /**
     * 修改${chineseName}
     */
    @Log(title = "${chineseName}", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody $name $lowerName)
    {
        return toAjax(${lowerName}Service.update${name}($lowerName));
    }

    /**
     * 删除${chineseName}
     */
    @Log(title = "${chineseName}", businessType = BusinessType.DELETE)
    @DeleteMapping("/{${idField}s}")
    public AjaxResult remove(@PathVariable ${idType}[] ${idField}s)
    {
        return toAjax(${lowerName}Service.delete${name}By${idField.Substring(0,1).ToUpper() + $idField.Substring(1)}s(${idField}s));
    }
}
"@
    Set-Content -Path $controllerPath -Value $controllerContent -Encoding UTF8
    Write-Host "生成 ${name}Controller.java" -ForegroundColor Green
}

Write-Host "`n所有Controller生成完成！" -ForegroundColor Cyan

