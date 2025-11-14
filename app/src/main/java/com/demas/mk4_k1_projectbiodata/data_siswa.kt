package com.demas.mk4_k1_projectbiodata   // samakan dengan package kamu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class data_siswa : AppCompatActivity() {

    private lateinit var rvSiswa: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_siswa)

        rvSiswa = findViewById(R.id.rvSiswa)
        rvSiswa.layoutManager = LinearLayoutManager(this)

        // CONTOH 5 DATA (ganti kalau mau 34 lagi)
        val listSiswa = listOf(
            Siswa(nama = "Absari", absen = "Absen: 1", nis = "NIS: 541241045", kelas = "Kelas: XI PPLG 4"),
            Siswa(nama = "Budi",   absen = "Absen: 2", nis = "NIS: 541241046", kelas = "Kelas: XI PPLG 4"),
            Siswa(nama = "Citra",  absen = "Absen: 3", nis = "NIS: 541241047", kelas = "Kelas: XI PPLG 4"),
            Siswa(nama = "Dimas",  absen = "Absen: 4", nis = "NIS: 541241048", kelas = "Kelas: XI PPLG 4"),
            Siswa(nama = "Eka",    absen = "Absen: 5", nis = "NIS: 541241049", kelas = "Kelas: XI PPLG 4"),
        )

        val adapter = SiswaAdapter(listSiswa)
        rvSiswa.adapter = adapter
    }
}
