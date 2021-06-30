package com.example.weatherappdemo.screen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappdemo.R
import com.example.weatherappdemo.core.data.local.model.Favorite
import com.example.weatherappdemo.core.data.local.model.Weather


class WeatherAdapter(private val mContext: Context) :
    RecyclerView.Adapter<WeatherAdapter.GenericViewHolder>() {
    private var mIsFavoriteScreen: Boolean = false
    private val mFavorites: MutableList<Favorite> = ArrayList()
    private var mWeathers: MutableList<Weather> = ArrayList()
    private var mItemClickListener: ItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_weathers, parent, false)
        val customItemViewHolder: GenericViewHolder = WeatherHolder(view)
        return customItemViewHolder
    }

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        bindDataNewLayout(holder as WeatherHolder, position)
    }

    private fun bindDataNewLayout(holder: WeatherHolder?, position: Int) {
        holder?.let {
            val weather: Weather = mWeathers[position]
            holder.mTvTitle?.text = weather.title ?: ""
            holder.mTvType?.text = weather.locationType ?: ""
            holder.mTvLatt?.text = weather.lattLong ?: ""
            val isFavorite = isFavorite(weather.woeid?:0)
            if (isFavorite || mIsFavoriteScreen) {
                holder.mImgFavorite?.setImageResource(R.drawable.ic_fav_on)
            } else {
                holder.mImgFavorite?.setImageResource(R.drawable.ic_fav_off)
            }
        }
    }

    private fun isFavorite(woeid: Int): Boolean {
        var isFavorite = false
        for (favorite: Favorite in mFavorites) {
            if (favorite.woeid == woeid) {
                isFavorite = true
                break
            }
        }
        return isFavorite
    }

    override fun getItemCount(): Int {
        return mWeathers.size
    }

    open inner class GenericViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    }

    internal inner class WeatherHolder(view: View?) : GenericViewHolder(view) {

        var mTvTitle: AppCompatTextView? = null
        var mTvType: AppCompatTextView? = null
        var mTvLatt: AppCompatTextView? = null
        var mImgFavorite: AppCompatImageView? = null

        init {
            view?.let {
                mTvTitle = view.findViewById(R.id.tv_title)
                mTvType = view.findViewById(R.id.tv_type)
                mTvLatt = view.findViewById(R.id.tv_latt_long)
                mImgFavorite = view.findViewById(R.id.img_favorite)
                mImgFavorite?.setOnClickListener {
                    mItemClickListener?.onFavoriteClick(mWeathers[position])
                }
            }
        }
    }

    fun setItemClickListener(listener: ItemClickListener) {
        mItemClickListener = listener
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setData(artices: List<Weather>) {
        mWeathers.clear()
        mWeathers.addAll(artices)
        notifyDataSetChanged()
    }

    fun setFavorites(favorites: List<Favorite>) {
        mFavorites.clear()
        mFavorites.addAll(favorites)
        notifyDataSetChanged()
    }

    fun setIsFavoriteScreen(isFavorite: Boolean) {
        mIsFavoriteScreen = isFavorite
    }
}
