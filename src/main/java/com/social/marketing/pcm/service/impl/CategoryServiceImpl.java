package com.social.marketing.pcm.service.impl;

import com.social.marketing.exception.BadRequestException;
import com.social.marketing.exception.NotFoundException;
import com.social.marketing.media.entity.Media;
import com.social.marketing.media.service.MediaService;
import com.social.marketing.pcm.entity.Category;
import com.social.marketing.pcm.model.request.CreateCategoryRequest;
import com.social.marketing.pcm.model.request.UpdateCategoryRequest;
import com.social.marketing.pcm.model.response.CategoryResponse;
import com.social.marketing.pcm.repository.CategoryRepository;
import com.social.marketing.pcm.repository.ProductRepository;
import com.social.marketing.pcm.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final MediaService mediaService;

    @Override
    public Category getCategoryById(Long id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isEmpty()) {
            throw new NotFoundException("Category not found.");
        }
        return categoryOpt.get();
    }

    @Override
    public List<Category> getCategoryByIds(List<Long> ids) {
        return categoryRepository.findAllById(ids);
    }

    @Override
    public void saveAll(List<Category> categories) {
        categoryRepository.saveAll(categories);
    }

    @Override
    public Page<CategoryResponse> getCategories(Specification<Category> specification, Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(specification, pageable);
        List<CategoryResponse> categoryResponses = categories.getContent().stream().map(this::convert).toList();
        return new PageImpl<>(categoryResponses, categories.getPageable(), categories.getTotalElements());
    }

    @Override
    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public List<CategoryResponse> getCategoryTree() {
        Specification<Category> specification = (root, query, criteriaBuilder)
                -> criteriaBuilder.isNull(root.get(Category.Fields.parent));
        List<Category> categories = categoryRepository.findAll(specification);
        return categories.stream().map(this::convert).toList();
    }

    @Override
    public CategoryResponse getCategory(Long id) {
        Category category = getCategoryById(id);
        return convert(category);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        Category category = getCategoryById(id);
        category.setName(request.name());
        category.setSlug(request.slug());
        category.setActive(request.active());
        category.setDescription(request.description());
        if (request.parentId() != null) {
            Category parent = getCategoryById(request.parentId());
            validateParent(category, parent);
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        if (request.imageId() != null) {
            Media image = mediaService.get(request.imageId());
            category.setImage(image);
        }
        return convert(category);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());
        category.setSlug(request.slug());
        category.setActive(request.active());
        category.setDescription(request.description());
        if (request.parentId() != null) {
            Category parent = getCategoryById(request.parentId());
            validateParent(category, parent);
            category.setParent(parent);
        }

        if (request.imageId() != null) {
            Media image = mediaService.get(request.imageId());
            category.setImage(image);
        }
        categoryRepository.save(category);
        return convert(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        if (hasProduct(category)) {
            throw new BadRequestException(
                    "Không thể xóa danh mục '" + category.getName() + "' vì vẫn còn sản phẩm được gán."
            );
        }
        List<Category> children = category.getChildren();
        if (children != null && !children.isEmpty()) {
            for (Category child : children) {
                child.setParent(null);
            }
            categoryRepository.saveAll(children);
        }
        categoryRepository.delete(category);
    }

    private boolean hasProduct(Category category) {
        return productRepository.existsByCategories(Collections.singletonList(category));
    }

    @Override
    public CategoryResponse convert(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        categoryResponse.setSlug(category.getSlug());
        categoryResponse.setDescription(category.getDescription());
        categoryResponse.setActive(category.isActive());
        categoryResponse.setImage(mediaService.convert(category.getImage()));
        categoryResponse.setParentId(category.getParent() == null ? null : category.getParent().getId());
        List<Category> children = category.getChildren();
        categoryResponse.setChildren(children.stream().map(this::convert).toList());
        categoryResponse.setCreatedDate(category.getCreatedDate().format(DateTimeFormatter.ISO_DATE_TIME));
        categoryResponse.setLastModifiedDate(category.getLastModifiedDate().format(DateTimeFormatter.ISO_DATE_TIME));
        return categoryResponse;
    }

    @Override
    public Category findBySlug(String slug) {
        Optional<Category> categoryOptional = categoryRepository.findBySlug(slug);
        if (categoryOptional.isEmpty()) {
            throw new NotFoundException("Category not found.");
        }
        return categoryOptional.get();
    }

    private void validateParent(Category category, Category parent) {
        if (category.getId() != null && category.getId().equals(parent.getId())) {
            throw new IllegalArgumentException("Category cannot be its own parent.");
        }

        Category ancestor = parent.getParent();
        while (ancestor != null) {
            if (category.getId() != null && category.getId().equals(ancestor.getId())) {
                throw new IllegalArgumentException("Circular parent relationship detected.");
            }
            ancestor = ancestor.getParent();
        }
    }

    @Override
    @Transactional
    public void importCategories(MultipartFile file) {
        logger.info("Starting category import from file: {}", file.getOriginalFilename());
        try {
            InputStream is = file.getInputStream();
            PushbackInputStream pis = new PushbackInputStream(is, 3);
            byte[] bom = new byte[3];
            int n = pis.read(bom);
            if (n == 3 && bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBB && bom[2] == (byte) 0xBF) {
                // BOM skipped
                logger.debug("BOM detected and skipped");
            } else {
                if (n > 0) pis.unread(bom, 0, n);
            }

            try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(pis, StandardCharsets.UTF_8));
                 CSVParser csvParser = new CSVParser(fileReader,
                         CSVFormat.DEFAULT.builder()
                                 .setHeader()
                                 .setSkipHeaderRecord(true)
                                 .setIgnoreHeaderCase(true)
                                 .setTrim(true)
                                 .build())) {

                List<Category> categories = new ArrayList<>();
                Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            // First pass: Create categories without parents
            Map<String, Category> slugToCategoryMap = new HashMap<>();
            List<CSVRecord> recordList = new ArrayList<>();
            csvRecords.forEach(recordList::add);

            logger.info("Processing {} category records", recordList.size());

            for (CSVRecord csvRecord : recordList) {
                Category category = new Category();
                category.setName(csvRecord.get("name"));
                category.setSlug(csvRecord.get("slug"));
                category.setDescription(csvRecord.get("description"));
                
                String activeStr = csvRecord.get("active");
                category.setActive(Boolean.parseBoolean(activeStr));

                // Check if category already exists by slug to avoid duplicates or update?
                // For simplicity, assuming new import or we can check existence.
                // Let's check if slug exists in DB
                Optional<Category> existing = categoryRepository.findBySlug(category.getSlug());
                if (existing.isPresent()) {
                    // Skip or update? Let's skip for now or maybe update fields
                    Category existingCat = existing.get();
                    existingCat.setName(category.getName());
                    existingCat.setDescription(category.getDescription());
                    existingCat.setActive(category.isActive());
                    slugToCategoryMap.put(category.getSlug(), existingCat);
                    categories.add(existingCat);
                    logger.debug("Updating existing category: {}", category.getSlug());
                } else {
                    slugToCategoryMap.put(category.getSlug(), category);
                    categories.add(category);
                    logger.debug("Creating new category: {}", category.getSlug());
                }
            }
            
            // Save first to get IDs (if needed) or just persist
            categoryRepository.saveAll(categories);
            logger.info("Saved {} categories", categories.size());

            // Second pass: Link parents
            int linkedCount = 0;
            for (CSVRecord csvRecord : recordList) {
                String slug = csvRecord.get("slug");
                String parentSlug = csvRecord.get("parentSlug");

                if (StringUtils.isNotBlank(parentSlug)) {
                    Category child = slugToCategoryMap.get(slug);
                    Category parent = slugToCategoryMap.get(parentSlug);
                    
                    if (parent == null) {
                        // Try to find in DB if not in current import
                        Optional<Category> parentOpt = categoryRepository.findBySlug(parentSlug);
                        if (parentOpt.isPresent()) {
                            parent = parentOpt.get();
                        }
                    }

                    if (parent != null && child != null) {
                         // Validate parent
                        try {
                            validateParent(child, parent);
                            child.setParent(parent);
                            linkedCount++;
                        } catch (IllegalArgumentException e) {
                            logger.warn("Invalid parent for category {}: {}", slug, e.getMessage());
                        }
                    }
                }
            }
            
            categoryRepository.saveAll(categories);
            logger.info("Category import completed. Total: {}, Linked to parents: {}", categories.size(), linkedCount);

        }
        } catch (IOException e) {
            logger.error("Failed to parse CSV file: {}", e.getMessage(), e);
            throw new RuntimeException("Fail to parse CSV file: " + e.getMessage());
        }
    }

}
