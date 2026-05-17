package br.com.fiap.wtcapp.model

import com.google.gson.annotations.SerializedName

// Essa é a classe que estava faltando!
data class RegisterRequest(
    val email: String,
    @SerializedName("password") val senha: String, // O GSON vai converter para 'password' pro seu Java
    val role: String
)