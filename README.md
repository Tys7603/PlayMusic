# PlayMusic - Android Jetpack Compose Karaoke Player App 🎵🎤

Dự án ứng dụng Trình phát nhạc Karaoke chuẩn Android hiện đại, được xây dựng **100% bằng Jetpack Compose** với kiến trúc Clean Architecture, MVVM, Koin Dependency Injection và ExoPlayer (Media3).

---

## 📌 Giới Thiệu Dự Án

**PlayMusic** là ứng dụng trình phát nhạc Android hỗ trợ hiển thị lời bài hát Karaoke khớp thời gian thực từng từ (sub-character word-level highlighting) ở tốc độ 60fps (chu kỳ 16ms). Giao diện ứng dụng sở hữu phong cách thiết kế Pastel Linear Gradient sang trọng, hỗ trợ xem trước lyric 2 dòng luân phiên và màn hình danh sách toàn bộ lời bài hát cuộn tự động thông minh.

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
│   │   ├── KaraokeCanvas.kt           # Canvas tô màu Karaoke 2 dòng luân phiên (Ping-Pong)
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

## ✨ Các Tính Năng Nổi Bật

### 1. 🎵 Trình Phát Nhạc Chất Lượng Cao (ExoPlayer Media3)
- Phát phát trực tuyến file âm thanh beat MP3 mượt mà.
- Hỗ trợ Play / Pause, tua tiến / tua lùi, cập nhật thời gian đã phát và thời gian còn lại (dạng số âm `-mm:ss`).

### 2. 🎤 Thuật Toán Tô Màu Lyric Karaoke Luân Phiên 2 Dòng (Ping-Pong)
- **Tô màu từng từ thời gian thực**: Sử dụng `Canvas.clipRect()` cắt cúp độ rộng theo millisecond của từng từ giúp câu hát được tô màu đen mượt mà trên nền trắng.
- **Thuật toán luân phiên 2 dòng**:
  - Dòng số chẵn (0, 2, 4...): Dòng trên active tô màu; Dòng dưới hiển thị câu hát tiếp theo ở dạng xem trước.
  - Dòng số lẻ (1, 3, 5...): Dòng dưới active tô màu; Dòng trên cập nhật câu hát tiếp-tiếp theo ở dạng xem trước.
- **Căn giữa & Xuống dòng tự động (`StaticLayout`)**: Tự động ngắt từ xuống dòng và căn giữa cân đối khi câu hát quá dài.

### 3. 📜 Màn Hình Xem Toàn Bộ Lời Bài Hát (Full Lyric Screen)
- Chạm vào vùng Lyric ở màn hình chính để mở màn hình toàn bộ lời bài hát.
- **Tự động cuộn theo nhạc**: Danh sách `LazyColumn` tự động cuộn đưa câu hát hiện tại vào vị trí trung tâm màn hình.
- **Tạm dừng khi vuốt tay**: Khi người dùng cuộn tay (`isScrollInProgress == true`), chế độ tự động cuộn tạm dừng để xem các câu hát khác. Ngay khi thả tay ra, danh sách tự động cuộn mượt trở lại câu hát đang phát.
- **Tua nhanh đến câu hát**: Chạm vào bất kỳ dòng lyric nào trong danh sách để tua bài hát đến mốc thời gian đó.

### 4. 🎨 Giao Diện Pastel Premium & Koin DI Clean Code
- Thiết kế nền Pastel Gradient (Xanh - Tím - Hồng) chuẩn hiện đại.
- Thanh tua Seekbar siêu mảnh `3.dp` không chấm thừa, biểu tượng Vector XML sắc nét.
- Kiến trúc Clean Code không chứa comment rác, tách biệt Stateful Screen & Stateless View.

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
