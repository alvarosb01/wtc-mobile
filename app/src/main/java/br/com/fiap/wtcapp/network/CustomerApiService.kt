package br.com.fiap.wtcapp.network

import br.com.fiap.wtcapp.model.CreateCustomerRequest
import br.com.fiap.wtcapp.model.CustomerResponse
import br.com.fiap.wtcapp.model.PageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CustomerApiService {
    @GET("customers")
    suspend fun listCustomers(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<PageResponse<CustomerResponse>>

    @POST("customers")
    suspend fun createCustomer(@Body request: CreateCustomerRequest): Response<CustomerResponse>
}