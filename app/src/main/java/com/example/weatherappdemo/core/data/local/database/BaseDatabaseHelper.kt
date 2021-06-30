package com.example.weatherappdemo.core.data.local.database

import android.content.Context

open class BaseDatabaseHelper internal constructor(context: Context?) {
    var appDatabase: AppDatabase

    init {
        appDatabase = AppDatabase.getInstance(context)!!
    }
}