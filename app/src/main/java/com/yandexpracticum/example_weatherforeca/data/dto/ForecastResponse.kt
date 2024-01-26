package com.yandexpracticum.example_weatherforeca.data.dto

import com.google.gson.annotations.SerializedName

class ForecastResponse (
    @SerializedName("current")
    val current: CurrentWeather
)