package com.icl.ohsguide.auth.network

import com.icl.ohsguide.auth.models.LoginRequests
import com.icl.ohsguide.auth.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {
    //@GET("posts/{id}")
    //fun getPostById(@Path("id") postId: Int): Call<Post>
    @POST("provider/login")
    fun login(@Body loginRequest: LoginRequests): Call<LoginResponse>

    @GET("provider/me")
    @Headers("Authorization: Bearer {token}")
    fun me(@Path("token") token: String): Call<ProviderStringResponse>
}
