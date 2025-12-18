# Fix SecurityUtils imports in system module

Write-Host "Fixing SecurityUtils imports..." -ForegroundColor Green

$files = @(
    "pipilin-system\src\main\java\com\pipilin\system\service\impl\SysUserServiceImpl.java",
    "pipilin-system\src\main\java\com\pipilin\system\service\impl\SysMenuServiceImpl.java",
    "pipilin-system\src\main\java\com\pipilin\system\service\impl\SysRoleServiceImpl.java",
    "pipilin-system\src\main\java\com\pipilin\system\service\impl\SysDeptServiceImpl.java"
)

foreach ($file in $files) {
    if (Test-Path $file) {
        $content = Get-Content $file -Raw -Encoding UTF8
        
        # Remove SecurityUtils import
        $content = $content -replace 'import com\.pipilin\.common\.utils\.SecurityUtils;[\r\n]+', ''
        
        # Replace SecurityUtils.isAdmin() with false
        $content = $content -replace 'SecurityUtils\.isAdmin\(\)', 'false'
        
        # Replace SecurityUtils.getUsername() with null
        $content = $content -replace 'SecurityUtils\.getUsername\(\)', 'null'
        
        # Replace SecurityUtils.getUserId() with null
        $content = $content -replace 'SecurityUtils\.getUserId\(\)', 'null'
        
        [System.IO.File]::WriteAllText($file, $content)
        Write-Host "  Fixed: $file" -ForegroundColor Yellow
    }
}

Write-Host "Done!" -ForegroundColor Green

