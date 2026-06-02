package com.danish.aplikasibiodata_percobaan

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

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etName = findViewById<EditText>(R.id.etRegisterName)
        val etEmail = findViewById<EditText>(R.id.etRegisterEmail)
        val etPassword = findViewById<EditText>(R.id.etRegisterPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnCreateAccount = findViewById<TextView>(R.id.btnCreateAccount)
        val btnBackToLogin = findViewById<TextView>(R.id.btnBackToLogin)

        btnCreateAccount.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(
                    this,
                    "Nama, email, password, dan confirm password wajib diisi",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Format email belum valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 8) {
                Toast.makeText(this, "Password minimal 8 karakter", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Confirm password tidak sama", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val registerRequest = RegisterRequest(
                name = name,
                email = email,
                password = password,
                passwordConfirmation = confirmPassword,
                role = "siswa"
            )

            RetrofitClient.instance.register(registerRequest)
                .enqueue(object : Callback<AuthResponse> {
                    override fun onResponse(
                        call: Call<AuthResponse>,
                        response: Response<AuthResponse>
                    ) {
                        if (!response.isSuccessful) {
                            val errorText = response.errorBody()?.string().orEmpty()
                            Log.e("REGISTER_API", "HTTP ${response.code()} error: $errorText")
                            Toast.makeText(
                                this@RegisterActivity,
                                "Register gagal: ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }

                        val authResponse = response.body()
                        val token = authResponse?.token
                        val user = authResponse?.user

                        if (authResponse?.success == true && !token.isNullOrBlank()) {
                            getSharedPreferences("login_session", MODE_PRIVATE)
                                .edit()
                                .putString("token", token)
                                .putInt("session_version", 2)
                                .putString("role", user?.role ?: "siswa")
                                .putString("name", user?.name ?: name)
                                .putString("email", user?.email ?: email)
                                .apply()

                            Toast.makeText(
                                this@RegisterActivity,
                                "Register berhasil",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                authResponse?.message ?: "Register gagal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                        Log.e("REGISTER_API", "Failure: ${t.message}", t)
                        Toast.makeText(
                            this@RegisterActivity,
                            "Koneksi gagal: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }

        btnBackToLogin.setOnClickListener { finish() }
    }
}
