package com.mamsky.stockalculator.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class StockalkulatorApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant()
    }

}
