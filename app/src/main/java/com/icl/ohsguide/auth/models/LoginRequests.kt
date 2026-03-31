package com.icl.ohsguide.auth.models

import com.icl.ohsguide.auth.data.User

data class LoginRequests (
    val email: String,
    val password: String
)

data class LoginResponse (
    val access_token: String,
    val expires_in: Int,
)