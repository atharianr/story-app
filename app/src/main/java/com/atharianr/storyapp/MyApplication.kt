package com.atharianr.storyapp

import android.app.Application
import android.content.SharedPreferences
import com.atharianr.storyapp.di.networkModule
import com.atharianr.storyapp.di.remoteDataSourceModule
import com.atharianr.storyapp.di.viewModelModule
import com.atharianr.storyapp.utils.PreferenceHelper
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        prefs = PreferenceHelper.defaultPrefs(this)

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    networkModule,
                    remoteDataSourceModule,
                    viewModelModule
                )
            )
        }
    }

    companion object {
        lateinit var prefs: SharedPreferences
    }
}