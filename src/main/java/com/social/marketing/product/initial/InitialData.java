package com.social.marketing.product.initial;

import com.social.marketing.product.entity.Category;
import com.social.marketing.product.entity.Product;
import com.social.marketing.product.entity.ProductStatus;
import com.social.marketing.product.service.CategoryService;
import com.social.marketing.product.service.ProductService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

//@Component
public class InitialData {

    @Resource
    private CategoryService categoryService;

    @Resource
    private ProductService productService;

    @PostConstruct
    @Transactional
    public void init() {
        initCategory();
        initProduct();
    }

    private void initCategory() {
        List<Category> categories = List.of(
                createCategory("bigo", "Bigo", "Live Streaming Platform"),
                createCategory("facebook", "Facebook", "Social Media Platform"),
                createCategory("spotify", "Spotify", "Music Streaming Platform"),
                createCategory("instagram", "Instagram", "Photo & Video Sharing Platform"),
                createCategory("tiktok", "TikTok", "Short Video Sharing Platform"),
                createCategory("threads", "Threads", "Text-based Social Networking Platform"),
                createCategory("shopee", "Shopee", "E-Commerce Platform"),
                createCategory("youtube", "YouTube", "Video Sharing Platform"),
                createCategory("twitter", "Twitter", "Microblogging Platform"),
                createCategory("telegram", "Telegram", "Messaging Platform"),
                createCategory("google", "Google", "Search & Technology Platform")
        );
        categoryService.saveAll(categories);
    }

    private Category createCategory(String code, String name, String description) {
        Category category = new Category();
        category.setCode(code);
        category.setName(name);
        category.setDescription(description);
        category.setActive(true);
        return category;
    }

    private void initProduct() {
        List<Product> products = List.of(
                createProduct("threads", "Bình luận"),
                createProduct("youtube", "Bình luận"),
                createProduct("threads", "Chia sẻ"),
                createProduct("spotify", "Tăng followers"),
                createProduct("facebook", "Like Bình luận"),
                createProduct("facebook", "Like bài viết"),
                createProduct("facebook", "Like page"),
                createProduct("shopee", "Like sản phẩm"),
                createProduct("threads", "Tăng like"),
                createProduct("tiktok", "Tăng like"),
                createProduct("twitter", "Tăng like"),
                createProduct("youtube", "Tăng like"),
                createProduct("bigo", "Live Stream"),
                createProduct("youtube", "Live"),
                createProduct("youtube", "Lượt xem"),
                createProduct("twitter", "Mắt live"),
                createProduct("telegram", "Post view"),
                createProduct("telegram", "Reactions"),
                createProduct("facebook", "Review + đánh giá page"),
                createProduct("instagram", "Tăng theo dõi"),
                createProduct("facebook", "Tăng theo dõi"),
                createProduct("threads", "Tăng theo dõi"),
                createProduct("twitter", "Tăng theo dõi"),
                createProduct("youtube", "Tăng theo dõi"),
                createProduct("instagram", "Tim bài viết"),
                createProduct("instagram", "Tăng bình luận"),
                createProduct("facebook", "Tăng bình luận"),
                createProduct("tiktok", "Tăng comment"),
                createProduct("tiktok", "Tăng lượt xem video"),
                createProduct("facebook", "Tăng member group"),
                createProduct("telegram", "Tăng member nhóm"),
                createProduct("facebook", "Tăng lượt xem livestream"),
                createProduct("instagram", "Tăng lượt xem livestream"),
                createProduct("tiktok", "Tăng lượt xem livestream"),
                createProduct("tiktok", "Tăng save"),
                createProduct("facebook", "Tăng share"),
                createProduct("tiktok", "Tăng share"),
                createProduct("tiktok", "Tăng theo dõi"),
                createProduct("shopee", "Tăng theo dõi"),
                createProduct("facebook", "Tăng view video"),
                createProduct("instagram", "Tăng view story"),
                createProduct("instagram", "Tăng view"),
                createProduct("facebook", "View Story"),
                createProduct("facebook", "Vip like"),
                createProduct("twitter", "View"),
                createProduct("google", "Đánh giá map")
        );
        productService.saveAll(products);
    }

    private Product createProduct(String categoryCode, String name) {
        Category category = categoryService.findByCode(categoryCode);
        Product product = new Product();
        product.setSku(UUID.randomUUID().toString());
        product.setName(name);
        product.setDescription("Auto-generated product for " + name);
        product.setCategory(category);
        product.setStatus(ProductStatus.DRAFT);
        return product;
    }
}