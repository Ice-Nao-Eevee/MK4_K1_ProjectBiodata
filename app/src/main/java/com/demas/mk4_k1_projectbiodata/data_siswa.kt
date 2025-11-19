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

        // Data 34 Siswa sudah disesuaikan dengan yang ada di foto (Absen & NIS sudah sinkron)
        val listSiswa = listOf(
            // NAMA //                       // ABSEN //      // NIS (SUDAH DISESUAIKAN) //  // KELAS //                // GENDER //        // HOBI //
            Siswa(nama = "ABSARI BEKTI SYAHFITRI", absen = "Absen: 1", nis = "NIS: 541241003", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = ""),
            Siswa(nama = "ADARA AURORA KUSUMA", absen = "Absen: 2", nis = "NIS: 541241001", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = ""),
            Siswa(nama = "AFFANDO FATHINATHA", absen = "Absen: 3", nis = "NIS: 541241009", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = ""),
            Siswa(nama = "BIMA VALIANT ALENTRA WIMBO", absen = "Absen: 4", nis = "NIS: 541241033", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = ""),
            Siswa(nama = "BINTANG NABA AL HAKIM", absen = "Absen: 5", nis = "NIS: 541241034", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = ""),
            Siswa(nama = "DANISH ADELIO", absen = "Absen: 6", nis = "NIS: 541241042", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = ""),
            Siswa(nama = "DEMAS BANYU BIRU", absen = "Absen: 7", nis = "NIS: 541241045", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = ""),
            Siswa(nama = "DORETHA GELSEY ANEZKA", absen = "Absen: 8", nis = "NIS: 541241050", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = ""),
            Siswa(nama = "EVAN FA'ADILLAH PRAWIDYA", absen = "Absen: 9", nis = "NIS: 541241056", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = ""),
            Siswa(nama = "FABIAN ROZAN FANANI", absen = "Absen: 10", nis = "NIS: 541241059", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = ""),
            Siswa(nama = "FADHIL REKH SAPUTRA", absen = "Absen: 11", nis = "NIS: 541241062", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = ""),
            Siswa(nama = "FAIRUZ HIDAYAT", absen = "Absen: 12", nis = "NIS: 541241064", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = ""),
            Siswa(nama = "FIRMAN NOOR ADI NUGROHO", absen = "Absen: 13", nis = "NIS: 541241074", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = ""),
            Siswa(nama = "HAJAR ASSYIFA ADHEAZASMI", absen = "Absen: 14", nis = "NIS: 541241082", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = ""),
            Siswa(nama = "IMTIYAZ FADHILAH 'AZMI", absen = "Absen: 15", nis = "NIS: 541241091", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = ""),
            Siswa(nama = "KHAFIDZ RIZQI IKHSANI", absen = "Absen: 16", nis = "NIS: 541241106", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = ""),
            Siswa(nama = "MEZZALUNA AZZAFIRA", absen = "Absen: 17", nis = "NIS: 541241125", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = ""),
            Siswa(nama = "MUHAMMAD ASHRAF AURAVVYANO SAKA", absen = "Absen: 18", nis = "NIS: 541241132", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = ""),
            Siswa(nama = "MUHAMMAD ROFIQ HIDAYAT", absen = "Absen: 19", nis = "NIS: 541241142", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = ""),
            Siswa(nama = "NAIFA ASHILA HANDOYO", absen = "Absen: 20", nis = "NIS: 541241149", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = ""),
            Siswa(nama = "NAWAF GADI ALFATIH", absen = "Absen: 21", nis = "NIS: 541241152", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = ""),
            Siswa(nama = "QUEENA AISYA PRASETYAWAN", absen = "Absen: 22", nis = "NIS: 541241159", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = ""),
            Siswa(nama = "RAFI IBRAHIM", absen = "Absen: 23", nis = "NIS: 541241161", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = ""),
            Siswa(nama = "RAJA FIDHIAZKA PRATAMA", absen = "Absen: 24", nis = "NIS: 541241164", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = ""),
            Siswa(nama = "RAZYA FAHMI AFRIANTO", absen = "Absen: 25", nis = "NIS: 541241168", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = ""),
            Siswa(nama = "RIZKY MADYACHANDRA RAMADHAN", absen = "Absen: 26", nis = "NIS: 541231192", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = ""),
            Siswa(nama = "RONA MIFTAHULJANNAH", absen = "Absen: 27", nis = "NIS: 541241171", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = ""),
            Siswa(nama = "SASKIA SYIFA SALSABILA", absen = "Absen: 28", nis = "NIS: 541241178", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = ""),
            Siswa(nama = "SIAM AL SOBARI", absen = "Absen: 29", nis = "NIS: 541241185", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = ""),
            Siswa(nama = "TANISHA NADIA HANZ", absen = "Absen: 30", nis = "NIS: 541241190", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = ""),
            Siswa(nama = "TIYAS AYU LESTARI", absen = "Absen: 31", nis = "NIS: 541241191", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = ""),
            Siswa(nama = "WISNU SATRIA SUJATMIKO", absen = "Absen: 32", nis = "NIS: 541241198", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = ""),
            Siswa(nama = "WIWEKO SINDUDI", absen = "Absen: 33", nis = "NIS: 541241199", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = ""),
            Siswa(nama = "YUDHISTIRA", absen = "Absen: 34", nis = "NIS: 541201201", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "")
        )

        val adapter = SiswaAdapter(listSiswa)
        rvSiswa.adapter = adapter
    }
}