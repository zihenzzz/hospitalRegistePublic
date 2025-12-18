# Remove UTF-8 BOM from all Java files

Write-Host "Removing UTF-8 BOM from Java files..." -ForegroundColor Green

$javaFiles = Get-ChildItem -Path "." -Filter "*.java" -Recurse
$fixedCount = 0

foreach ($file in $javaFiles) {
    $bytes = [System.IO.File]::ReadAllBytes($file.FullName)
    
    if ($bytes.Length -ge 3 -and $bytes[0] -eq 0xEF -and $bytes[1] -eq 0xBB -and $bytes[2] -eq 0xBF) {
        Write-Host "  Fixing: $($file.Name)" -ForegroundColor Yellow
        $newBytes = $bytes[3..($bytes.Length - 1)]
        [System.IO.File]::WriteAllBytes($file.FullName, $newBytes)
        $fixedCount++
    }
}

Write-Host "Fixed $fixedCount files" -ForegroundColor Cyan
