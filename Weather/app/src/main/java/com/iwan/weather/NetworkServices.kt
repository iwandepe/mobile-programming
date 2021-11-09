package com.iwan.weather

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.openweathermap.org/"

interface NetworkServices {
    @GET("data/2.5/weather?q=kediri&appid=c9253bbc87859a49fb28ec2b6d0d2e91")
    fun getCurrentWeather(): Call<CurrentWeatherResponse>

    @GET("data/2.5/onecall?lat=33.44&lon=-94.04&appid=c9253bbc87859a49fb28ec2b6d0d2e91")
    fun getWeather(): Call<WeatherResponse>
}

object DataServices {
    fun create(): NetworkServices {
        val retrofit = Retrofit.Builder()
            // convert json to kotlin object
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
        return retrofit.create(NetworkServices::class.java)
    }
}
