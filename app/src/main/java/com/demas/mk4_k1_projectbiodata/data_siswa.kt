package com.demas.mk4_k1_projectbiodata

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class data_siswa : AppCompatActivity() {

    private lateinit var siswaAdapter: SiswaAdapter // Deklarasi adapter di luar agar bisa diakses

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_siswa)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val etSearch = findViewById<EditText>(R.id.etSearch) // Menghubungkan EditText

        // DATA 34 SISWA KELAS XI PPLG 4 (gunakan daftar lengkap yang sudah kita buat)
        val listSiswa = listOf(
            Siswa("ABSARI BEKTI SYAHFITRI", "1", "541241001", "XI PPLG 4", "P", "Mendengarkan Musik"),
            Siswa("ADARA AURORA KUSUMA", "2", "541241003", "XI PPLG 4", "P", "Menulis Cerpen"),
            Siswa("AFFANDO FATHINATHA", "3", "541241009", "XI PPLG 4", "L", "Sepak Bola"),
            Siswa("BIMA VALIANT ALENTRA WIMBO", "4", "541241033", "XI PPLG 4", "L", "Bermain Game"),
            Siswa("BINTANG NABA AL HAKIM", "5", "541241034", "XI PPLG 4", "L", "Catur"),
            Siswa("DANISH ADELIO", "6", "541241042", "XI PPLG 4", "L", "Renang"),
            Siswa("DEMAS BANYU BIRU", "7", "541241045", "XI PPLG 4", "L", "Ngoding"), // Contoh Anda
            Siswa("DORETHA GELSEY ANEZKA", "8", "541241050", "XI PPLG 4", "P", "Menggambar"),
            Siswa("EVAN FA'ADILLAH PRAWIDYA", "9", "541241056", "XI PPLG 4", "L", "Bulu Tangkis"),
            Siswa("FABIAN ROZAN FANANI", "10", "541241059", "XI PPLG 4", "L", "Futsal"),
            Siswa("FADHIL REKH SAPUTRA", "11", "541241062", "XI PPLG 4", "L", "Memasak"),
            Siswa("FAIRUZ HIDAYAT", "12", "541241064", "XI PPLG 4", "L", "Fotografi"),
            Siswa("FIRMAN NOOR ADI NUGROHO", "13", "541241074", "XI PPLG 4", "L", "Bermain Musik"),
            Siswa("HAJAR ASSYIFA ADHEAZASMI", "14", "541241082", "XI PPLG 4", "P", "Membaca Buku"),
            Siswa("IMTIYAZ FADHILAH 'AZMI", "15", "541241091", "XI PPLG 4", "P", "Menonton Film"),
            Siswa("KHAFIDZ RIZIQ IKHSANI", "16", "541241106", "XI PPLG 4", "L", "Game Strategi"),
            Siswa("MEZZALUNA AZZAFIRA", "17", "541241125", "XI PPLG 4", "P", "Menari Tradisional"),
            Siswa("MUHAMMAD ASHRAF AURAVYANO SAKA", "18", "541241132", "XI PPLG 4", "L", "Workout"),
            Siswa("MUHAMMAD ROFIQ HIDAYAT", "19", "541241142", "XI PPLG 4", "L", "Sepeda Santai"),
            Siswa("NAIFA ASHILA HANDOYO", "20", "541241149", "XI PPLG 4", "P", "Jalan-Jalan"),
            Siswa("NAWAF GADI ALFATIH", "21", "541241152", "XI PPLG 4", "L", "Badminton"),
            Siswa("QUEENA AISYA PRASETYAWAN", "22", "541241159", "XI PPLG 4", "P", "Bermain Skateboard"),
            Siswa("RAFI IBRAHIM", "23", "541241161", "XI PPLG 4", "L", "Membaca Komik"),
            Siswa("RAJA FIDHIAZKA PRATAMA", "24", "541241164", "XI PPLG 4", "L", "Memancing"),
            Siswa("RAZYA FAHMI AFRIANTO", "25", "541241168", "XI PPLG 4", "L", "Mendaki Gunung"),
            Siswa("RIZKY MADYACHANDRA RAMADHAN", "26", "541231192", "XI PPLG 4", "L", "Membuat Konten"),
            Siswa("RONA MIFTAHULJANNAH", "27", "541241171", "XI PPLG 4", "P", "Menonton Anime"),
            Siswa("SASKIA SYIFA SALSABILA", "28", "541241178", "XI PPLG 4", "P", "Membuat Kerajinan"),
            Siswa("SIAM AL SOBARI", "29", "541241185", "XI PPLG 4", "L", "Kulineran"),
            Siswa("TANISHA NADIA HANZ", "30", "541241190", "XI PPLG 4", "P", "Menyanyi"),
            Siswa("TIYAS AYU LESTARI", "31", "541241191", "XI PPLG 4", "P", "Berkebun"),
            Siswa("WISNU SATRIA SUJATMIKO", "32", "541241198", "XI PPLG 4", "L", "Main Basket"),
            Siswa("WIWEKO SINDUADI", "33", "541241199", "XI PPLG 4", "L", "Tulis Menulis"),
            Siswa("YUDHISTIRA", "34", "541241201", "XI PPLG 4", "L", "Tidur") // Contoh Anda
        )

        siswaAdapter = SiswaAdapter(listSiswa) // Inisialisasi adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = siswaAdapter

        // Tambahkan TextWatcher untuk memicu pencarian
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Tidak melakukan apa-apa sebelum teks berubah
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Panggil fungsi filter pada adapter setiap kali teks berubah
                siswaAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // Tidak melakukan apa-apa setelah teks berubah
            }
        })
    }
}