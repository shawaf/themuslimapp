package me.shawaf.themuslimapp.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.Forest.plant


@HiltAndroidApp
class TheMuslimApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // This will initialize Timber
        if (me.shawaf.themuslimapp.BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }
    }
}