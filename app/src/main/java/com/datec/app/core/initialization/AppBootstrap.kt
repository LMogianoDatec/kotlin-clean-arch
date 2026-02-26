package com.datec.app.core.initialization

import android.app.Application
import android.util.Log
import com.datec.app.core.EnvConfig

object AppBootstrap {
    private const val TAG = "BOOT"

    fun init(application: Application) {
        try {
            Log.d(TAG, "Bootstrapping app: loading EnvConfig")
            EnvConfig.load(application)
        } catch (e: Exception) {
            Log.e(TAG, "Error during bootstrap", e)
        }
    }
}
