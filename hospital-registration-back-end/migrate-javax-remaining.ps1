# Replace remaining javax.annotation with jakarta.annotation

Write-Host "Replacing javax.annotation with jakarta.annotation..." -ForegroundColor Green

$javaFiles = Get-ChildItem -Path "." -Filter "*.java" -Recurse
$fixedCount = 0

foreach ($file in $javaFiles) {
    $content = Get-Content $file.FullName -Raw -Encoding UTF8
    
    if ($content -match 'import javax\.annotation') {
        $content = $content -replace 'import javax\.annotation', 'import jakarta.annotation'
        [System.IO.File]::WriteAllText($file.FullName, $content)
        Write-Host "  Fixed: $($file.Name)" -ForegroundColor Yellow
        $fixedCount++
    }
}

Write-Host "Fixed $fixedCount files" -ForegroundColor Cyan



