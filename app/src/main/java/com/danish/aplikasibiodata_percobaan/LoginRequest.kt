package com.danish.aplikasibiodata_percobaan

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    // Anotasi ini bertugas nembak key "login_input" ke JSON Laravel lo
    @SerializedName("login_input")
    val email: String,
    val password: String
)