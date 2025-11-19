package com.demas.mk4_k1_projectbiodata

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailSiswa : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_siswa)

        // Ambil data dari intent
        val nama = intent.getStringExtra("NAMA")
        val absen = intent.getStringExtra("ABSEN")
        val nis = intent.getStringExtra("NIS")
        val kelas = intent.getStringExtra("KELAS")

        // Tampilkan data
        findViewById<TextView>(R.id.tvDetailNama).text = "Nama: $nama"
        findViewById<TextView>(R.id.tvDetailAbsen).text = absen
        findViewById<TextView>(R.id.tvDetailNis).text = nis
        findViewById<TextView>(R.id.tvDetailKelas).text = kelas
    }
}