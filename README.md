# PlayMusic - Android Jetpack Compose Karaoke Player App 🎵🎤

Dự án ứng dụng Trình phát nhạc Karaoke chuẩn Android hiện đại, được xây dựng **100% bằng Jetpack Compose** với giao diện mượt mà và trải nghiệm hát Karaoke độc đáo.

---

## 📌 Giới Thiệu Dự Án

**PlayMusic** là ứng dụng trình phát nhạc Android hỗ trợ hiển thị lời bài hát Karaoke khớp thời gian thực từng từ. Ứng dụng mang đến trải nghiệm âm nhạc sống động với phong cách thiết kế Pastel Linear Gradient sang trọng, hỗ trợ xem trước lyric 2 dòng luân phiên thông minh và màn hình xem toàn bộ danh sách lời bài hát tự động cuộn theo giai điệu.

---

## ✨ Các Tính Năng Nổi Bật

### 1. 🎵 Phát Nhạc Trực Tuyến & Điều Khiển Phát Nhạc
- Phát nhạc mượt mà với âm thanh chất lượng cao.
- Hỗ trợ đầy đủ các thao tác: Bật/Tạm dừng nhạc, tua tiến/tua lùi thời gian phát.
- Hiển thị thời gian đã phát và thời gian đếm ngược còn lại của bài hát.

### 2. 🎤 Tô Màu Lời Karaoke Luân Phiên 2 Dòng Thông Minh
- **Tô màu từng từ theo thời gian thực**: Chữ trên màn hình tự động chuyển màu mượt mà chuẩn xác theo nhịp hát.
- **Hiển thị luân phiên 2 dòng**:
  - Khi một dòng đang được hát và tô màu, dòng còn lại sẽ tự động hiển thị câu hát tiếp theo để người dùng chuẩn bị trước.
  - Sau khi hát xong một câu, vị trí hát sẽ luân phiên chuyển đổi giữa dòng trên và dòng dưới giúp trải nghiệm nhìn không bị ngắt quãng.
- **Tự động căn giữa & xuống dòng**: Các câu hát dài được tự động ngắt xuống dòng cân đối, luôn hiển thị chính giữa màn hình mà không bị tràn ra ngoài.

### 3. 📜 Màn Hình Xem Toàn Bộ Lời Bài Hát (Full Lyric)
- **Xem toàn bộ lời bài hát**: Chạm vào vùng lyric ở màn hình chính để mở danh sách toàn bộ lời bài hát từ đầu đến cuối.
- **Tự động cuộn theo nhạc**: Danh sách lời bài hát tự động cuộn đưa câu hát đang phát vào vị trí trung tâm màn hình.
- **Thông minh khi thao tác vuốt**: Khi người dùng chạm vuốt danh sách để đọc trước lời, hệ thống tự động tạm dừng cuộn. Khi thả tay ra, danh sách sẽ tự động cuộn mượt trở lại đúng vị trí câu hát đang phát.
- **Tua nhạc theo câu hát**: Bấm trực tiếp vào bất kỳ dòng lyric nào trong danh sách để tua bài hát đến ngay thời điểm đó.

### 4. 🎨 Giao Diện Hiện Đại & Tinh Tế
- Tông màu nền Pastel Gradient (Xanh - Tím - Hồng) hài hòa, sang trọng.
- Thanh tua nhạc siêu mảnh, tinh gọn, không chi tiết thừa.
- Bộ biểu tượng hiển thị sắc nét và tương thích tốt trên mọi kích thước màn hình.

---

## 🛠️ Công Nghệ & Thư Viện (Tech Stack)

- **Ngôn ngữ lập trình**: Kotlin 2.0
- **Giao diện (UI)**: 100% Jetpack Compose (Material 3, Canvas Graphics, Custom Layouts)
- **Kiến trúc (Architecture)**: Clean Architecture + MVVM + Unidirectional Data Flow (UDF)
- **Quản lý phụ thuộc (DI)**: Koin 3.5 (`koin-android`, `koin-androidx-compose`)
- **Trình phát âm thanh (Audio Engine)**: AndroidX Media3 ExoPlayer 1.5.1
- **Xử lý bất đồng bộ**: Kotlin Coroutines & `StateFlow`
- **Định dạng dữ liệu**: XML DOM Parser (Xử lý mốc thời gian millisecond từng từ Lyric)

---

## 📂 Cấu Trúc Mã Nguồn (Source Code Structure)

```
d:/Android kotlin/PlayMusic/app/src/main/java/com/example/playmusic/
├── MainActivity.kt                      # Entry point với Edge-to-Edge Scaffold
├── MainApplication.kt                 # Khởi tạo Koin Dependency Injection Context
├── data/
│   ├── model/
│   │   └── LyricModel.kt              # Data model LyricLine & LyricWord
│   └── parser/
│       └── LyricParser.kt             # Tải & Parse XML lời Karaoke từ URL
├── di/
│   └── AppModule.kt                   # Koin DI Module khai báo ViewModel
├── ui/
│   ├── components/
│   │   ├── KaraokeCanvas.kt           # Canvas tô màu Karaoke 2 dòng luân phiên
│   │   ├── DiscAnimation.kt           # Đĩa nhạc đĩa than xoay tròn & cần đọc
│   │   └── PlayerControls.kt          # Thanh điều khiển nhạc & Seekbar siêu mảnh
│   ├── player/
│   │   ├── PlayerViewModel.kt         # Quản lý ExoPlayer engine & 60fps Ticker State
│   │   ├── PlayerScreen.kt            # Stateful Root Screen (Koin Injected)
│   │   ├── state/
│   │   │   └── PlayerScreenState.kt   # Data class bất biến UI State
│   │   └── view/
│   │       ├── PlayerContent.kt       # Trình phát nhạc chính (Stateless UI)
│   │       └── FullLyricContent.kt    # Màn hình xem toàn bộ Lyric cuộn tự động
│   └── theme/                         # Theme, Color, Type
```

---

## 🚀 Khởi Chạy Dự Án (Getting Started)

### Yêu cầu hệ thống:
- **Android Studio**: Ladybug / 2024.2.1+
- **JDK**: Java 17 / 21
- **Android SDK**: `compileSdk = 36`, `minSdk = 24`

### Cài đặt & Chạy trên máy ảo / thiết bị thật:
```bash
# Biên dịch và cài đặt APK Debug qua ADB
.\gradlew.bat installDebug

# Khởi chạy ứng dụng trên thiết bị
adb shell am start -n com.example.playmusic/.MainActivity
```
