# HỆ THỐNG TƯ VẤN TUYỂN SINH TỰ ĐỘNG
Trong bối cảnh hiện nay, trí tuệ nhân tạo (AI) đang chiếm xu thế ở tất cả các lĩnh vực cuộc sống. Cùng với đó, sự xuất hiện của nhiều công nghệ mới sử dụng sức mạnh của trí tuệ nhân tạo ngày càng nhiều và hiệu quả, giúp ích cho con người rất nhiều trong mọi khía cạnh cuộc sống, đem đến sự tiện lợi, chính xác cực kỳ cao.

Để tận dụng sự mạnh mẽ của trí tuệ nhân tạo, đề tài **"Phát triển hệ thống tư vấn tuyển sinh tự động"** được đề xuất. Mục tiêu của đề tài này nhằm mục đích xây dựng một ứng dụng cho phép hỏi đáp về thông tin ở phạm vi Khoa Công nghệ thông tin - Trường Đại học Mở TP.HCM. Cụ thể về thông tin tuyển sinh, thông tin về khoa, về ngành trong phạm vi được nêu. Từ đó, mang lại sự nhanh chóng, thuận tiện cho việc tìm hiểu thông tin từ nguồn dữ liệu đáng tin cậy.

## Các chức năng của hệ thống
### Chức năng phía người dùng
* Đăng nhập/đăng ký.
* Hỏi đáp và nhận kết quả trả lời.
* Chỉnh sửa thông tin người dùng, đổi mật khẩu, đổi ảnh đại diện.
### Chức năng phía quản trị viên
* Quản lý tài khoản người dùng.
* Quản lý lịch sử hỏi đáp.
* Quản lý dữ liệu vấn đáp.

## Công nghệ sử dụng
* Phía frontend: `Next.js` `TypeScript` `Redux` `Material UI`
* Phía backend: `Java Spring Boot` `Dialogflow`
* Phía cơ sở dữ liệu: `MySQL`

## Hướng dẫn cài đặt (Chạy trên local)
#### Bước 1: Yêu cầu môi trường cài đặt (vui lòng bỏ qua nếu đã đáp ứng các môi trường bên dưới).
* **Cài đặt môi trường NodeJS:** vào [trang chủ của NodeJS](https://nodejs.org) và tải về phiên bản `20.11.1 LTS` (tính đến ngày 28/02/2024). Sau đó mở tập tin cài đặt đã tải và làm theo hướng dẫn.
* **Cài đặt JDK 18:** tải và cài đặt JDK 18 [tại đây](https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html). Sau đó mở tập tin cài đặt đã tải và làm theo hướng dẫn.
* **Cài đặt MySQL Community Server:** tải và cài đặt MySQL Community Server [tại đây](https://dev.mysql.com/get/Downloads/MySQL-8.1/mysql-8.1.0-winx64.msi). Sau đó mở tập tin cài đặt đã tải và làm theo hướng dẫn.
* **Cài đặt MySQL Workbench:** tải và cài đặt MySQL Workbench [tại đây](https://dev.mysql.com/get/Downloads/MySQLGUITools/mysql-workbench-community-8.0.34-winx64.msi). Sau đó mở tập tin cài đặt đã tải và làm theo hướng dẫn.
* **Cài đặt Visual Studio Code:** vào [trang chủ của Visual Studio Code](https://code.visualstudio.com/download) và tải phiên bản phù hợp với máy tính.
* **Cài đặt IntelliJ IDEA Community:** vào [trang chủ của JetBrains](https://www.jetbrains.com/idea/download/?section=windows) và tải phiên bản IntelliJ IDEA Commnunity.
  
#### Bước 2: Mở `command line` hoặc `git bash` và gõ lệnh sau để tải mã nguồn đồ án về máy tính.
```
git clone https://github.com/workanhnguyen/automatic_consulting
```
#### Bước 3: Mở thư mục `client` bằng **Visual Studio Code** và mở thư mục `server` bằng **IntelliJ IDEA Community**.

#### Bước 4: Mở **MySQL Workbench** và tạo mới một schema đặt tên là `nckh_2023_db`. Sau đó nhập dữ liệu từ tập tin `nckh_2023_db.sql`.

#### Bước 5: Với thư mục `server` đã mở với **IntelliJ IDEA Community**, mở tập tin `application.properties` và sửa lại các lệnh sau:
```
spring.datasource.username=<Tên đăng nhập MySQL>
spring.datasource.password=<Mật khẩu đăng nhập MySQL>
```
Sau khi chỉnh sửa, mở tập tin `ServerApplication.java` và chạy tập tin này. Ứng dụng Java sẽ được chạy trên `http://localhost:8080`.

#### Bước 6: Với thư mục `client` đã mở với **Visual Studio Code**, mở terminal và thực thi lần lượt các lệnh sau:
```
npm install yarn
```
Sau khi thực thi thành công lệnh trên, thực thi tiếp lệnh sau để cài đặt tất cả các package cần thiết cho ứng dụng `ReactJS`:
```
yarn install
```
Cuối cùng thực thi lệnh sau để chạy ứng dụng:
```
yarn dev
```
Ứng dụng sẽ chạy trên `http://localhost:3000`.

## Hướng dẫn sử dụng
Người dùng truy cập vào `http://localhost:3000` và đăng nhập bằng tài khoản sẵn có hoặc đăng ký tài khoản mới. 
* Khi đăng nhập thành công, người dùng có thể sử dụng các chức năng của hệ thống.

Đối với quản trị viên, truy cập vào `http://localhost:8080/login` và sử dụng tài khoản sau để đăng nhập với vai trò admin:
```
Tên tài khoản: admin@gmail.com
Mật khẩu: admin
```
* Sau khi đăng nhập thành công, quản trị viên có thể sử dụng các chức năng quản trị.

## Thông tin nhà phát triển
* Họ và tên: Nguyễn Vân Anh
* MSSV: 2051012004
* Trường Đại học Mở thành phố Hồ Chí Minh.

@ 2024
