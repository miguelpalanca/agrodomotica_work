package com.example.myapplication2

import android.app.Application
import com.thingclips.smart.home.sdk.ThingHomeSdk

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ThingHomeSdk.init(this)
    }
}