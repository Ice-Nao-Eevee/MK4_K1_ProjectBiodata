package com.demas.mk4_k1_projectbiodata   // SESUAIKAN dgn package-mu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TampilanLogin : AppCompatActivity() {

    // username & password yang dianggap benar
    private val correctUsername = "admin"
    private val correctPassword = "12345"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tampilan_login)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin   = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // cek kosong dulu
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Username & Password wajib diisi",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // cek benar / salah
            if (username == correctUsername && password == correctPassword) {
                Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()

                // pindah ke halaman data_siswa
                val intent = Intent(this, data_siswa::class.java)
                startActivity(intent)
                finish()   // supaya tidak bisa back ke login
            } else {
                Toast.makeText(
                    this,
                    "Username atau Password salah",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
