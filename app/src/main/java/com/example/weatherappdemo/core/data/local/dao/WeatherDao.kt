package com.example.weatherappdemo.core.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.weatherappdemo.core.data.local.model.Weather

@Dao
interface WeatherDao : BaseDao<Weather> {
    @Query("select * from weather")
    fun observeWeathers(): LiveData<List<Weather>>

    @Query("DELETE FROM weather")
    fun deleteAll()

    @Query("select * from weather where woeid IN (:list)")
    fun getListWeatherByFavoritesDb(list: List<String>): List<Weather>

    @Query(
        "Select * from weather where title LIKE :keyword OR lattLong LIKE :keyword OR locationType LIKE :keyword"
    )
    fun getListWeathersByKeyword(keyword: String?): List<Weather>
}