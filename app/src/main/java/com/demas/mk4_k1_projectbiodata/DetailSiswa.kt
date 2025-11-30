package com.demas.mk4_k1_projectbiodata

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailSiswa : AppCompatActivity() {

    // Kunci untuk menerima data dari Intent (termasuk Hobi)
    companion object {
        const val EXTRA_NAMA = "extra_nama"
        const val EXTRA_ABSEN = "extra_absen"
        const val EXTRA_NIS = "extra_nis"
        const val EXTRA_GENDER = "extra_gender"
        const val EXTRA_HOBY = "extra_hoby"
        const val EXTRA_KELAS = "extra_kelas" // Ditambahkan untuk opsional display
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_siswa)

        // 1. Ambil data dari Intent
        val nama = intent.getStringExtra(EXTRA_NAMA)
        val absen = intent.getStringExtra(EXTRA_ABSEN)
        val nis = intent.getStringExtra(EXTRA_NIS)
        val gender = intent.getStringExtra(EXTRA_GENDER)
        val hobi = intent.getStringExtra(EXTRA_HOBY)
        val kelas = intent.getStringExtra(EXTRA_KELAS)

        // 2. Hubungkan View
        val tvNamaSiswa: TextView = findViewById(R.id.tvNamaSiswa)
        val tvAbsen: TextView = findViewById(R.id.tvAbsen)
        val tvNIS: TextView = findViewById(R.id.tvNIS)
        val tvGender: TextView = findViewById(R.id.tvGender)
        val tvHobi: TextView = findViewById(R.id.tvHobi)
        val tvSubtitle: TextView = findViewById(R.id.tvSubtitle) // Untuk menampilkan Kelas di header

        // 3. Tampilkan data ke View
        tvNamaSiswa.text = nama
        tvAbsen.text = absen
        tvNIS.text = nis
        tvGender.text = gender
        tvHobi.text = hobi
        tvSubtitle.text = kelas ?: "XI PPLG 4" // Gunakan data kelas untuk subtitle
    }
}