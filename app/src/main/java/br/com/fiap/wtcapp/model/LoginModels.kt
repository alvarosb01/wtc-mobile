package br.com.fiap.wtcapp.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    @SerializedName("password") val senha: String
)

data class LoginResponse(
    val token: String,
    val role: String, // Garanta que o Java envie "OPERADOR" ou "CLIENTE"
    val userId: String
)