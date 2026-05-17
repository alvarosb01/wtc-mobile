package br.com.fiap.wtcapp.network

import br.com.fiap.wtcapp.model.MessageResponse
import br.com.fiap.wtcapp.model.SendMessageRequest
import retrofit2.Response
import retrofit2.http.*

interface MessageApiService {
    // Busca todas as mensagens (ajuste a rota se tiver um GET geral, usei a base por enquanto)
    @GET("api/v1/messages/conversation/{id}")
    suspend fun getHistorico(@Path("id") conversationId: String): Response<List<MessageResponse>>

    @POST("api/v1/messages")
    suspend fun enviarMensagem(@Body request: SendMessageRequest): Response<MessageResponse>
}