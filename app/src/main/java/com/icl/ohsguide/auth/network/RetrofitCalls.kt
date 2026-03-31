package com.icl.ohsguide.auth.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCalls {
    private const val BASE_URL = "https://ngsadev.intellisoftkenya.com/auth/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

object ApiClient {
    val apiService: ApiInterface by lazy {
        RetrofitCalls.retrofit.create(ApiInterface::class.java)
    }
}