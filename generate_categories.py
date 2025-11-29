import csv
import random

headers = ["name", "slug", "description", "active", "parentSlug"]

# Generate some parent categories
parents = []
for i in range(1, 11):
    parents.append({
        "name": f"Danh mục cha {i}",
        "slug": f"danh-muc-cha-{i}",
        "description": f"Mô tả cho danh mục cha {i}",
        "active": "true",
        "parentSlug": ""
    })

# Generate children categories
children = []
for i in range(1, 91):
    parent = random.choice(parents)
    children.append({
        "name": f"Danh mục con {i}",
        "slug": f"danh-muc-con-{i}",
        "description": f"Mô tả cho danh mục con {i}",
        "active": "true",
        "parentSlug": parent["slug"]
    })

all_categories = parents + children

with open('categories_sample_100.csv', 'w', newline='', encoding='utf-8') as f:
    writer = csv.writer(f)
    writer.writerow(headers)
    
    for cat in all_categories:
        writer.writerow([cat["name"], cat["slug"], cat["description"], cat["active"], cat["parentSlug"]])

print("Generated categories_sample_100.csv")
