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
            // NAMA //                       // ABSEN //      // NIS (SUDAH DISESUAIKAN) //  // KELAS //                // GENDER //        // HOBI //      // FOTO PROFIL //
            Siswa(nama = "ABSARI BEKTI SYAHFITRI", absen = "Absen: 1", nis = "NIS: 541241003", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = "", foto = R.drawable.absari), // 1
            Siswa(nama = "ADARA AURORA KUSUMA", absen = "Absen: 2", nis = "NIS: 541241001", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = "", foto = R.drawable.adara), // 2
            Siswa(nama = "AFFANDO FATHINATHA", absen = "Absen: 3", nis = "NIS: 541241009", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "", foto = R.drawable.affan), // 3
            Siswa(nama = "BIMA VALIANT ALENTRA WIMBO", absen = "Absen: 4", nis = "NIS: 541241033", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "", foto = R.drawable.bima), // 4
            Siswa(nama = "BINTANG NABA AL HAKIM", absen = "Absen: 5", nis = "NIS: 541241034", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "", foto = R.drawable.bintang), // 5
            Siswa(nama = "DANISH ADELIO", absen = "Absen: 6", nis = "NIS: 541241042", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "", foto = R.drawable.danish), // 6
            Siswa(nama = "DEMAS BANYU BIRU", absen = "Absen: 7", nis = "NIS: 541241045", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "", foto = R.drawable.demas), // 7
            Siswa(nama = "DORETHA GELSEY ANEZKA", absen = "Absen: 8", nis = "NIS: 541241050", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = "", foto = R.drawable.doretha), // 8
            Siswa(nama = "EVAN FA'ADILLAH PRAWIDYA", absen = "Absen: 9", nis = "NIS: 541241056", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "", foto = R.drawable.evan), // 9
            Siswa(nama = "FABIAN ROZAN FANANI", absen = "Absen: 10", nis = "NIS: 541241059", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "", foto = R.drawable.fabian), // 10
            Siswa(nama = "FADHIL REKH SAPUTRA", absen = "Absen: 11", nis = "NIS: 541241062", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "", foto = R.drawable.fadhil), // 11
            Siswa(nama = "FAIRUZ HIDAYAT", absen = "Absen: 12", nis = "NIS: 541241064", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "", foto = R.drawable.fairuz), // 12
            Siswa(nama = "FIRMAN NOOR ADI NUGROHO", absen = "Absen: 13", nis = "NIS: 541241074", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "", foto = R.drawable.firman), // 13
            Siswa(nama = "HAJAR ASSYIFA ADHEAZASMI", absen = "Absen: 14", nis = "NIS: 541241082", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = "", foto = R.drawable.syifa), // 14
            Siswa(nama = "IMTIYAZ FADHILAH 'AZMI", absen = "Absen: 15", nis = "NIS: 541241091", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = "", foto = R.drawable.imtiyaz), // 15
            Siswa(nama = "KHAFIDZ RIZQI IKHSANI", absen = "Absen: 16", nis = "NIS: 541241106", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "", foto = R.drawable.khafidz), // 16
            Siswa(nama = "MEZZALUNA AZZAFIRA", absen = "Absen: 17", nis = "NIS: 541241125", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = "", foto = R.drawable.mezzaluna), // 17
            Siswa(nama = "MUHAMMAD ASHRAF AURAVVYANO SAKA", absen = "Absen: 18", nis = "NIS: 541241132", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "", foto = R.drawable.asraf), // 18
            Siswa(nama = "MUHAMMAD ROFIQ HIDAYAT", absen = "Absen: 19", nis = "NIS: 541241142", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "", foto = R.drawable.rofiq), // 19
            Siswa(nama = "NAIFA ASHILA HANDOYO", absen = "Absen: 20", nis = "NIS: 541241149", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = "", foto = R.drawable.naifa), // 20
            Siswa(nama = "NAWAF GADI ALFATIH", absen = "Absen: 21", nis = "NIS: 541241152", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "", foto = R.drawable.nawaf), // 21
            Siswa(nama = "QUEENA AISYA PRASETYAWAN", absen = "Absen: 22", nis = "NIS: 541241159", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = "", foto = R.drawable.queen), // 22
            Siswa(nama = "RAFI IBRAHIM", absen = "Absen: 23", nis = "NIS: 541241161", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "", foto = R.drawable.rafi), // 23
            Siswa(nama = "RAJA FIDHIAZKA PRATAMA", absen = "Absen: 24", nis = "NIS: 541241164", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "", foto = R.drawable.raja), // 24
            Siswa(nama = "RAZYA FAHMI AFRIANTO", absen = "Absen: 25", nis = "NIS: 541241168", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "", foto = R.drawable.razya), // 25
            Siswa(nama = "RIZKY MADYACHANDRA RAMADHAN", absen = "Absen: 26", nis = "NIS: 541231192", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = "", foto = R.drawable.chandra), // 26
            Siswa(nama = "RONA MIFTAHULJANNAH", absen = "Absen: 27", nis = "NIS: 541241171", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = "", foto = R.drawable.rona), // 27
            Siswa(nama = "SASKIA SYIFA SALSABILA", absen = "Absen: 28", nis = "NIS: 541241178", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = "", foto = R.drawable.saski), // 28
            Siswa(nama = "SIAM AL SOBARI", absen = "Absen: 29", nis = "NIS: 541241185", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "", foto = R.drawable.siam), // 29
            Siswa(nama = "TANISHA NADIA HANZ", absen = "Absen: 30", nis = "NIS: 541241190", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = "", foto = R.drawable.tanisha), // 30
            Siswa(nama = "TIYAS AYU LESTARI", absen = "Absen: 31", nis = "NIS: 541241191", kelas = "Kelas: XI PPLG 4", gender = "Perempuan", hobi = "", foto = R.drawable.tiyas), // 31
            Siswa(nama = "WISNU SATRIA SUJATMIKO", absen = "Absen: 32", nis = "NIS: 541241198", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "", foto = R.drawable.wisnu), // 32
            Siswa(nama = "WIWEKO SINDUDI", absen = "Absen: 33", nis = "NIS: 541241199", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "", foto = R.drawable.wiweko), // 33
            Siswa(nama = "YUDHISTIRA", absen = "Absen: 34", nis = "NIS: 541201201", kelas = "Kelas: XI PPLG 4", gender = "Laki-laki", hobi = "", foto = R.drawable.yudhistira) // 34
        )

        val adapter = SiswaAdapter(listSiswa)
        rvSiswa.adapter = adapter
    }
}