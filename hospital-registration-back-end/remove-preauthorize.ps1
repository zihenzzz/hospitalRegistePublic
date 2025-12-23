# Remove all @PreAuthorize annotations and imports

Write-Host "Removing @PreAuthorize annotations..." -ForegroundColor Green

# Find all Java files in the project
$files = Get-ChildItem -Recurse -Filter "*.java" | Where-Object { $_.FullName -notmatch "\\target\\" }

$count = 0

foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw -Encoding UTF8
    $original = $content
    
    # Remove PreAuthorize import
    $content = $content -replace 'import org\.springframework\.security\.access\.prepost\.PreAuthorize;[\r\n]+', ''
    
    # Remove @PreAuthorize annotations (single line)
    $content = $content -replace '\s*@PreAuthorize\([^\)]*\)[\r\n]+', "`r`n"
    
    # Remove @PreAuthorize annotations (multi-line)
    $content = $content -replace '\s*@PreAuthorize\([^)]*?\)[^)]*?\)', ''
    
    if ($content -ne $original) {
        [System.IO.File]::WriteAllText($file.FullName, $content)
        Write-Host "  Fixed: $($file.FullName)" -ForegroundColor Yellow
        $count++
    }
}

Write-Host "`nProcessed $count files" -ForegroundColor Cyan



