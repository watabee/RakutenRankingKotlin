package com.github.watabee.rakutenranking

import android.app.Application
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger
import org.koin.log.EmptyLogger
import timber.log.Timber

class RakutenRankingApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(
            androidContext = this,
            modules = listOf(appModule),
            logger = if (BuildConfig.DEBUG) AndroidLogger() else EmptyLogger()
        )

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}