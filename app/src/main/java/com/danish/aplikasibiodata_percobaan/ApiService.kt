package com.danish.aplikasibiodata_percobaan

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // --- AUTH SECTION ---
    @POST("login")
    fun login(
        @Body request: LoginRequest,
        @Header("Accept") accept: String = "application/json"
    ): Call<AuthResponse>

    @POST("register")
    fun register(
        @Body request: RegisterRequest,
        @Header("Accept") accept: String = "application/json"
    ): Call<AuthResponse>

    @POST("logout")
    fun logout(
        @Header("Authorization") authorization: String,
        @Header("Accept") accept: String = "application/json"
    ): Call<AuthResponse>

    @GET("me")
    fun me(
        @Header("Authorization") authorization: String,
        @Header("Accept") accept: String = "application/json"
    ): Call<AuthResponse>

    // 🔴 SUNTIKAN SAKTI: Endpoint buat narik list kelas se-sekolahan cong!
    @GET("classrooms")
    fun getClassrooms(
        @Query("tingkat") tingkat: String? = null,
        @Query("jurusan") jurusan: String? = null,
        @Header("Accept") accept: String = "application/json"
    ): Call<ClassroomsMasterResponse>


    // --- DATA SECTION ---
    @GET("siswa")
    fun getStudents(
        @Header("Authorization") authorization: String,
        @Query("classroom_id") classroomId: Int, // 🟢 SUNTIKAN SAKTI 100% SUKSES DI SINI SU!
        @Header("Accept") accept: String = "application/json"
    ): Call<SiswaResponse>

    // 🛠️ MODIFIKASI SAKTI: Diubah dari @FormUrlEncoded @PUT menjadi @Multipart @POST
    @Multipart
    @POST("siswa/update/{id}")
    fun updateSiswa(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int,
        @Part("nama") nama: RequestBody,
        @Part("nis") nis: RequestBody,
        @Part("no_absen") noAbsen: RequestBody,
        @Part("jenis_kelamin") jenisKelamin: RequestBody,
        @Part("tanggal_lahir") tanggalLahir: RequestBody,
        @Part("whatsapp") whatsapp: RequestBody,
        @Part("instagram") instagram: RequestBody,
        @Part("bio") bio: RequestBody,
        @Part("quote") quote: RequestBody,
        @Part foto: MultipartBody.Part?,
        @Header("Accept") accept: String = "application/json"
    ): Call<EditSiswaResponse>

    // --- SCHEDULE SECTION ---
    @GET("jadwal/sekarang")
    fun getCurrentSchedule(
        @Query("kelas") kelas: String,
        @Header("Accept") accept: String = "application/json"
    ): Call<CurrentScheduleResponse>

    // --- GALLERY SECTION ---
    @GET("gallery")
    fun getGalleries(
        @Header("Authorization") authorization: String,
        @Header("Accept") accept: String = "application/json"
    ): Call<GalleryResponse>

    @Multipart
    @POST("gallery")
    fun uploadGallery(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @Part("caption") caption: RequestBody,
        @Header("Accept") accept: String = "application/json"
    ): Call<GalleryResponse>

    @DELETE("gallery/{id}")
    fun deleteGallery(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int,
        @Header("Accept") accept: String = "application/json"
    ): Call<GalleryResponse>
}