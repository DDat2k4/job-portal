# Stage 1: Build stage
# Dùng image Maven chính thức có sẵn JDK Amazon Corretto 21 để build project
FROM maven:3.9.9-amazoncorretto-21 AS build

# Tạo thư mục làm việc trong container
WORKDIR /app

# Copy file cấu hình Maven (pom.xml) vào container
COPY pom.xml .

# Copy toàn bộ source code vào container
COPY src ./src

# Build project, tạo file .jar trong thư mục target (bỏ qua chạy test cho nhanh)
RUN mvn clean package -DskipTests


# Stage 2: Run stage
# Dùng image runtime Amazon Corretto 21.0.7 , chỉ cần để chạy ứng dụng
FROM amazoncorretto:21.0.7

# Tạo thư mục làm việc trong container runtime
WORKDIR /app

# Copy file jar từ build stage sang stage runtime
COPY --from=build /app/target/*.jar app.jar

# Mở port 8080 cho ứng dụng Spring Boot
EXPOSE 8080

# Lệnh khởi chạy ứng dụng khi container start
ENTRYPOINT ["java", "-jar", "app.jar"]
