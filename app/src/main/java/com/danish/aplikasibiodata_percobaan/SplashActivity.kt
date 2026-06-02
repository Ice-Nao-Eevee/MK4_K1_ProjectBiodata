package com.danish.aplikasibiodata_percobaan

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // MODIF: Mengubah durasi postDelayed dari 1000 menjadi 3000 (3 detik)
        Handler(Looper.getMainLooper()).postDelayed({
            val prefs = getSharedPreferences("login_session", MODE_PRIVATE)
            val token = prefs.getString("token", null)

            // FIX LOGIC: Cukup cek apakah token ada atau tidak.
            // Jika token tidak null/kosong, langsung izinkan masuk ke MainActivity tanpa mental.
            val destination = if (!token.isNullOrBlank()) {
                MainActivity::class.java
            } else {
                prefs.edit().clear().apply()
                LoginActivity::class.java
            }

            val intent = Intent(this, destination)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }, 3000) // <-- Angka diganti 3000 biar mejengnya agak lamaan dan elegan
    }
}