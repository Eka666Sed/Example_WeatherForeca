package com.yandexpracticum.example_weatherforeca.data.dto

import com.google.gson.annotations.SerializedName

class ForecaAuthResponse(
    @SerializedName("access_token")
    val token: String
)