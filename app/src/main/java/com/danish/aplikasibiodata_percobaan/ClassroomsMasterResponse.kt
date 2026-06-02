package com.danish.aplikasibiodata_percobaan

import com.google.gson.annotations.SerializedName

data class ClassroomsMasterResponse(
    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: List<ClassroomItem>? = null // Menampung list array 35 kelas dari Laravel
)

data class ClassroomItem(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("tingkat")
    val tingkat: String? = null,

    @SerializedName("jurusan")
    val jurusan: String? = null,

    @SerializedName("nama_kelas")
    val namaKelas: String? = null
)