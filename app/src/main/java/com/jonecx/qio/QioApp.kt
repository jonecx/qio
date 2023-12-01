package com.jonecx.qio

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class QioApp : Application() {
    override fun onCreate() {
        super.onCreate()
        print("Stuff")
    }
}
