package com.harry.weather.util

import android.content.Context
import androidx.core.content.ContextCompat
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.test.FakeImageLoaderEngine
import coil3.test.intercept

object ImageLoadingTestUtil {
    @OptIn(DelicateCoilApi::class)
    fun setupFakeImageLoader(context: Context) {
        val testDrawable = ContextCompat.getDrawable(context, com.harry.weather.test.R.drawable.test_weather_icon)

        val engine =
            FakeImageLoaderEngine
                .Builder()
                .intercept({ true }, testDrawable!!)
                .build()

        val imageLoader =
            ImageLoader
                .Builder(context)
                .components { add(engine) }
                .build()

        SingletonImageLoader.setUnsafe(imageLoader)
    }
}
