package br.com.fiap.wtcapp.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    fun getRetrofit(tokenManager: TokenManager): AuthApiService {
        // Criamos um cliente HTTP que funciona como um "Filter" do Spring
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()

            // Se existir um token salvo, adiciona no Header
            tokenManager.getToken()?.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            }

            chain.proceed(requestBuilder.build())
        }.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // Usamos o cliente com o interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }

    fun getCustomerService(context: android.content.Context): CustomerApiService {
        val tokenManager = TokenManager(context)

        val client = okhttp3.OkHttpClient.Builder().addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            tokenManager.getToken()?.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            }
            chain.proceed(requestBuilder.build())
        }.build()

        return retrofit2.Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .client(client)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(CustomerApiService::class.java)
    }

    // Mantemos esse para o Login (que não precisa de token)
    val authService: AuthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }

    fun getMessageService(context: android.content.Context): MessageApiService {
        val tokenManager = TokenManager(context)
        val client = okhttp3.OkHttpClient.Builder().addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            tokenManager.getToken()?.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            }
            chain.proceed(requestBuilder.build())
        }.build()

        return retrofit2.Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .client(client)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(MessageApiService::class.java)
    }

}