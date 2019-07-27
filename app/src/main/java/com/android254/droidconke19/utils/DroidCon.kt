package com.android254.droidconke19.utils

import android.app.Application
import androidx.core.provider.FontRequest
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig
import com.android254.droidconke19.BuildConfig
import com.android254.droidconke19.R
import com.android254.droidconke19.di.appModule
import com.android254.droidconke19.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger

class DroidCon : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            if (BuildConfig.DEBUG) androidLogger() else EmptyLogger()
            androidContext(this@DroidCon)
            modules(listOf(appModule, dataModule))
        }
    }
}
