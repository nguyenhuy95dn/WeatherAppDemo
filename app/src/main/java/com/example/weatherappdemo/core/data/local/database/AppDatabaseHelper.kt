package com.example.weatherappdemo.core.data.local.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weatherappdemo.core.data.local.dao.FavoriteDao
import com.example.weatherappdemo.core.data.local.dao.WeatherDao
import com.example.weatherappdemo.core.data.local.model.Favorite
import com.example.weatherappdemo.core.data.local.model.Weather

class AppDatabaseHelper(context: Context?) : BaseDatabaseHelper(context) {
    private var mWeatherDao: WeatherDao? = null
    private var mFavoriteDao: FavoriteDao? = null

    init {
        mWeatherDao = appDatabase.weatherDao
        mFavoriteDao = appDatabase.favoriteDao
    }

    fun observeWeathers(): LiveData<List<Weather>> {
        return mWeatherDao?.observeWeathers()!!
    }

    fun getListWeatherByFavoritesDb(list: List<String>): List<Weather> {
        return mWeatherDao?.getListWeatherByFavoritesDb(list)?:ArrayList()
    }

    fun getListWeathersByKeyword(keyword: String): List<Weather>? {
        return mWeatherDao?.getListWeathersByKeyword(keyword)
    }

    fun observeFavorites(): LiveData<List<Favorite>> {
        return mFavoriteDao?.observeFavorites()!!
    }
}