import csv
import random

headers = ["name", "sku", "slug", "description", "price", "originPrice", "status", "categorySlugs"]
statuses = ["ACTIVE", "DRAFT", "INACTIVE"]

with open('products_sample_1000.csv', 'w', newline='', encoding='utf-8') as f:
    writer = csv.writer(f)
    writer.writerow(headers)
    
    for i in range(1, 1001):
        name = f"Sản phẩm mẫu {i}"
        sku = f"SKU-{i:04d}"
        slug = f"san-pham-mau-{i}"
        description = f"Mô tả chi tiết cho sản phẩm mẫu số {i}. Đây là sản phẩm chất lượng cao."
        price = random.randint(100, 1000) * 1000
        origin_price = price + (random.randint(10, 50) * 1000)
        status = random.choice(statuses)
        
        # Assign random categories (1 to 3 categories per product)
        num_cats = random.randint(1, 3)
        product_cats = []
        for _ in range(num_cats):
            # Randomly pick from parent or child slugs generated in generate_categories.py
            # Since we don't want to read the file here for simplicity, we'll replicate the slug logic
            # Parents: danh-muc-cha-1 to 10
            # Children: danh-muc-con-1 to 90
            is_parent = random.choice([True, False])
            if is_parent:
                cat_slug = f"danh-muc-cha-{random.randint(1, 10)}"
            else:
                cat_slug = f"danh-muc-con-{random.randint(1, 90)}"
            product_cats.append(cat_slug)
        
        category_slugs = ",".join(list(set(product_cats))) # Remove duplicates

        writer.writerow([name, sku, slug, description, price, origin_price, status, category_slugs])

print("Generated products_sample_1000.csv")
