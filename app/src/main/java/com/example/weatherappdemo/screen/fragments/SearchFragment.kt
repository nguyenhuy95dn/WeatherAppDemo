package com.example.weatherappdemo.screen.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.weatherappdemo.databinding.FragmentSearchBinding
import com.example.weatherappdemo.screen.adapter.ItemClickListener
import com.example.weatherappdemo.screen.adapter.WeatherAdapter
import com.example.weatherappdemo.screen.main.MainActivity
import com.example.weatherappdemo.screen.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var mBinding: FragmentSearchBinding
    private lateinit var mWeatherAdapter: WeatherAdapter
    private val mMainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_search,
                container,
                false
            )
        initView()
        return mBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity?)?.hideToolBar()
    }

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity?)?.showToolBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerFavoriteData()

    }

    private fun initView() {
        context?.let { ctx ->
            mWeatherAdapter = WeatherAdapter(ctx)
            mWeatherAdapter.setItemClickListener(mListener)
            mBinding.rcvWeathers.adapter = mWeatherAdapter
            mBinding.rcvWeathers.layoutManager = LinearLayoutManager(context)
            mBinding.rcvWeathers.setHasFixedSize(true)
            mBinding.edtSearch.addTextChangedListener(mTextWatcherListener)
            mBinding.imgClose.setOnClickListener {
                mBinding.edtSearch.setText("")
            }
        }
    }

    private fun observerFavoriteData() {
        mMainViewModel.observeFavorites().observe(viewLifecycleOwner, Observer {
            mWeatherAdapter.setFavorites(it)
        })
    }

    private val mListener: ItemClickListener = object : ItemClickListener() {
        override fun onFavoriteClick(weather: Weather) {
            val id = weather.woeid ?: 0
            if (mMainViewModel.getFavorite(id) == null) {
                mMainViewModel.saveFavorite(Favorite(id))
            } else {
                mMainViewModel.deleteFavoriteById(id)
            }
        }
    }

    private val mTextWatcherListener: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val keyword = s.toString()
            if (keyword.equals("")) {
                mWeatherAdapter.setData(ArrayList())
                return
            }
            mWeatherAdapter.setData(mMainViewModel.getListWeathersByKeyword("%$keyword%"))
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }

    companion object {
        @JvmStatic
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }
}