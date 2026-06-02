package com.danish.aplikasibiodata_percobaan

import com.google.gson.annotations.SerializedName

data class SiswaResponse(
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("viewer_role") val viewerRole: String? = null,
    @SerializedName("data") val data: List<SiswaApiItem>? = null
)

data class SiswaApiItem(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("no_absen") val noAbsen: Int? = null,
    @SerializedName("nis") val nis: String? = null,
    @SerializedName("nama") val nama: String? = null,
    @SerializedName("jenis_kelamin") val jenisKelamin: String? = null,
    @SerializedName("jabatan_dev") val jabatanDev: String? = null,
    @SerializedName("foto") val foto: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("tanggal_lahir") val tanggalLahir: String? = null,
    @SerializedName("whatsapp") val whatsapp: String? = null,
    @SerializedName("instagram") val instagram: String? = null,
    @SerializedName("bio") val bio: String? = null,
    @SerializedName("quote") val quote: String? = null,
    @SerializedName("nama_ayah") val namaAyah: String? = null,
    @SerializedName("nama_ibu") val namaIbu: String? = null,
    @SerializedName("is_sensitive_hidden") val isSensitiveHidden: Boolean? = null,
    @SerializedName("privacy_notice") val privacyNotice: String? = null,
    @SerializedName("classroom_id") val classroomId: Int? = null,
    @SerializedName("classroom") val classroom: ClassroomResponse? = null
)
