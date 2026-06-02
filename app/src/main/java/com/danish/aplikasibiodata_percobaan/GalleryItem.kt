package com.danish.aplikasibiodata_percobaan

import com.google.gson.annotations.SerializedName

data class GalleryItem(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("caption") // Sesuaikan dengan JSON Laravel: "caption"
    val caption: String? = "Tanpa Judul",

    @SerializedName("image_url") // Pakai image_url agar link-nya lengkap
    val imageUrl: String = "",

    @SerializedName("user") // Tambahin ini biar bisa tau siapa yang upload
    val user: UserGallery? = null
)

data class UserGallery(
    @SerializedName("name")
    val name: String = ""
)