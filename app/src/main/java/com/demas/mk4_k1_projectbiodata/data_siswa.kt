package com.demas.mk4_k1_projectbiodata

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class data_siswa : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_siswa)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // DATA 34 SISWA (Ganti dengan data asli kamu!)
        val listSiswa = listOf(
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //1
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //2
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //3
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //4
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //5
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //6
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //7
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //8
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //9
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //10
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //11
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //12
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //13
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //14
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //15
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //16
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //17
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //18
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //19
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //20
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //21
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //22
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //23
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //24
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //25
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //26
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //27
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //28
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //29
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //30
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //31
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //32
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //33
            Siswa("Absari", "Absen: 1", "NIS: 541241045", "Kelas: XI PPLG 4"), //34
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SiswaAdapter(listSiswa)
    }
}