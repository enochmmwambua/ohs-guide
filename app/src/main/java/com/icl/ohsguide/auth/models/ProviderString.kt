package com.icl.ohsguide.auth.models

data class UserData(
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String
)

data class ProviderStringResponse (
    val email: String,
    val user: UserData
)

