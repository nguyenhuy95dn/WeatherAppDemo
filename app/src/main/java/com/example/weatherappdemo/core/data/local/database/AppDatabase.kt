package com.example.weatherappdemo.core.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherappdemo.core.data.local.dao.FavoriteDao
import com.example.weatherappdemo.core.data.local.dao.WeatherDao
import com.example.weatherappdemo.core.data.local.model.Favorite
import com.example.weatherappdemo.core.data.local.model.Weather

/**
 * AppDatabase
 */
@Database(entities = [Weather::class, Favorite::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val weatherDao: WeatherDao?
    abstract val favoriteDao: FavoriteDao?

    companion object {
        const val DATABASE_DB = "weather_database"
        private var appDatabase: AppDatabase? = null
        fun getInstance(context: Context?): AppDatabase? {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(
                    context!!, AppDatabase::class.java, DATABASE_DB
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build()
            }
            return appDatabase
        }
    }
}