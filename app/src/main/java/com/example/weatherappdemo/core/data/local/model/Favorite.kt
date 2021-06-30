package com.example.weatherappdemo.core.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
class Favorite(
    @PrimaryKey
    var woeid: Int? = null
)