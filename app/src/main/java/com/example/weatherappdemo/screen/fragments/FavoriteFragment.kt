package com.example.weatherappdemo.screen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherappdemo.R
import com.example.weatherappdemo.core.data.local.model.Favorite
import com.example.weatherappdemo.core.data.local.model.Weather
import com.example.weatherappdemo.databinding.FragmentFavoriteBinding
import com.example.weatherappdemo.screen.adapter.ItemClickListener
import com.example.weatherappdemo.screen.adapter.WeatherAdapter
import com.example.weatherappdemo.screen.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private lateinit var mBinding: FragmentFavoriteBinding
    private lateinit var mWeatherAdapter: WeatherAdapter
    private val mMainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_favorite,
                container,
                false
            )
        initView()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerData()
    }

    private fun initView() {
        context?.let {ctx ->
            mWeatherAdapter = WeatherAdapter(ctx)
            mWeatherAdapter.setIsFavoriteScreen(true)
            mWeatherAdapter.setItemClickListener(mListener)
            mBinding.rcvWeathers.adapter = mWeatherAdapter
            mBinding.rcvWeathers.layoutManager = LinearLayoutManager(context)
            mBinding.rcvWeathers.setHasFixedSize(true)
        }
    }

    private fun observerData() {
        mMainViewModel.observeFavorites().observe(viewLifecycleOwner, Observer {
            mWeatherAdapter.setData(mMainViewModel.getWeathers(getListFavoriteStr(it)) ?:ArrayList())
        })
    }

    private val mListener: ItemClickListener = object : ItemClickListener() {
        override fun onFavoriteClick(weather: Weather) {
            val id = weather.woeid?:0
            if (mMainViewModel.getFavorite(id) == null) {
                mMainViewModel.saveFavorite(Favorite(id))
            } else {
                mMainViewModel.deleteFavoriteById(id)
            }
        }
    }

    private fun getListFavoriteStr(list: List<Favorite>?): List<String> {
        val strings = ArrayList<String>()
        list?.let {
            for (favorite: Favorite in list) {
                strings.add(favorite.woeid.toString())
            }
        }
        return strings
    }

    companion object {
        @JvmStatic
        fun newInstance(): FavoriteFragment {
            return FavoriteFragment()
        }
    }
}