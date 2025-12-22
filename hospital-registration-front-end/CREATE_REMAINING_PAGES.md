# 快速创建剩余Vue页面指南

## 📝 已创建的完整示例
- ✅ **科室管理** (`src/views/system/section/index.vue`) - 完整CRUD示例

## 🔧 快速创建其他页面

基于 `section/index.vue` 模板，只需批量替换以下关键词即可创建其他页面：

### 1. 排班管理 (Scheduling)
复制 `section/index.vue` → `scheduling/index.vue`
```bash
替换内容：
- Section → Scheduling
- section → scheduling
- sectionId → schedulingId
- sectionName → schedulingName
- sectionCode → schedulingCode
- sectionDesc → schedulingDesc
- 科室 → 排班
```

### 2. 挂号管理 (Registration)
```bash
替换内容：
- Section → Registration
- section → registration
- sectionId → registrationId
- 科室 → 挂号
根据实际字段调整表单项
```

### 3. 门诊管理 (Outpatien)
```bash
替换内容：
- Section → Outpatien
- section → outpatien
- sectionId → outpatienId
- 科室 → 门诊
```

### 4. 充值记录 (Pay)
```bash
替换内容：
- Section → Pay
- section → pay
- sectionId → payId
- 科室 → 充值记录
```

### 5. 收藏管理 (Collect)
```bash
替换内容：
- Section → Collect
- section → collect
- sectionId → collectId
- 科室 → 收藏
```

### 6. 评价管理 (Evaluate)
```bash
替换内容：
- Section → Evaluate
- section → evaluate
- sectionId → evaluateId
- 科室 → 评价
```

## 🎯 页面创建步骤

1. **创建目录**：在 `src/views/system/` 下创建对应文件夹
2. **复制模板**：复制 `section/index.vue` 到新文件夹
3. **批量替换**：使用IDE的全局替换功能
4. **调整字段**：根据实际数据表字段调整表单项
5. **测试功能**：启动后端测试增删改查

## ✨ 页面结构说明

每个页面包含：
- 🔍 **搜索区域**：条件查询
- 🔘 **按钮工具栏**：新增、修改、删除、导出
- 📊 **数据表格**：列表展示、分页
- 📝 **弹窗表单**：新增/编辑对话框
- ✅ **权限控制**：v-hasPermi指令

## 📌 注意事项

1. **字段名称**要与数据库表字段一致
2. **权限标识**格式为：`system:模块名:操作`
3. **字典类型**如需使用需在页面中声明：`dicts: ['dict_type']`
4. **日期格式化**使用 `parseTime` 方法

## 🚀 快速完成命令

可以使用PowerShell批量创建：

```powershell
$modules = @("scheduling", "registration", "outpatien", "pay", "collect", "evaluate")
foreach ($module in $modules) {
    New-Item -ItemType Directory -Force -Path "src/views/system/$module"
    # 然后复制并替换文件内容
}
```

