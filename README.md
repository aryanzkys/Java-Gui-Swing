# LK08 - Junior High School Library Management System (Swing GUI)

Proyek ini dibuat untuk tugas **Pemrograman Lanjut (LK08)** berupa aplikasi Java berbasis **GUI Swing** untuk manajemen perpustakaan SMP.

## Identitas Tim

1. ARYAN ZAKY PRAYOGO - 255150207111059  
2. ACHMAD HUJAIRI - 255150200111042  
3. M. HIDAYATULLOH H. A. M - 255150201111025  
4. M. AHSHAL ZILHAMSYAH - 255150200111041  
5. DIKARDO SIAHAAN - 255150200111040  

## Distribusi Tugas (Format Akademika)

Berikut pembagian peran dan tanggung jawab anggota tim dalam pengembangan sistem:

1. **ARYAN ZAKY PRAYOGO (255150207111059)**  
	**Peran:** Koordinator pengembangan dan integrasi aplikasi.  
	**Tanggung jawab utama:**
	- Menyusun arsitektur awal program.
	- Mengembangkan [src/Main.java](src/Main.java) sebagai titik masuk aplikasi.
	- Mengintegrasikan alur login, navigasi tab utama, dan pemanggilan layanan sistem.
	- Melakukan validasi akhir alur eksekusi program.

2. **ACHMAD HUJAIRI (255150200111042)**  
	**Peran:** Pengembang modul data siswa dan data buku.  
	**Tanggung jawab utama:**
	- Mengembangkan kelas model [src/Student.java](src/Student.java) dan [src/Book.java](src/Book.java).
	- Mengimplementasikan fitur CRUD siswa dan buku pada [src/LibrarySwingApp.java](src/LibrarySwingApp.java).
	- Menyusun mekanisme pencarian data siswa/buku di GUI.

3. **M. HIDAYATULLOH H. A. M (255150201111025)**  
	**Peran:** Pengembang modul data pegawai dan autentikasi.  
	**Tanggung jawab utama:**
	- Mengembangkan kelas model [src/Employee.java](src/Employee.java).
	- Mengimplementasikan proses login pegawai berbasis data file (dialog login).
	- Mengembangkan fitur CRUD pegawai pada [src/LibrarySwingApp.java](src/LibrarySwingApp.java).

4. **M. AHSHAL ZILHAMSYAH (255150200111041)**  
	**Peran:** Pengembang modul transaksi perpustakaan.  
	**Tanggung jawab utama:**
	- Mengembangkan kelas model [src/Transaction.java](src/Transaction.java).
	- Mengimplementasikan fitur peminjaman dan pengembalian buku pada [src/LibrarySwingApp.java](src/LibrarySwingApp.java).
	- Menerapkan aturan bisnis (maksimal pinjam, validasi ketersediaan buku, status transaksi).

5. **DIKARDO SIAHAAN (255150200111040)**  
	**Peran:** Pengembang utilitas berkas, laporan, dan dokumentasi.  
	**Tanggung jawab utama:**
	- Mengembangkan utilitas File I/O pada [src/FileHelper.java](src/FileHelper.java).
	- Mengimplementasikan laporan pada [src/LibrarySwingApp.java](src/LibrarySwingApp.java).
	- Menyusun dokumentasi proyek pada [README.md](README.md).

### Mekanisme Kolaborasi

- Seluruh anggota berkontribusi pada tahap pengujian fungsional dan perbaikan bug.
- Integrasi akhir dilakukan setelah masing-masing modul dinyatakan berjalan.
- Setiap perubahan kode didiskusikan untuk menjaga konsistensi gaya penulisan dan struktur program.

## Deskripsi Singkat

Aplikasi ini menggunakan:

- **OOP (Object-Oriented Programming)**
- **String Processing** (`split`, `trim`, `contains`, `equalsIgnoreCase`, `replace`, `substring`, `format`)
- **File I/O TXT** (`BufferedReader`, `BufferedWriter`, `FileReader`, `FileWriter`, `PrintWriter`)
- **Exception Handling** (`try-catch-finally`)
- **Swing GUI** (`JFrame`, `JTabbedPane`, `JTable`, `JOptionPane`)

Semua data disimpan dalam file teks dengan delimiter `|`:

- `data/siswa.txt`
- `data/buku.txt`
- `data/pegawai.txt`
- `data/transaksi.txt`

## Fitur Aplikasi

### 1) Login Pegawai
- Login dengan **NIP** dan **Nama**
- Data diambil dari `pegawai.txt`
- Jika gagal, user diminta mengulang

### 2) Menu Utama (GUI)
- Tab **Siswa**
- Tab **Buku**
- Tab **Pegawai**
- Tab **Transaksi** (Peminjaman & Pengembalian)
- Tab **Laporan**

### 3) CRUD Siswa
- Tambah siswa
- Lihat siswa
- Edit siswa
- Hapus siswa
- Cari siswa (keyword)

Field: `NIS | Nama | Alamat`

### 4) CRUD Buku
- Tambah buku
- Lihat buku
- Edit buku
- Hapus buku
- Cari buku (keyword)

Field: `Kode | Judul | Jenis Buku`

### 5) CRUD Pegawai
- Tambah pegawai
- Lihat pegawai
- Edit pegawai
- Hapus pegawai

Field: `NIP | Nama | Tanggal Lahir`

### 6) Peminjaman Buku
Field transaksi:
- Kode Transaksi
- NIS
- Kode Buku
- Tanggal Pinjam
- Tanggal Kembali
- Status

Aturan:
- 1 siswa maksimal meminjam **2 buku aktif** (`status = 0`)
- Buku yang masih dipinjam orang lain tidak bisa dipinjam
- Data disimpan ke `transaksi.txt`
- `status 0 = belum kembali`

### 7) Pengembalian Buku
- Input kode transaksi
- Ubah status dari `0` ke `1`
- Simpan kembali ke file

### 8) Laporan
```
===== REPORT MENU =====
1. Books Not Returned
2. Overdue Borrowers
3. Borrow History
4. Total Students
5. Total Books
6. Back
```

Keterangan:
- **Overdue** jika `tanggal kembali < tanggal hari ini` dan `status = 0`
- Menampilkan nama siswa dan judul buku

## Struktur Proyek

```
java-school-library-system-swing/
├── src/
│   ├── Main.java
│   ├── Student.java
│   ├── Book.java
│   ├── Employee.java
│   ├── Transaction.java
│   ├── FileHelper.java
│   └── LibrarySwingApp.java
├── data/
│   ├── siswa.txt
│   ├── buku.txt
│   ├── pegawai.txt
│   └── transaksi.txt
├── LICENSE
└── README.md
```

## Cara Menjalankan (Windows - Command Prompt/PowerShell)

Pastikan Java sudah terpasang (JDK 8+).

1. Masuk ke folder proyek:
	- `cd d:\Projectku\kuliah\pemlan\LK-08\java-school-library-system-swing`

2. Compile semua file Java:
	- `javac src\*.java`

3. Jalankan program:
	- `java -cp src Main`

## Catatan Tambahan

- Program akan otomatis membuat file `.txt` jika belum ada.
- Program juga mengisi **dummy data** awal (siswa, buku, pegawai) jika file masih kosong.
- Aplikasi GUI menampilkan tabel data dan form input agar mudah digunakan.

Semoga bermanfaat untuk tugas LK08.
