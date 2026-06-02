package com.danish.aplikasibiodata_percobaan

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,

    @SerializedName("password_confirmation")
    val passwordConfirmation: String,

    val role: String
)
