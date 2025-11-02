package com.demas.mk4_k1_projectbiodata

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TampilanLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tampilan_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // TAMBAHKAN KODE INI 👇
        val txtEmail = findViewById<EditText>(R.id.txtemail)
        val txtPassword = findViewById<EditText>(R.id.txtpassword)
        val btnLogin = findViewById<Button>(R.id.btnlogin) // sesuaikan dengan id tombol login kamu

        btnLogin.setOnClickListener {
            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()

            if (email == "demas@gmail.com" && password == "demas08") {
                val intent = Intent(this, data_siswa::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Email atau Password salah!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}