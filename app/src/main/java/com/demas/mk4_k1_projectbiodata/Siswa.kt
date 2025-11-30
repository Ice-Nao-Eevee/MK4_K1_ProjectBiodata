package com.demas.mk4_k1_projectbiodata

data class Siswa(
    val nama: String,
    val absen: String, // Hanya angka "1" bukan "Absen: 1"
    val nis: String,
    val kelas: String,
    val gender: String, // Tambahkan ini
    val hobi: String? = "Tidak Diketahui" // Tambahkan ini (berikan nilai default sementara)
)