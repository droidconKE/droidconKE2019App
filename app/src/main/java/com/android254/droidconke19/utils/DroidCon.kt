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

        setupEmojiCompat()

        startKoin {
            if (BuildConfig.DEBUG) androidLogger() else EmptyLogger()
            androidContext(this@DroidCon)
            modules(listOf(appModule, dataModule))
        }
    }

    private fun setupEmojiCompat() {
        val fontRequest = FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs
        )
        val config = FontRequestEmojiCompatConfig(applicationContext, fontRequest)
                .setReplaceAll(true)
                .registerInitCallback(object : EmojiCompat.InitCallback() {
                    override fun onInitialized() {
                    }

                    override fun onFailed(throwable: Throwable?) {
                    }
                })
        EmojiCompat.init(config)

    }
}
