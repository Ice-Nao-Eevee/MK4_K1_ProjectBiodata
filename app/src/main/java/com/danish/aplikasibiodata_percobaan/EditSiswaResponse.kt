package com.danish.aplikasibiodata_percobaan

// Kelas data utama untuk menangkap response API
data class EditSiswaResponse(
    val success: Boolean,
    val message: String,
    val data: SiswaData? // 👈 Pastikan tipe datanya mengarah ke class SiswaData di bawah
)

// Kelas data child untuk menampung field objek data siswa
data class SiswaData(
    val id: Int,
    val nama: String,
    val nis: String,
    val no_absen: Int,
    val jenis_kelamin: String,
    val tanggal_lahir: String?,
    val whatsapp: String?,
    val instagram: String?,
    val bio: String?,
    val quote: String?,
    val foto: String? // 👈 KUNCINYA DI SINI CONG! Tambahkan baris ini biar gak merah lagi!
)