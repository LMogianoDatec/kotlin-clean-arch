package com.datec.app.core.services

import android.util.Log

/**
 * Lightweight logging service that prefixes logs with emojis and avoids
 * unnecessary string work by accepting message lambdas (inlined).
 */
object LogService {
    const val DEFAULT_TAG = "APP"

    inline fun d(tag: String = DEFAULT_TAG, crossinline msg: () -> String) {
        try {
            val m = msg()
            Log.d(tag, "🐛 $m")
        } catch (_: Exception) {
        }
    }

    inline fun i(tag: String = DEFAULT_TAG, crossinline msg: () -> String) {
        try {
            val m = msg()
            Log.i(tag, "ℹ️ $m")
        } catch (_: Exception) {
        }
    }

    inline fun w(tag: String = DEFAULT_TAG, crossinline msg: () -> String) {
        try {
            val m = msg()
            Log.w(tag, "⚠️ $m")
        } catch (_: Exception) {
        }
    }

    inline fun e(tag: String = DEFAULT_TAG, throwable: Throwable? = null, crossinline msg: () -> String) {
        try {
            val m = msg()
            if (throwable != null) Log.e(tag, "❌ $m", throwable) else Log.e(tag, "❌ $m")
        } catch (_: Exception) {
        }
    }

    inline fun success(tag: String = DEFAULT_TAG, crossinline msg: () -> String) {
        try {
            val m = msg()
            Log.i(tag, "✅ $m")
        } catch (_: Exception) {
        }
    }
}
