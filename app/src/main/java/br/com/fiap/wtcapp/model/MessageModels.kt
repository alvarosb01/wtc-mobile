package br.com.fiap.wtcapp.model

data class MessageResponse(
    val id: String? = null,
    val content: String? = null,
    val senderRole: String? = null,
    val senderId: Any? = null, // Pode vir como objeto ou string, Any não trava
    val status: String? = null,
    val subject: String? = null,
    val createdAt: Any? = null, // Datas costumam travar o GSON, Any resolve
    val conversationId: String? = null
)

data class SendMessageRequest(
    val targetType: String,
    val subject: String,
    val content: String,
    val customerId: String,
    val conversationId: String,
    val segmentId: String? = null,
    val groupName: String? = null,
    val customerIds: List<String>? = null
)