$headers = "name,sku,slug,description,price,originPrice,status,categorySlugs"
$statuses = @("ACTIVE", "DRAFT", "INACTIVE")

$csvContent = $headers

for ($i = 1; $i -le 1000; $i++) {
    $name = "Sản phẩm mẫu $i"
    $sku = "SKU-{0:D4}" -f $i
    $slug = "san-pham-mau-$i"
    $description = "Mô tả chi tiết cho sản phẩm mẫu số $i. Đây là sản phẩm chất lượng cao."
    $price = (Get-Random -Minimum 100 -Maximum 1000) * 1000
    $originPrice = $price + ((Get-Random -Minimum 10 -Maximum 50) * 1000)
    $status = $statuses | Get-Random

    # Assign random categories
    $numCats = Get-Random -Minimum 1 -Maximum 4
    $productCats = @()
    for ($j = 0; $j -lt $numCats; $j++) {
        $isParent = (Get-Random -Minimum 0 -Maximum 2) -eq 1
        if ($isParent) {
            $catSlug = "danh-muc-cha-$(Get-Random -Minimum 1 -Maximum 11)"
        } else {
            $catSlug = "danh-muc-con-$(Get-Random -Minimum 1 -Maximum 91)"
        }
        $productCats += $catSlug
    }
    $categorySlugs = ($productCats | Select-Object -Unique) -join ","

    $csvContent += "`n$name,$sku,$slug,$description,$price,$originPrice,$status,$categorySlugs"
}

$csvContent | Out-File -FilePath "products_sample_1000.csv" -Encoding UTF8
Write-Host "Generated products_sample_1000.csv"
