package com.demas.mk4_k1_projectbiodata

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailSiswa : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_siswa)

        // Ambil data dari intent
        val nama = intent.getStringExtra("NAMA") ?: "Tidak ada data"
        val absen = intent.getStringExtra("ABSEN") ?: "Tidak ada data"
        val nis = intent.getStringExtra("NIS") ?: "Tidak ada data"
        val gender = intent.getStringExtra("GENDER") ?: "Tidak ada data"
        val hobi = intent.getStringExtra("HOBI") ?: ""
        val foto = intent.getIntExtra("FOTO", R.drawable.ic_launcher_foreground)  // TAMBAHAN

        // Tampilkan foto
        findViewById<ImageView>(R.id.imgDetailFoto).setImageResource(foto)  // TAMBAHAN

        // Tampilkan data
        findViewById<TextView>(R.id.tvDetailNama).text = nama
        findViewById<TextView>(R.id.tvDetailAbsen).text = absen
        findViewById<TextView>(R.id.tvDetailNis).text = nis
        findViewById<TextView>(R.id.tvDetailGender).text = gender

        // Tampilkan hobi jika ada
        if (hobi.isNotEmpty()) {
            findViewById<TextView>(R.id.tvDetailHobi).apply {
                text = hobi
                visibility = View.VISIBLE
            }
            findViewById<TextView>(R.id.labelHobi).visibility = View.VISIBLE
            findViewById<View>(R.id.dividerHobi).visibility = View.VISIBLE
        }
    }
}