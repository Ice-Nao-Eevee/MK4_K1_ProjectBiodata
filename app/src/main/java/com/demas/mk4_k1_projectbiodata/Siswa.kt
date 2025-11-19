package com.demas.mk4_k1_projectbiodata

data class Siswa(
    val nama: String,
    val absen: String,
    val nis: String,
    val kelas: String,
    val gender: String,
    val hobi: String = "" // Optional, bisa kosong
)