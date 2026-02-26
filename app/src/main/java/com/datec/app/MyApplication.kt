package com.datec.app

import android.app.Application
import com.datec.app.core.initialization.AppBootstrap
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
	override fun onCreate() {
		AppBootstrap.init(this)
		super.onCreate()
	}
}