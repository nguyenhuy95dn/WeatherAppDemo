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
import com.example.weatherappdemo.databinding.FragmentListBinding
import com.example.weatherappdemo.screen.adapter.ItemClickListener
import com.example.weatherappdemo.screen.adapter.WeatherAdapter
import com.example.weatherappdemo.screen.main.MainViewModel
import com.example.weatherappdemo.utils.LiveDataResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {
    private lateinit var mBinding: FragmentListBinding
    private lateinit var mWeatherAdapter: WeatherAdapter
    private val mMainViewModel: MainViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_list,
                container,
                false
            )
        initView()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerListData()
        observerFavoriteData()
        mMainViewModel.loadWeathers()
    }

    private fun initView() {
        context?.let {ctx ->
            mWeatherAdapter = WeatherAdapter(ctx)
            mWeatherAdapter.setItemClickListener(mListener)
            mBinding.rcvWeathers.adapter = mWeatherAdapter
            mBinding.rcvWeathers.layoutManager = LinearLayoutManager(context)
            mBinding.rcvWeathers.setHasFixedSize(true)
        }
    }

    private fun observerListData() {
        mMainViewModel.mLiveListWeathers.observe(viewLifecycleOwner, Observer {
            when (it?.status) {
                LiveDataResult.Status.LOADING -> {
                    showProgress()
                }
                LiveDataResult.Status.SUCCESS -> {
                    hideProgressBar()
                    mWeatherAdapter.setData(it.data ?:ArrayList())
                    mMainViewModel.saveWeathers(it.data ?:ArrayList())
                }
                LiveDataResult.Status.ERROR -> {
                    hideProgressBar()
                    //todo error here
                }

            }
        })
    }

    private fun hideProgressBar() {
        mBinding.progressbar.visibility = View.GONE
    }

    private fun showProgress() {
        mBinding.progressbar.visibility = View.VISIBLE
    }

    private fun observerFavoriteData() {
        mMainViewModel.observeFavorites().observe(viewLifecycleOwner, Observer {
            mWeatherAdapter.setFavorites(it)
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

    companion object {
        @JvmStatic
        fun newInstance(): ListFragment {
            return ListFragment()
        }
    }
}