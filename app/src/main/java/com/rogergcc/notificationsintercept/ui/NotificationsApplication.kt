package com.rogergcc.notificationsintercept.ui

import android.app.Application
import com.rogergcc.notificationsintercept.di.injectModules
import com.rogergcc.notificationsintercept.ui.utils.TimberAppLogger
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Created on enero.
 * year 2023 .
 */
internal class NotificationsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initLogger()
        initializeKoin()
    }

    private fun initializeKoin() {
        startKoin {
            androidLogger()
            androidContext(this@NotificationsApplication)
            injectModules()
        }
    }

    private fun initLogger() {
        TimberAppLogger.init()
    }
}