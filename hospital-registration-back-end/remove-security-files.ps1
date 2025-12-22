# Remove all Spring Security related files

Write-Host "Removing Spring Security related files..." -ForegroundColor Green

# List of files to delete
$filesToDelete = @(
    "pipilin-framework\src\main\java\com\pipilin\framework\web\service\SysLoginService.java",
    "pipilin-framework\src\main\java\com\pipilin\framework\web\service\SysRegisterService.java",
    "pipilin-framework\src\main\java\com\pipilin\framework\web\service\SysPermissionService.java",
    "pipilin-framework\src\main\java\com\pipilin\framework\web\service\PermissionService.java",
    "pipilin-framework\src\main\java\com\pipilin\framework\web\service\TokenService.java",
    "pipilin-framework\src\main\java\com\pipilin\framework\web\service\UserDetailsServiceImpl.java",
    "pipilin-common\src\main\java\com\pipilin\common\utils\SecurityUtils.java"
)

# Delete security related directories if they exist
$dirsToDelete = @(
    "pipilin-framework\src\main\java\com\pipilin\framework\security",
    "pipilin-common\src\main\java\com\pipilin\common\core\domain\model"
)

$deletedCount = 0

foreach ($file in $filesToDelete) {
    if (Test-Path $file) {
        Remove-Item $file -Force
        Write-Host "  Deleted: $file" -ForegroundColor Yellow
        $deletedCount++
    }
}

foreach ($dir in $dirsToDelete) {
    if (Test-Path $dir) {
        Remove-Item $dir -Recurse -Force
        Write-Host "  Deleted directory: $dir" -ForegroundColor Yellow
        $deletedCount++
    }
}

Write-Host "`nDeleted $deletedCount items" -ForegroundColor Cyan


