package com.example.weatherappdemo.core.data.repository

import androidx.lifecycle.LiveData
import com.example.weatherappdemo.core.data.local.database.AppDatabaseHelper
import com.example.weatherappdemo.core.data.local.model.Favorite
import com.example.weatherappdemo.core.data.local.model.Weather
import com.example.weatherappdemo.core.di.AppService
import javax.inject.Inject


class AppRepository @Inject constructor(var mAppDatabaseHelper : AppDatabaseHelper, var mAppService:AppService) {

    fun observeWeathers(): LiveData<List<Weather>> {
        return mAppDatabaseHelper.observeWeathers()
    }

    fun getWeathers(list: List<String>): List<Weather> {
        return mAppDatabaseHelper.getListWeatherByFavoritesDb(list)!!
    }

    fun getListWeathersByKeyword(keyword: String): List<Weather>? {
        return mAppDatabaseHelper.getListWeathersByKeyword(keyword)
    }

    fun saveWeather(weather: Weather) {
        return mAppDatabaseHelper.appDatabase.weatherDao?.insert(weather)!!
    }

    fun saveWeathers(list: ArrayList<Weather>) {
        return mAppDatabaseHelper.appDatabase.weatherDao?.insert(list)!!
    }

    fun removeAllWeathers() {
        return mAppDatabaseHelper.appDatabase.weatherDao?.deleteAll()!!
    }

    fun observeFavorites(): LiveData<List<Favorite>> {
        return mAppDatabaseHelper.observeFavorites()
    }

    fun saveFavorite(favorite: Favorite) {
        return mAppDatabaseHelper.appDatabase.favoriteDao?.insert(favorite)!!
    }

    fun getFavorite(id: Int): Favorite? {
        return mAppDatabaseHelper.appDatabase.favoriteDao?.getFavorite(id)
    }

    fun deleteFavoriteById(id: Int) {
        mAppDatabaseHelper.appDatabase.favoriteDao?.deleteFavoriteById(id)!!
    }

    suspend fun loadWeathers(): ArrayList<Weather> {
        return mAppService.loadWeathers()
    }
}