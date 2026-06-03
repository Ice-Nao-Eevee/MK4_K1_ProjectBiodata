package com.danish.aplikasibiodata_percobaan

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class EditSiswaActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etNis: EditText
    private lateinit var etAbsen: EditText
    private lateinit var etGender: EditText
    private lateinit var etBirthDate: EditText
    private lateinit var etWhatsapp: EditText
    private lateinit var etInstagram: EditText
    private lateinit var etBio: EditText
    private lateinit var etQuote: EditText
    private lateinit var btnSave: TextView
    private lateinit var btnCancel: TextView
    private lateinit var btnSelectFoto: TextView // Tombol pilih foto baru

    private var studentId: Int = 0
    private var selectedImageUri: Uri? = null // Menampung file foto terpilih

    // LAUNCHER UNTUK BUKA GALERI HP
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            Toast.makeText(this, "Foto berhasil dipilih!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ── 🔴 SUNTIKAN KEAMANAN GAIB: VALIDASI LINTAS KELAS SEBELUM RENDER UI ──
        val prefs = getSharedPreferences("login_session", Context.MODE_PRIVATE)
        val userRole = prefs.getString("role", "umum") ?: "umum"
        val walasClassroomId = prefs.getInt("classroom_id", 0)

        // Ambil ID kelas si siswa yang dikirim lewat intent dari halaman sebelumnya
        val studentClassroomId = intent.getIntExtra("studentClassroomId", -1)

        // Cek apakah dia wali kelas sah dan apakah ID kelasnya cocok dengan kelas si murid
        if (userRole != "wali_kelas" || walasClassroomId != studentClassroomId) {
            Toast.makeText(this, "Anda tidak dapat mengedit karena anda bukan wali kelas siswa ini.", Toast.LENGTH_LONG).show()
            finish() // Tendang balik, gagalkan buka halaman edit!
            return
        }
        // ────────────────────────────────────────────────────────────────────────

        setContentView(R.layout.activity_edit_siswa)

        // Inisialisasi View ID Form sesuai XML asli lu
        etName = findViewById(R.id.etEditName)
        etNis = findViewById(R.id.etEditNis)
        etAbsen = findViewById(R.id.etEditAbsen)
        etGender = findViewById(R.id.etEditGender)
        etBirthDate = findViewById(R.id.etEditBirthDate)
        etWhatsapp = findViewById(R.id.etEditWhatsapp)
        etInstagram = findViewById(R.id.etEditInstagram)
        etBio = findViewById(R.id.etEditBio)
        etQuote = findViewById(R.id.etEditQuote)
        btnSave = findViewById(R.id.btnSaveData)
        btnCancel = findViewById(R.id.btnCancelEdit)
        btnSelectFoto = findViewById(R.id.btnPilihFotoMurid)

        // Tangkap data awal dari Fragment/Activity detail saat tombol Edit diklik
        studentId = intent.getIntExtra("studentId", 0)
        etName.setText(intent.getStringExtra("studentName"))
        etNis.setText(intent.getStringExtra("studentNis"))

        val absenData = intent.getIntExtra("studentAbsen", 0)
        etAbsen.setText(if (absenData == 0) "" else absenData.toString())

        etGender.setText(intent.getStringExtra("studentGender"))
        etBirthDate.setText(intent.getStringExtra("studentBirthDate"))
        etWhatsapp.setText(intent.getStringExtra("studentWhatsapp"))
        etInstagram.setText(intent.getStringExtra("studentInstagram"))
        etBio.setText(intent.getStringExtra("studentBio"))
        etQuote.setText(intent.getStringExtra("studentQuote"))

        // Klik aksi pilih foto dari galeri
        btnSelectFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }

        // Klik aksi simpan data
        btnSave.setOnClickListener {
            executeUploadChanges()
        }

        // Klik aksi batal kembali
        btnCancel.setOnClickListener {
            finish()
        }
    }

    // Helper untuk mengubah URI galeri menjadi File temporary agar bisa di-upload Retrofit
    private fun getFileFromUri(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(cacheDir, "temp_avatar_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    }

    private fun executeUploadChanges() {
        val updatedName = etName.text.toString().trim()
        val updatedNis = etNis.text.toString().trim()
        val updatedAbsenString = etAbsen.text.toString().trim()
        val updatedGender = etGender.text.toString().trim().uppercase()
        val updatedBirthDate = etBirthDate.text.toString().trim()
        val updatedWhatsapp = etWhatsapp.text.toString().trim()
        val updatedInstagram = etInstagram.text.toString().trim()
        val updatedBio = etBio.text.toString().trim()
        val updatedQuote = etQuote.text.toString().trim()

        if (updatedName.isEmpty() || updatedNis.isEmpty()) {
            Toast.makeText(this, "Nama dan NIS tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedAbsen = updatedAbsenString.toIntOrNull() ?: 1

        if (updatedGender != "L" && updatedGender != "P") {
            Toast.makeText(this, "Gender harus diisi huruf L atau P!", Toast.LENGTH_SHORT).show()
            return
        }

        // Animasi Loading
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menyimpan perubahan data ke Cloudinary...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        // Ambil token login session Walas
        val prefs = getSharedPreferences("login_session", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null)
        val authHeader = "Bearer $token"

        // Ubah semua tipe teks biasa menjadi RequestBody (Wajib untuk Multipart)
        val textType = "text/plain".toMediaTypeOrNull()
        val rbName = updatedName.toRequestBody(textType)
        val rbNis = updatedNis.toRequestBody(textType)
        val rbAbsen = updatedAbsen.toString().toRequestBody(textType)
        val rbGender = updatedGender.toRequestBody(textType)
        val rbBirthDate = updatedBirthDate.toRequestBody(textType)
        val rbWhatsapp = updatedWhatsapp.toRequestBody(textType)
        val rbInstagram = updatedInstagram.toRequestBody(textType)
        val rbBio = updatedBio.toRequestBody(textType)
        val rbQuote = updatedQuote.toRequestBody(textType)

        // Proses konversi file Gambar/Foto Profil
        var multipartFoto: MultipartBody.Part? = null
        selectedImageUri?.let { uri ->
            try {
                val file = getFileFromUri(uri)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                multipartFoto = MultipartBody.Part.createFormData("foto", file.name, requestFile)
            } catch (e: Exception) {
                Log.e("FILE_CONVERT_ERROR", e.message ?: "Gagal memproses gambar")
            }
        }

        // Tembak API Multipart Retrofit
        RetrofitClient.instance.updateSiswa(
            authHeader,
            studentId,
            rbName,
            rbNis,
            rbAbsen,
            rbGender,
            rbBirthDate,
            rbWhatsapp,
            rbInstagram,
            rbBio,
            rbQuote,
            multipartFoto // File foto profil Cloudinary meluncur!
        ).enqueue(object : Callback<EditSiswaResponse> {
            override fun onResponse(call: Call<EditSiswaResponse>, response: Response<EditSiswaResponse>) {
                progressDialog.dismiss()
                if (response.isSuccessful && response.body()?.success == true) {

                    // Ambil URL Foto baru yang dikembalikan oleh response body server Laravel
                    val newFotoUrl = response.body()?.data?.foto ?: ""

                    // Titipkan semua sisa field data baru ke cache lokal SharedPreferences
                    val syncPrefs = getSharedPreferences("edit_temp_sync", Context.MODE_PRIVATE)
                    syncPrefs.edit().apply {
                        putInt("sync_id", studentId)
                        putString("sync_name", updatedName)
                        putString("sync_nis", updatedNis)
                        putInt("sync_absen", updatedAbsen)
                        putString("sync_gender", updatedGender)
                        putString("sync_birthdate", updatedBirthDate)
                        putString("sync_whatsapp", updatedWhatsapp)
                        putString("sync_instagram", updatedInstagram)
                        putString("sync_bio", updatedBio)
                        putString("sync_quote", updatedQuote)

                        if (newFotoUrl.isNotEmpty()) {
                            putString("sync_foto", newFotoUrl)
                        }
                        apply()
                    }

                    Toast.makeText(this@EditSiswaActivity, "Data Siswa Berhasil Diperbarui!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    try {
                        val errorBodyString = response.errorBody()?.string()
                        Log.e("LARAVEL_ERROR_RAW", errorBodyString ?: "Kosong")

                        val jsonObject = JSONObject(errorBodyString ?: "{}")
                        val serverMessage = jsonObject.optString("message", "Gagal memperbarui data")

                        Toast.makeText(this@EditSiswaActivity, "Server: $serverMessage", Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@EditSiswaActivity, "Eror HTTP Kode: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<EditSiswaResponse>, t: Throwable) {
                progressDialog.dismiss()
                Log.e("EDIT_SISWA_FAIL", t.message ?: "Network Error")
                Toast.makeText(this@EditSiswaActivity, "Koneksi Bermasalah: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}