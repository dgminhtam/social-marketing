$headers = "name,slug,description,active,parentSlug"
$parents = @()
for ($i = 1; $i -le 10; $i++) {
    $parents += [PSCustomObject]@{
        name = "Danh mục cha $i"
        slug = "danh-muc-cha-$i"
        description = "Mô tả cho danh mục cha $i"
        active = "true"
        parentSlug = ""
    }
}

$children = @()
for ($i = 1; $i -le 90; $i++) {
    $parent = $parents | Get-Random
    $children += [PSCustomObject]@{
        name = "Danh mục con $i"
        slug = "danh-muc-con-$i"
        description = "Mô tả cho danh mục con $i"
        active = "true"
        parentSlug = $parent.slug
    }
}

$allCategories = $parents + $children

$csvContent = $headers
foreach ($cat in $allCategories) {
    $csvContent += "`n$($cat.name),$($cat.slug),$($cat.description),$($cat.active),$($cat.parentSlug)"
}

$csvContent | Out-File -FilePath "categories_sample_100.csv" -Encoding UTF8
Write-Host "Generated categories_sample_100.csv"
