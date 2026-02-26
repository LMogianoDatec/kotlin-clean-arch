package com.datec.app.core.network

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import android.util.Log
import com.datec.app.core.initialization.EnvConfig
import com.datec.app.core.initialization.Environment

class GlobalInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.d("ApiClient", "${request.method} ${request.url}")

        val response = chain.proceed(request)

        val responseBody = response.body
        if (responseBody.toString().isBlank()) {
            Log.w("ApiClient", "Empty body: ${response.code}")
            return response
        }

        val content = responseBody.string()
        val newBody = content.toResponseBody(responseBody.contentType())

        if (EnvConfig.environment == Environment.DEV) {
            if (response.isSuccessful) {
                Log.d("ApiClient", "✓ ${request.method} ${request.url} -> ${response.code}\n$content")
            } else {
                Log.e("ApiClient", "✗ ${request.method} ${request.url} -> ${response.code}\n$content")
            }
        }

        return response.newBuilder().body(newBody).build()
    }
}