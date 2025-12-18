# Script to generate missing mapper and service files

$entities = @(
    @{Name="PSection"; IdField="sectionId"; IdType="Long"},
    @{Name="PScheduling"; IdField="schedulingId"; IdType="Long"},
    @{Name="PRegistration"; IdField="registrationId"; IdType="Long"},
    @{Name="PPay"; IdField="payId"; IdType="Long"},
    @{Name="POutpatien"; IdField="outpatienId"; IdType="Long"},
    @{Name="PEvaluate"; IdField="evaluateId"; IdType="Long"}
)

foreach ($entity in $entities) {
    $name = $entity.Name
    $idField = $entity.IdField
    $idType = $entity.IdType
    $lowerName = $name.Substring(0,1).ToLower() + $name.Substring(1)
    
    # Generate Mapper Interface
    $mapperPath = "pipilin-system\src\main\java\com\pipilin\system\mapper\${name}Mapper.java"
    $mapperContent = @"
package com.pipilin.system.mapper;

import java.util.List;
import com.pipilin.system.domain.$name;

/**
 * ${name}Mapper接口
 * 
 * @author pipilin
 * @date 2024-12-18
 */
public interface ${name}Mapper 
{
    public $name select${name}By${idField.Substring(0,1).ToUpper() + $idField.Substring(1)}($idType $idField);
    public List<$name> select${name}List($name $lowerName);
    public int insert${name}($name $lowerName);
    public int update${name}($name $lowerName);
    public int delete${name}By${idField.Substring(0,1).ToUpper() + $idField.Substring(1)}($idType $idField);
    public int delete${name}By${idField.Substring(0,1).ToUpper() + $idField.Substring(1)}s(${idType}[] ${idField}s);
}
"@
    Set-Content -Path $mapperPath -Value $mapperContent -Encoding UTF8
    
    # Generate Service Interface
    $servicePath = "pipilin-system\src\main\java\com\pipilin\system\service\I${name}Service.java"
    $serviceContent = @"
package com.pipilin.system.service;

import java.util.List;
import com.pipilin.system.domain.$name;

/**
 * ${name}Service接口
 * 
 * @author pipilin
 * @date 2024-12-18
 */
public interface I${name}Service
{
    public List<$name> select${name}List($name $lowerName);
    public int insert${name}($name $lowerName);
    public int update${name}($name $lowerName);
    public int delete${name}ByIds(${idType}[] ids);
    public int delete${name}ById($idType id);
}
"@
    Set-Content -Path $servicePath -Value $serviceContent -Encoding UTF8
    
    # Generate Service Implementation
    $serviceImplPath = "pipilin-system\src\main\java\com\pipilin\system\service\impl\${name}ServiceImpl.java"
    $serviceImplContent = @"
package com.pipilin.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pipilin.system.mapper.${name}Mapper;
import com.pipilin.system.domain.$name;
import com.pipilin.system.service.I${name}Service;

/**
 * ${name}Service业务层处理
 * 
 * @author pipilin
 * @date 2024-12-18
 */
@Service
public class ${name}ServiceImpl implements I${name}Service 
{
    @Autowired
    private ${name}Mapper ${lowerName}Mapper;

    @Override
    public List<$name> select${name}List($name $lowerName)
    {
        return ${lowerName}Mapper.select${name}List($lowerName);
    }

    @Override
    public int insert${name}($name $lowerName)
    {
        return ${lowerName}Mapper.insert${name}($lowerName);
    }

    @Override
    public int update${name}($name $lowerName)
    {
        return ${lowerName}Mapper.update${name}($lowerName);
    }

    @Override
    public int delete${name}ByIds(${idType}[] ids)
    {
        return ${lowerName}Mapper.delete${name}By${idField.Substring(0,1).ToUpper() + $idField.Substring(1)}s(ids);
    }

    @Override
    public int delete${name}ById($idType id)
    {
        return ${lowerName}Mapper.delete${name}By${idField.Substring(0,1).ToUpper() + $idField.Substring(1)}(id);
    }
}
"@
    Set-Content -Path $serviceImplPath -Value $serviceImplContent -Encoding UTF8
    
    Write-Host "Generated files for $name"
}

Write-Host "All files generated successfully!"
"@

