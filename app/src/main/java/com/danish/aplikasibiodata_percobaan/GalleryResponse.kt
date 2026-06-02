package com.danish.aplikasibiodata_percobaan

import com.google.gson.annotations.SerializedName

data class GalleryResponse(
    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("data")
    val data: List<GalleryItem> = emptyList()
)