package com.example.weatherappdemo.screen.adapter

import com.example.weatherappdemo.core.data.local.model.Weather

abstract class ItemClickListener {
    open fun onFavoriteClick(weather: Weather) {}
}