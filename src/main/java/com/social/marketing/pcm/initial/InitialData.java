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
                createProduct("hoa-tinh-yeu", "Bó Hồng Đỏ Thắm", "Bó 12 hoa hồng đỏ Ecuador, biểu tượng tình yêu vĩnh cửu.", BigDecimal.valueOf(750000), ProductStatus.PUBLISHED),
                createProduct("hoa-tinh-yeu", "Trái Tim Chung Đôi", "Lẵng hoa kết hình trái tim từ hồng trắng và baby.", BigDecimal.valueOf(1200000), ProductStatus.PUBLISHED),
                createProduct("hoa-tinh-yeu", "Bó 99 Đóa Hồng", "99 đóa hồng đỏ thắm thay lời muốn nói.", BigDecimal.valueOf(3500000), ProductStatus.PUBLISHED),
                createProduct("hoa-tinh-yeu", "Ngọt Ngào (Hồng Phấn)", "Bó 20 hoa hồng phấn nhẹ nhàng và lãng mạn.", BigDecimal.valueOf(800000), ProductStatus.PUBLISHED),
                createProduct("hoa-tinh-yeu", "Tình Yêu Duy Nhất (1 Đóa)", "Một đóa hồng đỏ trong hộp kính sang trọng.", BigDecimal.valueOf(350000), ProductStatus.PUBLISHED),
                createProduct("hoa-tinh-yeu", "Bó Hồng Kem Dâu", "Bó 25 hoa hồng kem mix cùng hoa baby.", BigDecimal.valueOf(950000), ProductStatus.PUBLISHED),
                createProduct("hoa-tinh-yeu", "Chuyện Tình (Hồng và Lan)", "Lẵng hoa hồng và lan hồ điệp tím.", BigDecimal.valueOf(1800000), ProductStatus.DRAFT),
                createProduct("hoa-tinh-yeu", "Bó Hoa Tỏ Tình", "Bó hoa hồng Ohara nhập khẩu.", BigDecimal.valueOf(1300000), ProductStatus.PUBLISHED),
                createProduct("hoa-tinh-yeu", "Hộp Hoa Yêu Thương", "Hộp hoa gỗ cao cấp cắm đầy hoa hồng.", BigDecimal.valueOf(1600000), ProductStatus.PUBLISHED),
                createProduct("hoa-tinh-yeu", "Bó Hồng Trắng Tinh Khôi", "Bó 30 đóa hồng trắng tinh khiết.", BigDecimal.valueOf(1100000), ProductStatus.PUBLISHED),

                // --- HOA KHAI TRƯƠNG (Grand Opening) ---
                createProduct("hoa-khai-truong", "Lẵng Hoa Phát Tài", "Lẵng hoa 2 tầng (đồng tiền, lan, hồng môn) chúc mừng khai trương.", BigDecimal.valueOf(2500000), ProductStatus.PUBLISHED),
                createProduct("hoa-khai-truong", "Kệ Hoa Khai Trương Hồng Phát", "Kệ hoa lan hồ điệp vàng và hồng môn đỏ.", BigDecimal.valueOf(3800000), ProductStatus.PUBLISHED),
                createProduct("hoa-khai-truong", "Vạn Sự Như Ý", "Kệ hoa hướng dương và đồng tiền.", BigDecimal.valueOf(1800000), ProductStatus.PUBLISHED),
                createProduct("hoa-khai-truong", "Tấn Tài Tấn Lộc", "Lẵng hoa lan vũ nữ và hoa ly.", BigDecimal.valueOf(2200000), ProductStatus.PUBLISHED),
                createProduct("hoa-khai-truong", "Thuận Buồm Xuôi Gió", "Kệ hoa lan hồ điệp trắng 3 tầng.", BigDecimal.valueOf(5500000), ProductStatus.PUBLISHED),
                createProduct("hoa-khai-truong", "Đại Cát Đại Lợi", "Lẵng hoa hồng cam và lan.", BigDecimal.valueOf(1900000), ProductStatus.PUBLISHED),
                createProduct("hoa-khai-truong", "Hưng Thịnh", "Kệ hoa 1 tầng nhỏ gọn, chủ đạo màu đỏ.", BigDecimal.valueOf(1200000), ProductStatus.PUBLISHED),
                createProduct("hoa-khai-truong", "Phú Quý", "Bình hoa lan hồ điệp 10 cành (vàng).", BigDecimal.valueOf(4500000), ProductStatus.DRAFT),
                createProduct("hoa-khai-truong", "Thành Công Vươn Xa", "Kệ hoa cẩm tú cầu và hoa hồng.", BigDecimal.valueOf(2800000), ProductStatus.PUBLISHED),

                // --- HOA SINH NHẬT (Birthday) ---
                createProduct("hoa-sinh-nhat", "Bó Hướng Dương Rực Rỡ", "Tặng người bạn thân, luôn hướng về phía mặt trời.", BigDecimal.valueOf(500000), ProductStatus.DRAFT),
                createProduct("hoa-sinh-nhat", "Giỏ Hoa Ngọt Ngào", "Giỏ hoa cẩm tú cầu hồng và hoa hồng.", BigDecimal.valueOf(850000), ProductStatus.PUBLISHED),
                createProduct("hoa-sinh-nhat", "Bó Hoa Tỏa Nắng", "Bó hoa tulip vàng rực rỡ.", BigDecimal.valueOf(700000), ProductStatus.PUBLISHED),
                createProduct("hoa-sinh-nhat", "Lẵng Hoa Tặng Mẹ", "Lẵng hoa cẩm chướng (carnation) hồng.", BigDecimal.valueOf(900000), ProductStatus.PUBLISHED),
                createProduct("hoa-sinh-nhat", "Happy Birthday (Mix)", "Bó hoa mix nhiều loại (hồng, cúc, baby).", BigDecimal.valueOf(650000), ProductStatus.PUBLISHED),
                createProduct("hoa-sinh-nhat", "Bó Hoa Thanh Lịch", "Bó hoa ly trắng và hồng.", BigDecimal.valueOf(720000), ProductStatus.PUBLISHED),
                createProduct("hoa-sinh-nhat", "Giỏ Hoa Nhiệt Đới", "Giỏ hoa cắm hồng môn và lá cọ.", BigDecimal.valueOf(950000), ProductStatus.PUBLISHED),
                createProduct("hoa-sinh-nhat", "Hộp Bất Ngờ", "Hộp hoa nhỏ kèm bánh macaron.", BigDecimal.valueOf(550000), ProductStatus.DRAFT),
                createProduct("hoa-sinh-nhat", "Bó Hoa Mẫu Đơn (Peony)", "Bó 5 cành Mẫu Đơn (theo mùa).", BigDecimal.valueOf(1500000), ProductStatus.PUBLISHED),
                createProduct("hoa-sinh-nhat", "Bó Cúc Tana", "Bó cúc tana nhỏ xinh, phong cách Hàn Quốc.", BigDecimal.valueOf(450000), ProductStatus.PUBLISHED),

                // --- BÓ HOA VÀ GIỎ HOA (Bouquets & Baskets) ---
                createProduct("bo-hoa", "Bó Baby Trắng Nhẹ Nhàng", "Bó hoa baby trắng nhập khẩu Hà Lan.", BigDecimal.valueOf(400000), ProductStatus.PUBLISHED),
                createProduct("lang-hoa", "Giỏ Hoa Cẩm Tú Cầu", "Giỏ hoa cẩm tú cầu xanh biếc.", BigDecimal.valueOf(650000), ProductStatus.PUBLISHED),
                createProduct("bo-hoa", "Bó Tulip 10 Bông (Trắng)", "Tulip trắng tinh khôi.", BigDecimal.valueOf(550000), ProductStatus.PUBLISHED),
                createProduct("bo-hoa", "Bó Cát Tường", "Bó hoa cát tường xanh/hồng.", BigDecimal.valueOf(480000), ProductStatus.PUBLISHED),
                createProduct("lang-hoa", "Giỏ Hoa Tặng Mẹ (Cẩm chướng)", "Giỏ hoa cẩm chướng và hoa baby.", BigDecimal.valueOf(700000), ProductStatus.PUBLISHED),
                createProduct("bo-hoa", "Bó Hoa Tốt Nghiệp", "Bó hoa hướng dương và gấu cử nhân.", BigDecimal.valueOf(600000), ProductStatus.PUBLISHED),
                createProduct("lang-hoa", "Giỏ Hoa Cảm Ơn", "Giỏ hoa hồng kem và thạch thảo.", BigDecimal.valueOf(800000), ProductStatus.PUBLISHED),
                createProduct("bo-hoa", "Bó Hoa Oải Hương (Lavender)", "Bó lavender khô nhập Pháp.", BigDecimal.valueOf(550000), ProductStatus.DRAFT),
                createProduct("lang-hoa", "Giỏ Hoa Ly", "Giỏ hoa ly hồng thơm ngát.", BigDecimal.valueOf(750000), ProductStatus.PUBLISHED),
                createProduct("bo-hoa", "Bó Cúc Mẫu Đơn", "Bó 5 cành cúc mẫu đơn mix.", BigDecimal.valueOf(620000), ProductStatus.PUBLISHED),

                // --- HOA CHIA BUỒN (Sympathy) ---
                createProduct("hoa-chia-buon", "Kệ Hoa Thành Kính Phân Ưu", "Kệ hoa ly trắng và lan trắng.", BigDecimal.valueOf(1800000), ProductStatus.PUBLISHED),
                createProduct("hoa-chia-buon", "Vòng Hoa Vô Cùng Thương Tiếc", "Vòng hoa cúc trắng và lan tím.", BigDecimal.valueOf(1500000), ProductStatus.PUBLISHED),
                createProduct("hoa-chia-buon", "Lẵng Hoa Ly Trắng", "Lẵng hoa ly trắng trang trọng.", BigDecimal.valueOf(1100000), ProductStatus.PUBLISHED),
                createProduct("hoa-chia-buon", "Kệ Hoa Lan Hồ Điệp Trắng", "Kệ lan hồ điệp trắng 5 cành.", BigDecimal.valueOf(2200000), ProductStatus.PUBLISHED),
                createProduct("hoa-chia-buon", "Vòng Hoa Tin Lành (Thánh Giá)", "Vòng hoa cúc trắng (cho tang lễ đạo Chúa).", BigDecimal.valueOf(1700000), ProductStatus.PUBLISHED),
                createProduct("hoa-chia-buon", "Kệ Hoa Tông Trắng Xanh", "Kệ hoa hồng trắng và cẩm tú cầu xanh.", BigDecimal.valueOf(1900000), ProductStatus.DRAFT),
                createProduct("hoa-chia-buon", "Giỏ Hoa Cúc Trắng", "Giỏ hoa cúc trắng để bàn viếng.", BigDecimal.valueOf(800000), ProductStatus.PUBLISHED),
                createProduct("hoa-chia-buon", "Kệ Hoa Hiện Đại", "Kệ hoa tông trắng, cắm kiểu hiện đại.", BigDecimal.valueOf(2300000), ProductStatus.PUBLISHED),

                // --- HOA CƯỚI (Wedding) ---
                createProduct("hoa-cuoi", "Bó Hoa Cô Dâu (Hồng Trắng)", "Bó hoa hồng trắng và baby.", BigDecimal.valueOf(1200000), ProductStatus.PUBLISHED),
                createProduct("hoa-cuoi", "Bó Hoa Calla Lily", "Bó hoa rum (Calla Lily) trắng 10 cành.", BigDecimal.valueOf(1500000), ProductStatus.PUBLISHED),
                createProduct("hoa-cuoi", "Bó Hoa Mẫu Đơn (Peony)", "Bó hoa mẫu đơn hồng cho cô dâu.", BigDecimal.valueOf(2500000), ProductStatus.PUBLISHED),
                createProduct("hoa-cuoi", "Hoa Cài Áo Chú Rể", "Hoa hồng trắng nhỏ cài áo.", BigDecimal.valueOf(150000), ProductStatus.PUBLISHED),
                createProduct("hoa-cuoi", "Bó Hoa Cưới (Tông Cam Đất)", "Bó hoa hồng cam đất (hot trend).", BigDecimal.valueOf(1300000), ProductStatus.DRAFT),
                createProduct("hoa-cuoi", "Hoa Để Bàn Tiệc (Thấp)", "Bình hoa nhỏ để bàn tiệc gallery.", BigDecimal.valueOf(450000), ProductStatus.PUBLISHED),
                createProduct("hoa-cuoi", "Hoa Cổng Cưới (Tươi)", "Cổng hoa tươi (Hồng, Lan, Cẩm tú cầu).", BigDecimal.valueOf(8000000), ProductStatus.PUBLISHED),
                createProduct("hoa-cuoi", "Bó Hoa Cầm Tay (Hồng Sen)", "Bó hoa hồng sen và cúc tana.", BigDecimal.valueOf(900000), ProductStatus.PUBLISHED),

                // --- HOA ĐỂ BÀN (Desk Flowers) ---
                createProduct("hoa-de-ban", "Bình Lan Hồ Điệp Vàng", "Bình 3 cành lan hồ điệp vàng.", BigDecimal.valueOf(1300000), ProductStatus.PUBLISHED),
                createProduct("hoa-de-ban", "Chậu Sen Đá Mix", "Chậu xi măng sen đá (nhiều loại).", BigDecimal.valueOf(350000), ProductStatus.PUBLISHED),
                createProduct("hoa-de-ban", "Bình Hoa Tulip (Giả)", "Bình tulip silicon cao cấp (như thật).", BigDecimal.valueOf(400000), ProductStatus.PUBLISHED),
                createProduct("hoa-de-ban", "Chậu Kim Tiền", "Chậu cây kim tiền (phong thủy).", BigDecimal.valueOf(450000), ProductStatus.PUBLISHED),
                createProduct("hoa-de-ban", "Bình Cẩm Tú Cầu Nhỏ", "Bình cẩm tú cầu 1 bông để bàn.", BigDecimal.valueOf(250000), ProductStatus.DRAFT),
                createProduct("hoa-de-ban", "Chậu Sống Đời", "Chậu hoa sống đời (nhiều màu).", BigDecimal.valueOf(150000), ProductStatus.PUBLISHED),
                createProduct("hoa-de-ban", "Bình Hồng Nhập Khẩu", "Bình 5 bông hồng Ecuador.", BigDecimal.valueOf(600000), ProductStatus.PUBLISHED),

                // --- SẢN PHẨM KHÁC (Add-ons) ---
                createProduct("qua-tang", "Socola Ferrero Rocher (16 viên)", "Hộp socola Ferrero Rocher 16 viên.", BigDecimal.valueOf(250000), ProductStatus.PUBLISHED),
                createProduct("qua-tang", "Gấu Bông Nhỏ", "Gấu bông teddy nhỏ (kèm hoa).", BigDecimal.valueOf(180000), ProductStatus.PUBLISHED),
                createProduct("qua-tang", "Thiệp Chúc Mừng (Thủ công)", "Thiệp chúc mừng handmade.", BigDecimal.valueOf(50000), ProductStatus.PUBLISHED)
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
        product.setPrice(price.add(BigDecimal.TEN));
        product.setOriginPrice(price);
        product.setCategory(category);
        product.setStatus(status);
        return product;
    }
}