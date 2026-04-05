import os
import glob
import re

out = []
src_dir = 'd:/Development/Project/social-marketing/src/main/java'

for file_path in glob.glob(src_dir + '/**/*.java', recursive=True):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # Find public class
    class_match = re.search(r'public\s+(?:abstract\s+)?class\s+(\w+)(.*?)\{', content, re.DOTALL)
    if class_match:
        class_name = class_match.group(1)
        rest_of_decl = class_match.group(2)
        
        is_service_name = class_name.endswith('Service')
        
        if is_service_name:
            if 'implements' not in rest_of_decl:
                # Store relative to src/main/java
                rel_path = os.path.relpath(file_path, src_dir)
                out.append(f"{class_name} ({rel_path})")

with open('services_without_interfaces.txt', 'w', encoding='utf-8') as fout:
    fout.write("Services Without Interfaces:\n")
    for s in sorted(list(set(out))):
        fout.write("- " + s + "\n")
