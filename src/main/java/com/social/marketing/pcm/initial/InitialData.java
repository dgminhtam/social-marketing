package com.social.marketing.pcm.initial;

import com.social.marketing.pcm.entity.Category;
import com.social.marketing.pcm.entity.Product;
import com.social.marketing.pcm.entity.ProductStatus;
import com.social.marketing.pcm.service.CategoryService;
import com.social.marketing.pcm.service.ProductService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

//@Component
public class InitialData {

    @Resource
    private CategoryService categoryService;

    @Resource
    private ProductService productService;

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern EDGESDHASHES = Pattern.compile("(^-|-$)");

    @PostConstruct
    @Transactional
    public void init() {
        initCategory();
        initProduct();
    }

    private void initCategory() {
        List<Category> categories = List.of(
                createCategory("hoa-tinh-yeu", "Hoa Tình Yêu", "Dành tặng cho người bạn yêu thương."),
                createCategory("hoa-khai-truong", "Hoa Khai Trương", "Chúc mừng khởi đầu hồng phát."),
                createCategory("hoa-sinh-nhat", "Hoa Sinh Nhật", "Món quà ý nghĩa trong ngày đặc biệt."),
                createCategory("hoa-chia-buon", "Hoa Chia Buồn", "Gửi lời chia buồn sâu sắc."),
                createCategory("bo-hoa", "Bó Hoa", "Các loại hoa được bó gọn gàng, tinh tế."),
                createCategory("lang-hoa", "Lẵng Hoa", "Các lẵng hoa/giỏ hoa sang trọng.")
        );
        categoryService.saveAll(categories);
    }

    private Category createCategory(String slug, String name, String description) {
        Category category = new Category();
        category.setSlug(slug);
        category.setName(name);
        category.setDescription(description);
        category.setActive(true);
        return category;
    }

    private void initProduct() {
        List<Product> products = List.of(
                createProduct("hoa-tinh-yeu", "Bó Hồng Đỏ Thắm", "Bó 12 hoa hồng đỏ Ecuado, biểu tượng tình yêu vĩnh cửu.", BigDecimal.valueOf(750000), ProductStatus.PUBLISHED),
                createProduct("hoa-tinh-yeu", "Trái Tim Chung Đôi", "Lẵng hoa kết hình trái tim từ hồng trắng và baby.", BigDecimal.valueOf(1200000), ProductStatus.PUBLISHED),
                createProduct("hoa-khai-truong", "Lẵng Hoa Phát Tài", "Lẵng hoa 2 tầng (đồng tiền, lan, hồng môn) chúc mừng khai trương.", BigDecimal.valueOf(2500000), ProductStatus.PUBLISHED),
                createProduct("hoa-sinh-nhat", "Bó Hướng Dương Rực Rỡ", "Tặng người bạn thân, luôn hướng về phía mặt trời.", BigDecimal.valueOf(500000), ProductStatus.DRAFT),
                createProduct("bo-hoa", "Bó Baby Trắng Nhẹ Nhàng", "Bó hoa baby trắng nhập khẩu Hà Lan.", BigDecimal.valueOf(400000), ProductStatus.PUBLISHED),
                createProduct("lang-hoa", "Giỏ Hoa Cẩm Tú Cầu", "Giỏ hoa cẩm tú cầu xanh biếc.", BigDecimal.valueOf(650000), ProductStatus.PUBLISHED)
        );
        productService.saveAll(products);
    }

    private String generateSlug(String name) {
        String nowhitespace = WHITESPACE.matcher(name).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        slug = EDGESDHASHES.matcher(slug).replaceAll("");
        return slug.toLowerCase();
    }

    private Product createProduct(String categorySlug, String name, String description, BigDecimal price, ProductStatus status) {
        Category category = categoryService.findBySlug(categorySlug);

        Product product = new Product();
        product.setSku(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        product.setName(name);

        product.setSlug(generateSlug(name));

        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setStatus(status);
        return product;
    }
}