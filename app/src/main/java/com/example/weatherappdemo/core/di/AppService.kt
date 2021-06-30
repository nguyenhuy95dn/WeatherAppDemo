package com.example.weatherappdemo.core.di

import com.example.weatherappdemo.core.data.local.model.Weather
import retrofit2.http.GET

interface AppService {
    @GET("location/search/?query=a")
    suspend fun loadWeathers(): ArrayList<Weather>
}