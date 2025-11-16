package com.social.marketing.pcm.initial;

import com.social.marketing.pcm.entity.Category;
import com.social.marketing.pcm.entity.Product;
import com.social.marketing.pcm.entity.ProductStatus;
import com.social.marketing.pcm.service.CategoryService;
import com.social.marketing.pcm.service.ProductService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.ArrayList; // Thêm import
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
                createCategory("lang-hoa", "Lẵng Hoa", "Các lẵng hoa/giỏ hoa sang trọng."),
                createCategory("qua-tang", "Quà Tặng", "Các lẵng hoa/giỏ hoa sang trọng."),
                createCategory("hoa-cuoi", "Hoa Cưới", "Các lẵng hoa/giỏ hoa sang trọng."),
                createCategory("hoa-de-ban", "Hoa Để Bàn", "Các lẵng hoa/giỏ hoa sang trọng.")
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
                // --- HOA TÌNH YÊU (Love) ---
                createProduct("Bó Hồng Đỏ Thắm", "Bó 12 hoa hồng đỏ Ecuador, biểu tượng tình yêu vĩnh cửu.", BigDecimal.valueOf(750000), ProductStatus.PUBLISHED, "hoa-tinh-yeu", "bo-hoa"),
                createProduct("Trái Tim Chung Đôi", "Lẵng hoa kết hình trái tim từ hồng trắng và baby.", BigDecimal.valueOf(1200000), ProductStatus.PUBLISHED, "hoa-tinh-yeu", "lang-hoa"),
                createProduct("Bó 99 Đóa Hồng", "99 đóa hồng đỏ thắm thay lời muốn nói.", BigDecimal.valueOf(3500000), ProductStatus.PUBLISHED, "hoa-tinh-yeu", "bo-hoa"),
                createProduct("Ngọt Ngào (Hồng Phấn)", "Bó 20 hoa hồng phấn nhẹ nhàng và lãng mạn.", BigDecimal.valueOf(800000), ProductStatus.PUBLISHED, "hoa-tinh-yeu", "bo-hoa"),
                createProduct("Tình Yêu Duy Nhất (1 Đóa)", "Một đóa hồng đỏ trong hộp kính sang trọng.", BigDecimal.valueOf(350000), ProductStatus.PUBLISHED, "hoa-tinh-yeu", "qua-tang"),
                createProduct("Bó Hồng Kem Dâu", "Bó 25 hoa hồng kem mix cùng hoa baby.", BigDecimal.valueOf(950000), ProductStatus.PUBLISHED, "hoa-tinh-yeu", "bo-hoa"),
                createProduct("Chuyện Tình (Hồng và Lan)", "Lẵng hoa hồng và lan hồ điệp tím.", BigDecimal.valueOf(1800000), ProductStatus.DRAFT, "hoa-tinh-yeu", "lang-hoa"),
                createProduct("Bó Hoa Tỏ Tình", "Bó hoa hồng Ohara nhập khẩu.", BigDecimal.valueOf(1300000), ProductStatus.PUBLISHED, "hoa-tinh-yeu", "bo-hoa"),
                createProduct("Hộp Hoa Yêu Thương", "Hộp hoa gỗ cao cấp cắm đầy hoa hồng.", BigDecimal.valueOf(1600000), ProductStatus.PUBLISHED, "hoa-tinh-yeu", "qua-tang"),
                createProduct("Bó Hồng Trắng Tinh Khôi", "Bó 30 đóa hồng trắng tinh khiết.", BigDecimal.valueOf(1100000), ProductStatus.PUBLISHED, "hoa-tinh-yeu", "bo-hoa"),

                // --- HOA KHAI TRƯƠNG (Grand Opening) ---
                createProduct("Lẵng Hoa Phát Tài", "Lẵng hoa 2 tầng (đồng tiền, lan, hồng môn) chúc mừng khai trương.", BigDecimal.valueOf(2500000), ProductStatus.PUBLISHED, "hoa-khai-truong", "lang-hoa"),
                createProduct("Kệ Hoa Khai Trương Hồng Phát", "Kệ hoa lan hồ điệp vàng và hồng môn đỏ.", BigDecimal.valueOf(3800000), ProductStatus.PUBLISHED, "hoa-khai-truong"),
                createProduct("Vạn Sự Như Ý", "Kệ hoa hướng dương và đồng tiền.", BigDecimal.valueOf(1800000), ProductStatus.PUBLISHED, "hoa-khai-truong"),
                createProduct("Tấn Tài Tấn Lộc", "Lẵng hoa lan vũ nữ và hoa ly.", BigDecimal.valueOf(2200000), ProductStatus.PUBLISHED, "hoa-khai-truong", "lang-hoa"),
                createProduct("Thuận Buồm Xuôi Gió", "Kệ hoa lan hồ điệp trắng 3 tầng.", BigDecimal.valueOf(5500000), ProductStatus.PUBLISHED, "hoa-khai-truong"),
                createProduct("Đại Cát Đại Lợi", "Lẵng hoa hồng cam và lan.", BigDecimal.valueOf(1900000), ProductStatus.PUBLISHED, "hoa-khai-truong", "lang-hoa"),
                createProduct("Hưng Thịnh", "Kệ hoa 1 tầng nhỏ gọn, chủ đạo màu đỏ.", BigDecimal.valueOf(1200000), ProductStatus.PUBLISHED, "hoa-khai-truong"),
                createProduct("Phú Quý", "Bình hoa lan hồ điệp 10 cành (vàng).", BigDecimal.valueOf(4500000), ProductStatus.DRAFT, "hoa-khai-truong", "hoa-de-ban"),
                createProduct("Thành Công Vươn Xa", "Kệ hoa cẩm tú cầu và hoa hồng.", BigDecimal.valueOf(2800000), ProductStatus.PUBLISHED, "hoa-khai-truong"),

                // --- HOA SINH NHẬT (Birthday) ---
                createProduct("Bó Hướng Dương Rực Rỡ", "Tặng người bạn thân, luôn hướng về phía mặt trời.", BigDecimal.valueOf(500000), ProductStatus.DRAFT, "hoa-sinh-nhat", "bo-hoa"),
                createProduct("Giỏ Hoa Ngọt Ngào", "Giỏ hoa cẩm tú cầu hồng và hoa hồng.", BigDecimal.valueOf(850000), ProductStatus.PUBLISHED, "hoa-sinh-nhat", "lang-hoa"),
                createProduct("Bó Hoa Tỏa Nắng", "Bó hoa tulip vàng rực rỡ.", BigDecimal.valueOf(700000), ProductStatus.PUBLISHED, "hoa-sinh-nhat", "bo-hoa"),
                createProduct("Lẵng Hoa Tặng Mẹ", "Lẵng hoa cẩm chướng (carnation) hồng.", BigDecimal.valueOf(900000), ProductStatus.PUBLISHED, "hoa-sinh-nhat", "lang-hoa"),
                createProduct("Happy Birthday (Mix)", "Bó hoa mix nhiều loại (hồng, cúc, baby).", BigDecimal.valueOf(650000), ProductStatus.PUBLISHED, "hoa-sinh-nhat", "bo-hoa"),
                createProduct("Bó Hoa Thanh Lịch", "Bó hoa ly trắng và hồng.", BigDecimal.valueOf(720000), ProductStatus.PUBLISHED, "hoa-sinh-nhat", "bo-hoa"),
                createProduct("Giỏ Hoa Nhiệt Đới", "Giỏ hoa cắm hồng môn và lá cọ.", BigDecimal.valueOf(950000), ProductStatus.PUBLISHED, "hoa-sinh-nhat", "lang-hoa"),
                createProduct("Hộp Bất Ngờ", "Hộp hoa nhỏ kèm bánh macaron.", BigDecimal.valueOf(550000), ProductStatus.DRAFT, "hoa-sinh-nhat", "qua-tang"),
                createProduct("Bó Hoa Mẫu Đơn (Peony)", "Bó 5 cành Mẫu Đơn (theo mùa).", BigDecimal.valueOf(1500000), ProductStatus.PUBLISHED, "hoa-sinh-nhat", "bo-hoa"),
                createProduct("Bó Cúc Tana", "Bó cúc tana nhỏ xinh, phong cách Hàn Quốc.", BigDecimal.valueOf(450000), ProductStatus.PUBLISHED, "hoa-sinh-nhat", "bo-hoa"),

                // --- BÓ HOA VÀ GIỎ HOA (Đã được gộp vào các danh mục ở trên) ---
                createProduct("Bó Baby Trắng Nhẹ Nhàng", "Bó hoa baby trắng nhập khẩu Hà Lan.", BigDecimal.valueOf(400000), ProductStatus.PUBLISHED, "bo-hoa"),
                createProduct("Giỏ Hoa Cẩm Tú Cầu", "Giỏ hoa cẩm tú cầu xanh biếc.", BigDecimal.valueOf(650000), ProductStatus.PUBLISHED, "lang-hoa"),
                createProduct("Bó Tulip 10 Bông (Trắng)", "Tulip trắng tinh khôi.", BigDecimal.valueOf(550000), ProductStatus.PUBLISHED, "bo-hoa"),
                createProduct("Bó Cát Tường", "Bó hoa cát tường xanh/hồng.", BigDecimal.valueOf(480000), ProductStatus.PUBLISHED, "bo-hoa"),
                createProduct("Giỏ Hoa Tặng Mẹ (Cẩm chướng)", "Giỏ hoa cẩm chướng và hoa baby.", BigDecimal.valueOf(700000), ProductStatus.PUBLISHED, "lang-hoa", "hoa-sinh-nhat"),
                createProduct("Bó Hoa Tốt Nghiệp", "Bó hoa hướng dương và gấu cử nhân.", BigDecimal.valueOf(600000), ProductStatus.PUBLISHED, "bo-hoa", "qua-tang"),
                createProduct("Giỏ Hoa Cảm Ơn", "Giỏ hoa hồng kem và thạch thảo.", BigDecimal.valueOf(800000), ProductStatus.PUBLISHED, "lang-hoa"),
                createProduct("Bó Hoa Oải Hương (Lavender)", "Bó lavender khô nhập Pháp.", BigDecimal.valueOf(550000), ProductStatus.DRAFT, "bo-hoa", "qua-tang"),
                createProduct("Giỏ Hoa Ly", "Giỏ hoa ly hồng thơm ngát.", BigDecimal.valueOf(750000), ProductStatus.PUBLISHED, "lang-hoa"),
                createProduct("Bó Cúc Mẫu Đơn", "Bó 5 cành cúc mẫu đơn mix.", BigDecimal.valueOf(620000), ProductStatus.PUBLISHED, "bo-hoa"),

                // --- HOA CHIA BUỒN (Sympathy) ---
                createProduct("Kệ Hoa Thành Kính Phân Ưu", "Kệ hoa ly trắng và lan trắng.", BigDecimal.valueOf(1800000), ProductStatus.PUBLISHED, "hoa-chia-buon"),
                createProduct("Vòng Hoa Vô Cùng Thương Tiếc", "Vòng hoa cúc trắng và lan tím.", BigDecimal.valueOf(1500000), ProductStatus.PUBLISHED, "hoa-chia-buon"),
                createProduct("Lẵng Hoa Ly Trắng", "Lẵng hoa ly trắng trang trọng.", BigDecimal.valueOf(1100000), ProductStatus.PUBLISHED, "hoa-chia-buon", "lang-hoa"),
                createProduct("Kệ Hoa Lan Hồ Điệp Trắng", "Kệ lan hồ điệp trắng 5 cành.", BigDecimal.valueOf(2200000), ProductStatus.PUBLISHED, "hoa-chia-buon"),
                createProduct("Vòng Hoa Tin Lành (Thánh Giá)", "Vòng hoa cúc trắng (cho tang lễ đạo Chúa).", BigDecimal.valueOf(1700000), ProductStatus.PUBLISHED, "hoa-chia-buon"),
                createProduct("Kệ Hoa Tông Trắng Xanh", "Kệ hoa hồng trắng và cẩm tú cầu xanh.", BigDecimal.valueOf(1900000), ProductStatus.DRAFT, "hoa-chia-buon"),
                createProduct("Giỏ Hoa Cúc Trắng", "Giỏ hoa cúc trắng để bàn viếng.", BigDecimal.valueOf(800000), ProductStatus.PUBLISHED, "hoa-chia-buon", "lang-hoa"),
                createProduct("Kệ Hoa Hiện Đại", "Kệ hoa tông trắng, cắm kiểu hiện đại.", BigDecimal.valueOf(2300000), ProductStatus.PUBLISHED, "hoa-chia-buon"),

                // --- HOA CƯỚI (Wedding) ---
                createProduct("Bó Hoa Cô Dâu (Hồng Trắng)", "Bó hoa hồng trắng và baby.", BigDecimal.valueOf(1200000), ProductStatus.PUBLISHED, "hoa-cuoi", "bo-hoa"),
                createProduct("Bó Hoa Calla Lily", "Bó hoa rum (Calla Lily) trắng 10 cành.", BigDecimal.valueOf(1500000), ProductStatus.PUBLISHED, "hoa-cuoi", "bo-hoa"),
                createProduct("Bó Hoa Mẫu Đơn (Peony)", "Bó hoa mẫu đơn hồng cho cô dâu.", BigDecimal.valueOf(2500000), ProductStatus.PUBLISHED, "hoa-cuoi", "bo-hoa"),
                createProduct("Hoa Cài Áo Chú Rể", "Hoa hồng trắng nhỏ cài áo.", BigDecimal.valueOf(150000), ProductStatus.PUBLISHED, "hoa-cuoi"),
                createProduct("Bó Hoa Cưới (Tông Cam Đất)", "Bó hoa hồng cam đất (hot trend).", BigDecimal.valueOf(1300000), ProductStatus.DRAFT, "hoa-cuoi", "bo-hoa"),
                createProduct("Hoa Để Bàn Tiệc (Thấp)", "Bình hoa nhỏ để bàn tiệc gallery.", BigDecimal.valueOf(450000), ProductStatus.PUBLISHED, "hoa-cuoi", "hoa-de-ban"),
                createProduct("Hoa Cổng Cưới (Tươi)", "Cổng hoa tươi (Hồng, Lan, Cẩm tú cầu).", BigDecimal.valueOf(8000000), ProductStatus.PUBLISHED, "hoa-cuoi"),
                createProduct("Bó Hoa Cầm Tay (Hồng Sen)", "Bó hoa hồng sen và cúc tana.", BigDecimal.valueOf(900000), ProductStatus.PUBLISHED, "hoa-cuoi", "bo-hoa"),

                // --- HOA ĐỂ BÀN (Desk Flowers) ---
                createProduct("Bình Lan Hồ Điệp Vàng", "Bình 3 cành lan hồ điệp vàng.", BigDecimal.valueOf(1300000), ProductStatus.PUBLISHED, "hoa-de-ban"),
                createProduct("Chậu Sen Đá Mix", "Chậu xi măng sen đá (nhiều loại).", BigDecimal.valueOf(350000), ProductStatus.PUBLISHED, "hoa-de-ban", "qua-tang"),
                createProduct("Bình Hoa Tulip (Giả)", "Bình tulip silicon cao cấp (như thật).", BigDecimal.valueOf(400000), ProductStatus.PUBLISHED, "hoa-de-ban"),
                createProduct("Chậu Kim Tiền", "Chậu cây kim tiền (phong thủy).", BigDecimal.valueOf(450000), ProductStatus.PUBLISHED, "hoa-de-ban"),
                createProduct("Bình Cẩm Tú Cầu Nhỏ", "Bình cẩm tú cầu 1 bông để bàn.", BigDecimal.valueOf(250000), ProductStatus.DRAFT, "hoa-de-ban"),
                createProduct("Chậu Sống Đời", "Chậu hoa sống đời (nhiều màu).", BigDecimal.valueOf(150000), ProductStatus.PUBLISHED, "hoa-de-ban"),
                createProduct("Bình Hồng Nhập Khẩu", "Bình 5 bông hồng Ecuador.", BigDecimal.valueOf(600000), ProductStatus.PUBLISHED, "hoa-de-ban"),

                // --- SẢN PHẨM KHÁC (Add-ons) ---
                createProduct("Socola Ferrero Rocher (16 viên)", "Hộp socola Ferrero Rocher 16 viên.", BigDecimal.valueOf(250000), ProductStatus.PUBLISHED, "qua-tang"),
                createProduct("Gấu Bông Nhỏ", "Gấu bông teddy nhỏ (kèm hoa).", BigDecimal.valueOf(180000), ProductStatus.PUBLISHED, "qua-tang"),
                createProduct("Thiệp Chúc Mừng (Thủ công)", "Thiệp chúc mừng handmade.", BigDecimal.valueOf(50000), ProductStatus.PUBLISHED, "qua-tang")
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

    /**
     * Tạo một đối tượng Product mới.
     *
     * @param name           Tên sản phẩm
     * @param description    Mô tả
     * @param price          Giá gốc
     * @param status         Trạng thái
     * @param categorySlugs  (Varargs) Một hoặc nhiều slug của danh mục mà sản phẩm này thuộc về
     * @return Product
     */
    private Product createProduct(String name, String description, BigDecimal price, ProductStatus status, String... categorySlugs) {
        List<Category> categories = new ArrayList<>();
        if (categorySlugs != null) {
            for (String slug : categorySlugs) {
                Category category = categoryService.findBySlug(slug);
                if (category != null) {
                    categories.add(category);
                } else {
                    // Cảnh báo nếu không tìm thấy slug danh mục trong dữ liệu mẫu
                    System.out.println("WARN: Sample data category slug not found: " + slug);
                }
            }
        }

        Product product = new Product();
        product.setSku(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        product.setName(name);

        product.setSlug(generateSlug(name));

        product.setDescription(description);
        product.setPrice(price.add(BigDecimal.TEN)); // Giả sử giá bán cao hơn giá gốc 10
        product.setOriginPrice(price);
        product.setCategories(categories); // Cập nhật: set list of categories
        product.setStatus(status);
        return product;
    }
}