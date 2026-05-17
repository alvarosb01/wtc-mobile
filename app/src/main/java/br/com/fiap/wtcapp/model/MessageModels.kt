package br.com.fiap.wtcapp.model

import com.google.gson.annotations.SerializedName

// DTO para Enviar (POST) - Deve bater com o seu Record Java de 8 campos
data class SendMessageRequest(
    val targetType: String,
    val subject: String,
    val content: String,
    val customerId: String? = null,
    val segmentId: String? = null,
    val groupName: String? = null,
    val customerIds: List<String>? = null,
    val conversationId: String
)

// DTO para Receber (GET)
data class MessageResponse(
    val id: String? = null,
    val content: String? = null,
    val senderRole: String? = null,
    val status: String? = null,
    val subject: String? = null,
    val conversationId: String? = null
)