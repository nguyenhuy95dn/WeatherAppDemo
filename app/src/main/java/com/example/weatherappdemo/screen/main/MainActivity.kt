package com.example.weatherappdemo.screen.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.weatherappdemo.R
import com.example.weatherappdemo.databinding.ActivityMainBinding
import com.example.weatherappdemo.screen.fragments.FavoriteFragment
import com.example.weatherappdemo.screen.fragments.ListFragment
import com.example.weatherappdemo.screen.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mMainBinding: ActivityMainBinding
    private lateinit var mListFragment: ListFragment
    private lateinit var mFavoriteFragment: FavoriteFragment
    private var mCurrentFragment: Fragment? = null
    private val mFragments = ArrayList<Fragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initFragments()
        initView()
    }

    private fun initView() {
        mMainBinding.navigation.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )
        mMainBinding.navigation.setItemIconTintList(null)
        mMainBinding.navigation.setSelectedItemId(R.id.action_home)
        showFirstFragment()
    }

    private fun showFirstFragment() {
        supportFragmentManager.beginTransaction().replace(
            R.id.main_container,
            mListFragment
        ).commit()
        mCurrentFragment = mListFragment
        mFragments.add(mCurrentFragment!!)
    }

    private fun initFragments() {
        mListFragment = ListFragment.newInstance()
        mFavoriteFragment = FavoriteFragment.newInstance()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.option_search -> {
                onSearchClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onFavoriteScreenClicked() {
        if (mCurrentFragment is FavoriteFragment) {
            return
        }
        mCurrentFragment?.let { switchFragment(mFavoriteFragment, it) }
    }

    private fun onListScreenClicked() {
        if (mCurrentFragment is ListFragment) {
            return
        }
        mCurrentFragment?.let { switchFragment(mListFragment, it) }
    }

    fun switchFragment(show: Fragment, hide: Fragment) {
        mCurrentFragment = show
        if (!mFragments.contains(mCurrentFragment!!)) {
            mFragments.add(mCurrentFragment!!)
        }
        if (!show.isAdded()) {
            supportFragmentManager.beginTransaction().add(R.id.main_container, show).hide(hide)
                .show(show).commit()
        } else {
            supportFragmentManager.beginTransaction().hide(hide).show(show).commit()
        }
    }

    private fun onSearchClicked() {
        if (mCurrentFragment is SearchFragment) {
            return
        }
        mCurrentFragment?.let {
            switchFragment(
                SearchFragment.newInstance(),
                it
            )
        }
    }

    fun removeShowFragment(remove: Fragment, show: Fragment) {
        mCurrentFragment = show
        if (!show.isAdded) {
            supportFragmentManager.beginTransaction().add(R.id.main_container, show).remove(remove)
                .show(show).commit()
        } else {
            supportFragmentManager.beginTransaction().remove(remove).show(show).commit()
        }
    }

    private fun removeCurrentFragment() {
        removeShowFragment(mCurrentFragment!!, mFragments.get(mFragments.size - 2))
        mFragments.removeAt(mFragments.size - 1)
    }

    val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.action_home -> {
                    onListScreenClicked()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.action_favorite -> {
                    onFavoriteScreenClicked()
                    return@OnNavigationItemSelectedListener true
                }
                else -> {
                    return@OnNavigationItemSelectedListener true
                }
            }
        }

    override fun onBackPressed() {
        if (mCurrentFragment is SearchFragment) {
            removeCurrentFragment()
            return
        }
        super.onBackPressed()
    }

    fun hideToolBar() {
        supportActionBar?.hide()
        mMainBinding.navigation.visibility = View.GONE
    }

    fun showToolBar() {
        supportActionBar?.show()
        mMainBinding.navigation.visibility = View.VISIBLE
    }

}