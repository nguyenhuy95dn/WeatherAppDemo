package com.example.weatherappdemo.screen.main

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.weatherappdemo.core.data.local.model.Favorite
import com.example.weatherappdemo.core.data.local.model.Weather
import com.example.weatherappdemo.core.data.repository.AppRepository
import com.example.weatherappdemo.utils.LiveDataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private var mAppRepository: AppRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var mLiveListWeathers = MutableLiveData<LiveDataResult<ArrayList<Weather>>>()
    fun loadWeathers() {
        viewModelScope.launch(Dispatchers.IO) {
            mLiveListWeathers.postValue(LiveDataResult.loading())
            removeAllWeathers()
            try {
                val list = mAppRepository.loadWeathers()
                mLiveListWeathers.postValue(LiveDataResult.success(list))
                saveWeathers(list)
            } catch (e: Exception) {
                mLiveListWeathers.postValue(LiveDataResult.error(e))
            }
        }
    }

    fun observeFavorites(): LiveData<List<Favorite>> {
        return mAppRepository.observeFavorites()
    }

    fun saveWeather(weather: Weather) {
        return mAppRepository.saveWeather(weather)
    }

    fun saveFavorite(favorite: Favorite) {
        return mAppRepository.saveFavorite(favorite)
    }

    fun getFavorite(id: Int): Favorite? {
        return mAppRepository.getFavorite(id)
    }

    fun saveWeathers(list: ArrayList<Weather>) {
        return mAppRepository.saveWeathers(list)
    }

    fun removeAllWeathers() {
        return mAppRepository.removeAllWeathers()
    }

    fun getWeathers(strings: List<String>): List<Weather> {
        return mAppRepository.getWeathers(strings)
    }

    fun getListWeathersByKeyword(keyword: String): List<Weather> {
        return mAppRepository.getListWeathersByKeyword(keyword)?:ArrayList()
    }

    fun deleteFavoriteById(id: Int) {
        mAppRepository.deleteFavoriteById(id)
    }
}