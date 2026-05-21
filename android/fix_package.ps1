$srcDir = "D:\aaron\Documents\Mia Bisuteria Deploy\android\app\src"
$files = Get-ChildItem -Path $srcDir -Recurse -Filter "*.kt"
foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw
    $newContent = $content -replace "com\.miabisuteria\.admin", "com.miabisuteri.admin"
    if ($content -ne $newContent) {
        Set-Content -Path $file.FullName -Value $newContent -NoNewline
        Write-Host "Updated: $($file.Name)"
    }
}

# Also fix AndroidManifest.xml
$manifest = "D:\aaron\Documents\Mia Bisuteria Deploy\android\app\src\main\AndroidManifest.xml"
$content = Get-Content $manifest -Raw
$newContent = $content -replace "com\.miabisuteria\.admin", "com.miabisuteri.admin"
Set-Content -Path $manifest -Value $newContent -NoNewline
Write-Host "Updated: AndroidManifest.xml"

Write-Host "Done!"
