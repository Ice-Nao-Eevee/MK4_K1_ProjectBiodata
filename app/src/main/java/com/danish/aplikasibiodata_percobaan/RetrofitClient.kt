package com.danish.aplikasibiodata_percobaan

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Emulator Android Studio memakai 10.0.2.2 untuk mengakses localhost laptop.
    // Kalau pakai HP asli, ganti ke IP laptop kamu, contoh: http://192.168.1.10:8000/api/
    private const val BASE_URL = "http://192.168.1.17:8000/api/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
