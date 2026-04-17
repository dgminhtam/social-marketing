---
name: define-beans
description: 'Quy chuẩn khai báo Spring Bean sử dụng hướng tiếp cận hybrid giữa Auto-Scanning và @Configuration.'
---

# Quy Chuẩn Khai Báo Spring Bean: Hướng Tiếp Cận Hybrid (Hỗn Hợp)

## Mục Đích
Tài liệu này quy định cách thức khai báo và quản lý Spring Beans trong dự án. Để tối ưu hóa giữa tốc độ phát triển (Development Speed) và khả năng kiểm soát cấu hình (Configuration Control), dự án áp dụng Hướng tiếp cận Hybrid (Hỗn hợp).

**Nguyên tắc cốt lõi:** Kết hợp sức mạnh quét tự động (Component Scanning) cho mã nguồn nội bộ và cấu hình thủ công (Manual Configuration) cho các thư viện bên ngoài hoặc logic khởi tạo phức tạp.

## Quy Tắc 1: Sử Dụng Stereotype Annotations (Ưu tiên Mặc định)
Sử dụng các annotation `@Component`, `@Service`, `@Repository`, `@Controller`, `@RestController` cho tất cả các class thuộc mã nguồn của dự án (Internal Code) do team tự phát triển.

**Khi nào sử dụng:**
- Các lớp xử lý nghiệp vụ (Business Logic).
- Các lớp tương tác với cơ sở dữ liệu (Data Access Layer).
- Các lớp định tuyến request (Controllers).
- Các tiện ích nội bộ đơn giản (Internal Utils).

**Ưu điểm:**
- Code ngắn gọn, giảm thiểu boilerplate code.
- Tận dụng tối đa tính năng Auto-scanning của Spring Boot.

**Ví dụ chuẩn:**
```java
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void processOrder(Order order) {
        // Logic xử lý
    }
}
```

## Quy Tắc 2: Sử Dụng @Bean trong @Configuration (Trường hợp Đặc biệt)
Sử dụng `@Bean` đặt bên trong các lớp `@Configuration` cho các thư viện của bên thứ 3 (External Libraries) hoặc khi cần logic khởi tạo đặc thù.

**Khi nào sử dụng:**
- **Thư viện ngoài:** Không thể gắn `@Component` trực tiếp vào mã nguồn của họ (ví dụ: `RestTemplate`, `ObjectMapper`, `AmazonS3`, `RedisTemplate`).
- **Khởi tạo phức tạp:** Cần gọi các hàm builder, setter, hoặc tính toán logic trước khi tạo ra object.
- **Nhiều instance (Multiple Beans):** Cần tạo nhiều Bean từ cùng một class nhưng với cấu hình khác nhau (Ví dụ: 2 cái DataSource kết nối đến 2 database khác nhau).
- **Cấu hình theo điều kiện:** Kết hợp với `@ConditionalOnProperty`, `@ConditionalOnMissingBean`...

**Ví dụ chuẩn:**
```java
@Configuration
public class WebClientConfig {

    // Ví dụ 1: Bean từ thư viện ngoài với cấu hình tùy chỉnh
    @Bean
    public ObjectMapper customObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    // Ví dụ 2: Khởi tạo Bean cần Builder phức tạp
    @Bean
    public RestTemplate paymentRestTemplate(RestTemplateBuilder builder) 
        return builder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
    }
}
```

## Các Nguyên Tắc Bổ Sung (Best Practices)

### 4.1. Phân chia File Configuration Hợp Lý
Không gộp toàn bộ `@Bean` vào một file `AppConfig` duy nhất. Hãy chia nhỏ theo domain hoặc chức năng để dễ bảo trì.
- **Nên làm:** `SecurityConfig`, `DatabaseConfig`, `CacheConfig`, `ApiIntegrationConfig`.
- **Không nên:** `GlobalAppConfiguration` (chứa từ database đến redis, security...).

### 4.2. Dependency Injection: Luôn Dùng Constructor Injection
Dù sử dụng Stereotype hay `@Bean`, ưu tiên tuyệt đối **Constructor Injection**. Hạn chế tối đa Field Injection (`@Autowired` hoặc `@Resource` trên biến).

**Nên làm (Sử dụng Explicit Constructor hoặc `@RequiredArgsConstructor`):**
```java
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

**Không nên (Field Injection):**
```java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository; // Vi phạm quy tắc
}
```

### 4.3. Đặt Tên Bean Rõ Ràng
Khi dùng `@Bean`, tên method chính là tên của Bean. Hãy đặt tên rõ nghĩa, đặc biệt khi có nhiều Bean cùng kiểu.
```java
@Bean
public DataSource primaryDataSource() { ... }

@Bean
public DataSource readOnlyDataSource() { ... }
```

## Anti-Patterns (Những Lỗi Cần Tránh)

- **Lạm dụng `@Bean` cho code nội bộ:** Đừng dùng `@Bean` để khởi tạo `UserService` hay `OrderController` nếu không có lý do thực sự đặc biệt. Điều này làm tăng code Boilerplate thừa vô ích.
- **Khởi tạo trạng thái (State) bên trong Bean Config:** Các lớp `@Configuration` chỉ nên làm nhiệm vụ "lắp ráp" (wiring) các object. Không viết logic nghiệp vụ (business logic) hoặc gọi API bên ngoài ngay trong method `@Bean`.