package com.example.weather.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val baseURL = "https://api.weatherapi.com/"

    private fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).
            build()
    }

    val  weatherApi = getInstance().create(WeatherApi::class.java)
}