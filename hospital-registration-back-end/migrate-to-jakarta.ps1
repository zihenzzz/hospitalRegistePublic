# 批量替换 javax 为 jakarta 的 PowerShell 脚本

Write-Host "开始迁移 javax.* 到 jakarta.*..." -ForegroundColor Green

# 定义需要替换的包映射
$replacements = @{
    'import javax.validation' = 'import jakarta.validation'
    'import javax.servlet' = 'import jakarta.servlet'
}

# 获取所有 Java 文件
$javaFiles = Get-ChildItem -Path "." -Filter "*.java" -Recurse

$totalFiles = 0
$modifiedFiles = 0

foreach ($file in $javaFiles) {
    $content = Get-Content $file.FullName -Raw -Encoding UTF8
    $originalContent = $content
    $modified = $false
    
    foreach ($key in $replacements.Keys) {
        if ($content -match [regex]::Escape($key)) {
            $content = $content -replace [regex]::Escape($key), $replacements[$key]
            $modified = $true
        }
    }
    
    if ($modified) {
        Set-Content -Path $file.FullName -Value $content -Encoding UTF8 -NoNewline
        Write-Host "  已更新: $($file.FullName)" -ForegroundColor Yellow
        $modifiedFiles++
    }
    
    $totalFiles++
}

Write-Host "`n迁移完成!" -ForegroundColor Green
Write-Host "  总文件数: $totalFiles" -ForegroundColor Cyan
Write-Host "  修改文件数: $modifiedFiles" -ForegroundColor Cyan
Write-Host "`n请运行 'mvn clean install -DskipTests' 重新编译项目" -ForegroundColor Yellow

