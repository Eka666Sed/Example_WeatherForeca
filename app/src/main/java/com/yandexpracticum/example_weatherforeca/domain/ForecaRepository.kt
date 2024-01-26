package com.yandexpracticum.example_weatherforeca.domain

import android.util.Log
import com.yandexpracticum.example_weatherforeca.data.ForecaApi
import com.yandexpracticum.example_weatherforeca.data.dto.ForecaAuthRequest
import com.yandexpracticum.example_weatherforeca.data.dto.LocationsResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ForecaRepository {

    private companion object {
        const val BASE_URL = "https://pfa.foreca.com"

        const val USER = "cat-sedoff"
        const val PASSWORD = "t9LOKd2Zc3gB"
        const val HARDCODED_LOCATION = "Barcelona"
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(
            RxJava2CallAdapterFactory.create()
        )
        .build()

    private val forecaService = retrofit.create(ForecaApi::class.java)

    private var token = ""

    fun authenticate() {
        forecaService.authenticate(ForecaAuthRequest(USER, PASSWORD))
            .flatMap { tokenResponse ->
                token = tokenResponse.token
                Log.d("RxJava", "Got new access token!")
                val bearerToken = "Bearer ${tokenResponse.token}"
                forecaService.getLocations(bearerToken, HARDCODED_LOCATION)
            }
//                val responseError = Response.error<LocationsResponse>(401, ResponseBody.create(
//                    MediaType.get("application/json"), "{}"))
//                val exception = HttpException(responseError)
//
//                Single.error<LocationsResponse>(exception)
//            }
            .retry { count,throwable ->
                Log.d("RxJava", "Got error! count: $count, $throwable")
                count < 3 && throwable is HttpException && throwable.code() == 401
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { locationsResponse ->
                    Log.d("RxJava", "Got location: ${locationsResponse.locations}")
                },
                { error -> Log.e("RxJava", "Got error with auth or locations", error) }
            )

//            .enqueue(object : Callback<ForecaAuthResponse> {
//                override fun onResponse(call: Call<ForecaAuthResponse>,
//                                        response: Response<ForecaAuthResponse>
//                ) {
//                    if (response.code() == 200) {
//                        token = response.body()?.token.toString()
//                        search(token, HARDCODED_LOCATION)
//                    } else {
//                        Log.e("RxJavaForeca", "Something went wrong with auth: ${response.code().toString()}")
//                    }
//                }
//
//                override fun onFailure(call: Call<ForecaAuthResponse>, t: Throwable) {
//                    Log.e("RxJavaForeca", "onFailure auth request", t)
//                }
//            })
    }

//    private fun search(accessToken: String, searchQuery: String) {
//        val bearerToken = "Bearer $accessToken"
//        forecaService.getLocations(bearerToken, HARDCODED_LOCATION)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                { locationsResponse ->
//                    Log.d("RxJava", "Got location: ${locationsResponse.locations}")
//                },
//                { error -> Log.e("RxJava", "Got error with auth or locations", error) }
//            )

//            .enqueue(object : Callback<LocationsResponse> {
//                override fun onResponse(call: Call<LocationsResponse>,
//                                        response: Response<LocationsResponse>) {
//                    when (response.code()) {
//                        200 -> {
//                            if (response.body()?.locations?.isNotEmpty() == true) {
//                                val locations = response.body()?.locations!!
//
//                                Log.d("RxJavaForeca", "Found locations!")
//                                locations.forEach {
//                                    Log.d("RxJavaForeca", it.toString())
//                                }
//
//
//                            } else {
//                                Log.d("RxJavaForeca", "Nothing found")
//                            }
//
//                        }
//                        401 -> authenticate()
//                        else -> {
//                            Log.e("RxJavaForeca", "Something went wrong with search: ${response.code().toString()}")
//                        }
//                    }
//
//                }
//
//                override fun onFailure(call: Call<LocationsResponse>, t: Throwable) {
//                    Log.e("RxJavaForeca", "onFailure search request", t)
//                }
//
//            })
    }
