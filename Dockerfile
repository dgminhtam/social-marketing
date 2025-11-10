# ----- GIAI ĐOẠN 1: BUILD (Dùng JDK đầy đủ) -----
# Dùng base image của Maven và Java 17
FROM eclipse-temurin:17-jdk-jammy AS build

# Đặt thư mục làm việc
WORKDIR /app

# Copy file pom.xml và .mvn (để tận dụng cache)
COPY pom.xml .
COPY .mvn/ .mvn/

# === THÊM 2 DÒNG SỬA LỖI Ở ĐÂY ===
COPY mvnw .
RUN chmod +x mvnw
# ================================

# Copy toàn bộ source code
COPY src ./src

# Chạy lệnh build của Maven
# -DskipTests để bỏ qua test, build nhanh hơn
RUN ./mvnw clean package -DskipTests


# ----- GIAI ĐOẠN 2: RUN (Dùng JRE mỏng nhẹ) -----
# Dùng base image JRE (chỉ chứa môi trường chạy, không cần JDK)
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Lấy file .jar đã được build từ giai đoạn 1
# (Tên file .jar có thể khác, nhưng `*.jar` sẽ tự tìm)
COPY --from=build /app/target/*.jar app.jar

# Port mà Spring Boot đang chạy (khớp với application-prod.yml)
EXPOSE 8080

# Lệnh để khởi động ứng dụng
# Kích hoạt profile "prod"
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=dev"]