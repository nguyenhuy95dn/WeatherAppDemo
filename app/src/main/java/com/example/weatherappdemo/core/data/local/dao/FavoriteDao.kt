package com.example.weatherappdemo.core.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.weatherappdemo.core.data.local.model.Favorite

@Dao
interface FavoriteDao : BaseDao<Favorite> {
    @Query("Select * from favorite")
    fun getWeathers(): LiveData<List<Favorite>>

    @Query("select * from favorite")
    fun observeWeathers(): LiveData<List<Favorite>>

    @Query("DELETE FROM favorite")
    fun deleteAll()

    @Query("select * from favorite")
    fun observeFavorites(): LiveData<List<Favorite>>

    @Query("Select * from favorite where woeid = :id")
    fun getFavorite(id: Int): Favorite?

    @Query("DELETE FROM favorite where woeid = :id")
    fun deleteFavoriteById(id: Int)
}