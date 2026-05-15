package br.com.fiap.wtcapp.model

import com.google.gson.annotations.SerializedName

data class SendMessageRequest(
    val targetType: String = "CUSTOMER", // Match com o Enum MessageTargetType
    val subject: String,
    val content: String,
    val customerId: String,
    val conversationId: String // O campo que estava faltando!
)

// Response ajustado para o seu Record Java
data class MessageResponse(
    val id: String? = null,
    val content: String? = null,
    val senderRole: String? = null,
    val status: String? = null,
    val subject: String? = null,
    val conversationId: String? = null
)