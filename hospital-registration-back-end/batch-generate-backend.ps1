# 批量生成后端Mapper和Service实现类

$entities = @(
    @{Name="PScheduling"; IdField="schedulingId"; ChineseName="排班"},
    @{Name="PRegistration"; IdField="registrationId"; ChineseName="挂号"},
    @{Name="POutpatien"; IdField="outpatienId"; ChineseName="门诊"},
    @{Name="PPay"; IdField="payId"; ChineseName="充值记录"},
    @{Name="PCollect"; IdField="collectId"; ChineseName="收藏"},
    @{Name="PEvaluate"; IdField="evaluateId"; ChineseName="评价"}
)

foreach ($entity in $entities) {
    $name = $entity.Name
    $idField = $entity.IdField
    $chineseName = $entity.ChineseName
    $lowerName = $name.Substring(0,1).ToLower() + $name.Substring(1)
    $capitalIdField = $idField.Substring(0,1).ToUpper() + $idField.Substring(1)
    
    # Generate Mapper Interface
    $mapperPath = "pipilin-system\src\main\java\com\pipilin\system\mapper\${name}Mapper.java"
    $mapperContent = "package com.pipilin.system.mapper;

import java.util.List;
import com.pipilin.system.domain.$name;

/**
 * ${chineseName}Mapper接口
 * 
 * @author pipilin
 * @date 2024-12-22
 */
public interface ${name}Mapper 
{
    public $name select${name}By${capitalIdField}(Long $idField);
    public List<$name> select${name}List($name $lowerName);
    public int insert${name}($name $lowerName);
    public int update${name}($name $lowerName);
    public int delete${name}By${capitalIdField}(Long $idField);
    public int delete${name}By${capitalIdField}s(Long[] ${idField}s);
}"
    
    [System.IO.File]::WriteAllText($mapperPath, $mapperContent, [System.Text.Encoding]::UTF8)
    Write-Host "生成 ${name}Mapper.java" -ForegroundColor Green
    
    # Generate Service Implementation
    $serviceImplPath = "pipilin-system\src\main\java\com\pipilin\system\service\impl\${name}ServiceImpl.java"
    $serviceImplContent = "package com.pipilin.system.service.impl;

import java.util.List;
import com.pipilin.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pipilin.system.mapper.${name}Mapper;
import com.pipilin.system.domain.$name;
import com.pipilin.system.service.I${name}Service;

/**
 * ${chineseName}Service业务层处理
 * 
 * @author pipilin
 * @date 2024-12-22
 */
@Service
public class ${name}ServiceImpl implements I${name}Service 
{
    @Autowired
    private ${name}Mapper ${lowerName}Mapper;

    @Override
    public $name select${name}By${capitalIdField}(Long $idField)
    {
        return ${lowerName}Mapper.select${name}By${capitalIdField}($idField);
    }

    @Override
    public List<$name> select${name}List($name $lowerName)
    {
        return ${lowerName}Mapper.select${name}List($lowerName);
    }

    @Override
    public int insert${name}($name $lowerName)
    {
        ${lowerName}.setCreateTime(DateUtils.getNowDate());
        return ${lowerName}Mapper.insert${name}($lowerName);
    }

    @Override
    public int update${name}($name $lowerName)
    {
        ${lowerName}.setUpdateTime(DateUtils.getNowDate());
        return ${lowerName}Mapper.update${name}($lowerName);
    }

    @Override
    public int delete${name}By${capitalIdField}s(Long[] ${idField}s)
    {
        return ${lowerName}Mapper.delete${name}By${capitalIdField}s(${idField}s);
    }

    @Override
    public int delete${name}By${capitalIdField}(Long $idField)
    {
        return ${lowerName}Mapper.delete${name}By${capitalIdField}($idField);
    }
}"
    
    [System.IO.File]::WriteAllText($serviceImplPath, $serviceImplContent, [System.Text.Encoding]::UTF8)
    Write-Host "生成 ${name}ServiceImpl.java" -ForegroundColor Green
}

Write-Host "`n所有Mapper和Service实现类生成完成！" -ForegroundColor Cyan

