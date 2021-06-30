package com.example.weatherappdemo.core.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather")
class Weather(
    @SerializedName("title")
    @Expose
    var title: String? = null,
    @SerializedName("location_type")
    @Expose
    var locationType: String? = null,
    @SerializedName("woeid")
    @Expose
    @PrimaryKey
    var woeid: Int? = null,
    @SerializedName("latt_long")
    @Expose
    var lattLong: String? = null
)