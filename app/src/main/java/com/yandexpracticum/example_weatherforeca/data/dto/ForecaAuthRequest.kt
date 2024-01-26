package com.yandexpracticum.example_weatherforeca.data.dto

import com.google.gson.annotations.SerializedName

class ForecaAuthRequest(
    @SerializedName("user")
    val user: String,
    @SerializedName("password")
    val password: String
)

