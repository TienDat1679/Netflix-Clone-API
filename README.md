# 🎬 Movie & TV Series App

Ứng dụng xem phim và gợi ý phim sử dụng Spring Boot, MySQL
## 📌 Tính năng chính

- 🔍 Tìm kiếm phim và TV Series
- ❤️ Lưu phim yêu thích
- 📝 Tạo danh sách xem
- 🤖 Hệ thống gợi ý phim thông minh dựa trên hành vi người dùng
- 📄 Giao diện người dùng thân thiện
- Xem phim và lưu thời gian xem
- Bình luận cho xem
- .........

## 🛠️ Công nghệ sử dụng

- **Backend**: Spring Boot
- **Database**: MySQL
- **API giao tiếp**: RESTful

## 🔧 Cài đặt và chạy ứng dụng

### 1. Clone dự án

git clone https://github.com/TienDat1679/Netflix-Clone-API.git
### 2. Cau hinh database

CREATE DATABASE netflix-clone-local CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- Tạo tài khoản người dùng
CREATE USER 'root'@'localhost' IDENTIFIED BY '1234';

-- Cấp quyền cho người dùng trên database
GRANT ALL PRIVILEGES ON netflix-clone-local.* TO 'root'@'localhost';
### 3. Chạy ứng dụng

-- Import dự án vào ứng dụng intelij sau đó khởi động bằng dự án spring boot

### 2. Them du lieu

-- Giải nén file database.7z
-- Import dữ liệu trong file vào cơ sở dữ liệu mysql vừa tạo ở trên
