package com.example.qiitafetcher

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class QFApplication : Application() {

    companion object {
        val networkFlipperPlugin by lazy { NetworkFlipperPlugin() }
    }

    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, false)

//      todo BuildConfigを設定し、DEBUGビルドの時のみFlipperの起動がされるよう制御する
//       if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
        if (FlipperUtils.shouldEnableFlipper(this)) {
            val client = AndroidFlipperClient.getInstance(this)

            client.addPlugin(networkFlipperPlugin)
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))

            // Flipperの起動
            client.start()
        }
    }
}
