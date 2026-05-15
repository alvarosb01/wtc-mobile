package br.com.fiap.wtcapp.network

import br.com.fiap.wtcapp.model.LoginRequest
import br.com.fiap.wtcapp.model.LoginResponse
import br.com.fiap.wtcapp.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Body

interface AuthApiService {
    @POST("api/v1/auth/login") // Ajuste para o seu endpoint exato do Spring
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // ADICIONE ESTA LINHA:
    @POST("api/v1/auth/register") // Verifique se o caminho no seu Java é esse mesmo
    suspend fun registrarUsuario(@Body request: RegisterRequest): Response<Unit>
}