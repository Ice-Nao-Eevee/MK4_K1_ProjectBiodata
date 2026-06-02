package com.danish.aplikasibiodata_percobaan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    // Default role saat aplikasi dibuka
    private var selectedRole = "siswa"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnSiswa = findViewById<TextView>(R.id.btnSiswa)
        val btnWaliKelas = findViewById<TextView>(R.id.btnWaliKelas)
        val btnLogin = findViewById<TextView>(R.id.btnLogin)
        val btnRegister = findViewById<TextView>(R.id.btnRegister)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        // Fungsi untuk ganti UI saat tab Role diklik
        fun selectRole(role: String) {
            selectedRole = if (role == "Wali Kelas") "wali_kelas" else "siswa"

            if (selectedRole == "siswa") {
                btnSiswa.setBackgroundResource(R.drawable.role_selected_bg)
                btnSiswa.setTextColor(getColor(R.color.primary_red))
                btnWaliKelas.setBackgroundResource(R.drawable.role_unselected_bg)
                btnWaliKelas.setTextColor(getColor(R.color.text_dark))
                etEmail.hint = "Masukkan NIS atau Email"
            } else {
                btnWaliKelas.setBackgroundResource(R.drawable.role_selected_bg)
                btnWaliKelas.setTextColor(getColor(R.color.primary_red))
                btnSiswa.setBackgroundResource(R.drawable.role_unselected_bg)
                btnSiswa.setTextColor(getColor(R.color.text_dark))
                etEmail.hint = "Email Wali Kelas"
            }
        }

        btnSiswa.setOnClickListener { selectRole("Siswa") }
        btnWaliKelas.setOnClickListener { selectRole("Wali Kelas") }

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Input login dan password wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginRequest = LoginRequest(email = email, password = password)

            RetrofitClient.instance.login(loginRequest)
                .enqueue(object : Callback<AuthResponse> {
                    override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                        if (response.isSuccessful) {
                            val authResponse = response.body()
                            val user = authResponse?.user
                            val token = authResponse?.token

                            if (authResponse?.success == true && user != null && !token.isNullOrBlank()) {

                                // --- VALIDASI ROLE DINAMIS (UPDATED) ---
                                if (selectedRole == "wali_kelas") {
                                    if (user.role != "wali_kelas") {
                                        Toast.makeText(this@LoginActivity, "Akun ini bukan akun Wali Kelas!", Toast.LENGTH_LONG).show()
                                        return
                                    }
                                } else {
                                    // 🔴 ADJUSTMENT: siswa_pplg4 dilebur jadi 'siswa' atau 'umum' sesuai DB baru su
                                    if (user.role != "siswa" && user.role != "umum") {
                                        Toast.makeText(this@LoginActivity, "Gunakan tab Wali Kelas untuk login ini!", Toast.LENGTH_LONG).show()
                                        return
                                    }
                                }

                                // 🔴 SUNTIKAN 1: Ekstrak data kelas dari objek user
                                val classroom = user.classroom
                                val classroomId = classroom?.id ?: 0 // Default 0 jika akun umum
                                val className = classroom?.namaKelas ?: "Umum" // Default "Umum" jika akun umum

                                val student = user.student

                                // Simpan session lengkap ke SharedPreferences
                                getSharedPreferences("login_session", Context.MODE_PRIVATE)
                                    .edit()
                                    .putString("token", token)
                                    .putString("role", user.role)
                                    .putString("name", student?.nama ?: user.name)
                                    .putString("email", student?.email ?: user.email.orEmpty())
                                    .putString("nis", student?.nis ?: user.nis.orEmpty())
                                    .putInt("classroom_id", student?.classroomId ?: classroomId)
                                    .putString("class_name", student?.className ?: className)
                                    .putInt("student_id", student?.id ?: 0)
                                    .putInt("student_absen", student?.noAbsen ?: 0)
                                    .putString("student_gender", student?.jenisKelamin.orEmpty())
                                    .putString("student_birth_date", student?.tanggalLahir.orEmpty())
                                    .putString("student_whatsapp", student?.whatsapp.orEmpty())
                                    .putString("student_instagram", student?.instagram.orEmpty())
                                    .putString("student_bio", student?.bio.orEmpty())
                                    .putString("student_quote", student?.quote.orEmpty())
                                    .putString("student_foto", student?.foto.orEmpty())
                                    .apply()

                                Toast.makeText(this@LoginActivity, "Selamat datang, ${user.name}", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()

                            } else {
                                Toast.makeText(this@LoginActivity, authResponse?.message ?: "Login gagal atau token tidak ditemukan", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Log.e("LOGIN_API", "Error Code: ${response.code()}")
                            Toast.makeText(this@LoginActivity, "NIS/Email atau Password salah!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                        Log.e("LOGIN_API", "Failure: ${t.message}")
                        Toast.makeText(this@LoginActivity, "Server mati atau tidak terjangkau!", Toast.LENGTH_LONG).show()
                    }
                })
        }

        // Set default ke Siswa saat start
        selectRole("Siswa")
    }
}