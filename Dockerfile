# --- GIAI ĐOẠN 1: BUILD ---
# Sử dụng base image là JDK (Java Development Kit) để build source code
# Sử dụng eclipse-temurin là một bản phân phối OpenJDK được hỗ trợ tốt
FROM eclipse-temurin:17-jdk as build

# Đặt thư mục làm việc
WORKDIR /app

# 1. Copy file pom.xml và tải dependencies
# Tận dụng Docker cache: Bước này chỉ chạy lại khi pom.xml thay đổi
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .
RUN ./mvnw dependency:go-offline

# 2. Copy toàn bộ source code
COPY src ./src

# 3. Build ứng dụng
# Bỏ qua test vì test nên được chạy ở bước CI riêng
RUN ./mvnw package -DskipTests

# --- GIAI ĐOẠN 2: RUN ---
# Sử dụng base image là JRE (Java Runtime Environment) - nhỏ gọn hơn nhiều
FROM eclipse-temurin:17-jre-slim

WORKDIR /app

# Đặt profile mặc định là 'prod' qua biến môi trường
# Bạn có thể dễ dàng ghi đè biến này khi chạy container
ENV SPRING_PROFILES_ACTIVE=prod

# Copy file .jar đã được build từ giai đoạn 'build'
# Đổi tên file thành 'app.jar' để dễ quản lý
COPY --from=build /app/target/*.jar app.jar

# Expose port mà Spring Boot chạy
EXPOSE 8080

# Lệnh để khởi động ứng dụng
# Spring Boot sẽ tự động nhận biến môi trường SPRING_PROFILES_ACTIVE
ENTRYPOINT ["java", "-jar", "app.jar"]