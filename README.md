# 🌿 PlantyCare - Smart Indoor Plant & Environment Monitor

> **Aplikasi Android Modern berbasis Kotlin Jetpack Compose, MVVM Architecture, SQLite (Room Database), dan Sensor Pintar (Ambient Light & Motion/Vibration Alerts).**

---

## 📖 1. Nama & Deskripsi Aplikasi

**PlantyCare** adalah aplikasi pendamping perawatan tanaman hias *indoor* profesional yang dirancang untuk membantu pemilik tanaman memantau kesehatan tanaman kesayangannya. Aplikasi ini mengintegrasikan **sensor perangkat keras (*hardware sensors*)** smartphone untuk:
1. Mengukur intensitas cahaya ruangan (*Lux*) secara *live* (**Environment Sensor**).
2. Mendeteksi gesture penyiraman melalui gerakan guncangan ponsel (**Motion Sensor - Accelerometer**).
3. **Peringatan Getar Otomatis (*Vibration Reminder Alert*)**: Mengirimkan sinyal getaran ritmis (*bzzz-bzzz*) ke tangan pengguna saat tiba jadwalnya menyiram tanaman, sehingga pengguna tidak akan pernah lupa menyiram tanamannya.

Seluruh data profil dan tanaman tersimpan aman di database lokal SQLite (Room).

---

## ✨ 2. Fitur-Fitur Aplikasi

1. **Apple-style Animated Handwriting Splash Screen 🍎:**
   * Animasi menulis indah huruf demi huruf (*letter-by-letter handwriting*) dengan tipografi kaligrafi (*"hello", "halo", "bonjour", "selamat datang"*).
   * Transisi memudar (*smooth fade*) yang anggun menuju logo resmi **PlantyCare**.
   * Pengecekan otomatis sesi login pengguna.

2. **Sistem Autentikasi Modern (Background Creme + Auto-Slide Tanaman):**
   * Background berbalut warna **Warm Cream** (`#FAF8F3`).
   * *Header* atas memuat **koleksi foto tanaman asli yang bergeser otomatis (*auto-sliding carousel*)** setiap 3,5 detik.
   * *Tab switcher* Masuk (Login) dan Daftar (Sign Up), serta akses cepat *Tamu (Guest Mode)*.

3. **Smart Vibration Alert (Getar Pengingat Siram Otomatis) 📳:**
   * Begitu aplikasi dibuka dan mendeteksi ada tanaman yang sudah jatuh tempo siram, smartphone akan **bergetar dengan ritme khusus (pulsa ganda)** untuk memperingatkan pengguna secara fisik.
   * Terdapat kartu peringatan *"Tanaman Butuh Air!"* di Home Dashboard dan tombol *"Uji Getar"* interaktif.

4. **Katalog & Ensiklopedia Tanaman Asli (Bebas Emotikon) 📸:**
   * Menampilkan foto asli beresolusi tinggi untuk seluruh tanaman (*Kaktus Koboi, Lidah Buaya, Peace Lily, Mawar Mini, Tulip, Monstera, Sansevieria, Sirih Gading, Anggrek Bulan*).
   * Dilengkapi komparasi nilai sensor cahaya ruangan secara *real-time*.

5. **Sensor Hub & Tindakan Interaktif:**
   * **Visualisasi Light Gauge Meter 2D (Canvas)**: Mengukur nilai Lux ruangan tempat pot berada secara *live*.
   * **Fitur Shake-to-Water (Sensor Gerak)**: Deteksi guncangan ponsel via *Accelerometer* untuk menyiram otomatis disertai getaran konfirmasi.

6. **Halaman Profil Pengguna Profesional (Mode Baca & Edit Pensil) ✏️:**
   * Data akun terkunci dalam mode baca (*View Mode*) secara default.
   * Menekan **ikon pensil** di kanan atas akan membuka mode edit (*Edit Mode*) untuk mengubah nama, username, email, dan foto profil dari galeri.

---

## 📡 3. Sensor yang Digunakan & Cara Kerjanya

| No | Sensor | Kategori Sensor | Fungsi & Penerapan dalam Aplikasi |
|:---:|:---|:---|:---|
| 1 | **Ambient Light Sensor** (`Sensor.TYPE_LIGHT`) | **Environment Sensor** | Membaca intensitas cahaya ruangan (*Lux*) secara *real-time*. Nilai ini diverifikasi terhadap kebutuhan tanaman pada katalog (misal: Kaktus butuh 2.500–6.000 Lux, Peace Lily butuh 500–1.500 Lux) untuk memberi tahu pengguna apakah ruangan saat ini terlalu gelap, ideal, atau terlalu terik. |
| 2 | **Accelerometer** (`Sensor.TYPE_ACCELEROMETER`) & **Vibrator Motor** | **Motion Sensor & Haptic Feedback** | **Dual Functionality:**<br>1. **Peringatan Jadwal Siram**: Menghasilkan sinyal getaran ritmis pulsa ganda (*urgent pattern*) untuk memperingatkan pengguna bahwa ada tanaman yang harus disiram hari ini.<br>2. **Gesture Siram**: Mendeteksi guncangan ponsel di dekat pot untuk mencatat aksi penyiraman secara otomatis ke SQLite disertai getaran konfirmasi. |

---

## 🚀 4. Cara Menjalankan Aplikasi di Android Studio

1. **Buka Android Studio**.
2. Pilih menu **File > Open**, lalu pilih folder:
   ```text
   C:\Users\Lenovo\AndroidStudioProjects\PlantyCare
   ```
3. Tunggu proses **Gradle Sync** selesai.
4. Hubungkan HP fisik Android atau jalankan Android Virtual Device (Emulator).
   * *Tips Menguji Sensor di Emulator*: Buka menu titik tiga (**Extended Controls**) > pilih tab **Virtual Sensors** > sesuaikan slider **Light** atau tab **Move** untuk simulasi guncangan.
5. Klik tombol **Run `app`** (ikon segitiga hijau `▶` atau tekan `Shift + F10`).
