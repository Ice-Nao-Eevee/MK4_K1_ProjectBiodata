package com.danish.aplikasibiodata_percobaan

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String? = null,
    @SerializedName("token") val token: String? = null,
    @SerializedName("user") val user: UserData? = null
)

data class UserData(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String = "Pengguna",
    @SerializedName("email") val email: String? = "",
    @SerializedName("nis") val nis: String? = null,
    @SerializedName("role") val role: String = "umum",
    @SerializedName("classroom") val classroom: ClassroomResponse? = null,
    @SerializedName("student") val student: LoggedInStudentData? = null
)

data class ClassroomResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("tingkat") val tingkat: String? = null,
    @SerializedName("jurusan") val jurusan: String? = null,
    @SerializedName("nomor_kelas") val nomorKelas: Int? = null,
    @SerializedName("nama_kelas") val namaKelas: String? = null
)

data class LoggedInStudentData(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("no_absen") val noAbsen: Int? = null,
    @SerializedName("nis") val nis: String? = null,
    @SerializedName("nama") val nama: String? = null,
    @SerializedName("jenis_kelamin") val jenisKelamin: String? = null,
    @SerializedName("tanggal_lahir") val tanggalLahir: String? = null,
    @SerializedName("whatsapp") val whatsapp: String? = null,
    @SerializedName("instagram") val instagram: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("bio") val bio: String? = null,
    @SerializedName("quote") val quote: String? = null,
    @SerializedName("foto") val foto: String? = null,
    @SerializedName("classroom_id") val classroomId: Int? = null,
    @SerializedName("class_name") val className: String? = null
)
