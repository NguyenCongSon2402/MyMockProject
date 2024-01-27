package com.example.mymockproject.di

import android.app.Application
import com.example.mymockproject.helper.LogUtil
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application()  {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(LogUtil())
    }
}