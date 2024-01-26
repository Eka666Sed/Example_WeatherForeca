package com.yandexpracticum.example_weatherforeca.data

import com.yandexpracticum.example_weatherforeca.data.dto.ForecaAuthRequest
import com.yandexpracticum.example_weatherforeca.data.dto.ForecaAuthResponse
import com.yandexpracticum.example_weatherforeca.data.dto.ForecastResponse
import com.yandexpracticum.example_weatherforeca.data.dto.LocationsResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ForecaApi {
    @POST("/authorize/token?expire_hours=-1")
    fun authenticate(
        @Body request: ForecaAuthRequest
    ): Single<ForecaAuthResponse>

    @GET("/api/v1/location/search/{query}")
    fun getLocations(
        @Header("Authorization") token: String,
        @Path("query") query: String
    ): Single<LocationsResponse>

    @GET("/api/v1/current/{location}")
    fun getForecast(
        @Header("Authorization") token: String,
        @Path("location") locationId: Int
    ): Single<ForecastResponse>

}