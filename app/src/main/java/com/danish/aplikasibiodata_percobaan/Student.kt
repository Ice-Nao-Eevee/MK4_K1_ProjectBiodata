package com.danish.aplikasibiodata_percobaan

import com.google.gson.annotations.SerializedName

data class Student(
    @SerializedName("id") val id: Int,
    @SerializedName("nis") val nis: String,
    @SerializedName("nama") val name: String,
    @SerializedName("no_absen") val absen: Int,
    @SerializedName("jenis_kelamin") val gender: String,
    @SerializedName("jabatan_dev") val role: String,
    @SerializedName("classroom_id") val classroomId: Int = 0,
    val birthDate: String = "",
    val whatsapp: String = "",
    val instagram: String = "",
    val email: String = "",
    var className: String = "Belum Pilih Kelas",
    val category: String = "Siswa",
    val tags: List<Tag> = emptyList(),
    val bio: String = "",
    val projects: Int = 0,
    val commits: Int = 0,
    val activityRole: String = "",
    val quote: String = "",
    val avatarRes: Int = R.drawable.ic_avatar_placeholder,
    val fotoUrl: String = "",
    val namaAyah: String = "",
    val namaIbu: String = "",
    val isSensitiveHidden: Boolean = false,
    val privacyNotice: String = ""
)

data class Tag(
    val label: String,
    val bgColorRes: Int,
    val textColorRes: Int
)
