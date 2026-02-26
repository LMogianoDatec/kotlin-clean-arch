package com.datec.app.core.initialization

import android.content.Context
import com.datec.app.BuildConfig
import com.datec.app.core.services.LogService

enum class Environment {


    DEV, QA, PROD;

    companion object {
        fun fromFlavor(flavor: String): Environment = when (flavor.lowercase()) {
            "dev" -> DEV
            "qa" -> QA
            "prod" -> PROD
            else -> PROD
        }
    }
}

object EnvConfig {
    private var values: Map<String, String> = emptyMap()
        private const val TAG = "EnvConfig"
        var loaded: Boolean = false
            private set
    val environment: Environment = Environment.fromFlavor(BuildConfig.FLAVOR)

    val API_BASE_URL: String
        get() = get("API_BASE_URL", "https://api.example.com")

    fun load(context: Context) {
        val fileName = "env.${BuildConfig.FLAVOR}"
        LogService.d(TAG) { "ENVIRONMENT: ${environment.name}" }
        LogService.i(TAG) { "FILE ENV: $fileName" }

        val assetsList = try {
            context.assets.list("")?.joinToString(", ")
        } catch (e: Exception) {
            null
        }
        LogService.d(TAG) { "Top-level assets: ${assetsList ?: "<unavailable>"}" }

        val text = context.assets.open(fileName).bufferedReader().use { it.readText() }
        values = text.lines()
            .mapNotNull { line ->
                val l = line.trim()
                if (l.isEmpty() || l.startsWith("#")) return@mapNotNull null
                val idx = l.indexOf('=')
                if (idx <= 0) return@mapNotNull null
                val key = l.substring(0, idx).trim()
                val value = l.substring(idx + 1).trim()
                key to value
            }
            .toMap()
    }

    fun get(key: String, default: String = ""): String = values[key] ?: default

}
