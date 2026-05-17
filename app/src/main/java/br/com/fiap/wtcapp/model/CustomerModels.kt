package br.com.fiap.wtcapp.model

// O "embrulho" do Spring Page
data class PageResponse<T>(
    val content: List<T>,
    val totalElements: Int,
    val totalPages: Int,
    val last: Boolean
)

// O seu CustomerResponse Java convertido
data class CustomerResponse(
    val id: String,
    val name: String,
    val document: String,
    val vip: Boolean,
    val fidelidade: Boolean,
    val ativo: Boolean,
    val createdAt: String // Recebemos como String para facilitar o parse inicial
)

// Equivalente ao seu CreateCustomerRequest do Java
data class CreateCustomerRequest(
    val name: String,
    val document: String,
    val vip: Boolean = false,
    val fidelidade: Boolean = false,
    val ativo: Boolean = true
)