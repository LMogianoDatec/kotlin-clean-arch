package com.datec.app.core.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*
import kotlinx.serialization.serializer
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.util.concurrent.TimeUnit

class ApiClient(
    okHttpClient: OkHttpClient? = null,
    private val baseUrl: String,
    connectTimeoutSeconds: Long = 10,
    readTimeoutSeconds: Long = 10,
    interceptors: List<Interceptor>? = null,
) {

    val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    val client: OkHttpClient

    init {
        val builder = okHttpClient?.newBuilder()
            ?: OkHttpClient.Builder()

        builder
            .connectTimeout(connectTimeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(readTimeoutSeconds, TimeUnit.SECONDS)

        interceptors?.forEach { interceptor ->
            if (!builder.interceptors().any { it::class == interceptor::class }) {
                builder.addInterceptor(interceptor)
            }
        }

        if (!builder.interceptors().any { it is GlobalInterceptor }) {
            builder.addInterceptor(GlobalInterceptor())
        }

        client = builder.build()
    }

    fun buildUrl(
        path: String,
        query: Map<String, String>?
    ): String {
        val base = baseUrl.trimEnd('/')
        val cleanPath = path.trimStart('/')

        val httpUrlBuilder = "$base/$cleanPath"
            .toHttpUrl()
            .newBuilder()

        query?.forEach { (k, v) ->
            httpUrlBuilder.addQueryParameter(k, v)
        }

        return httpUrlBuilder.build().toString()
    }

    fun buildRequest(
        method: String,
        url: String,
        headers: Map<String, String>?,
        body: RequestBody?
    ): Request {
        val builder = Request.Builder().url(url)

        headers?.forEach { (k, v) ->
            builder.addHeader(k, v)
        }

        when (method.uppercase()) {
            "GET" -> builder.get()
            "POST" -> builder.post(body ?: EMPTY)
            "PUT" -> builder.put(body ?: EMPTY)
            "PATCH" -> builder.patch(body ?: EMPTY)
            "DELETE" -> if (body != null) builder.delete(body) else builder.delete()
        }

        return builder.build()
    }

    suspend inline fun <reified T> get(
        path: String,
        headers: Map<String, String>? = null,
        query: Map<String, String>? = null,
        dataKey: String? = null,
        noinline fallback: (() -> T)? = null,
    ): T = request("GET", path, headers, query, null, fallback, dataKey)

    suspend inline fun <reified T> post(
        path: String,
        bodyObj: Any? = null,
        headers: Map<String, String>? = null,
        dataKey: String? = null,
    ): T = request("POST", path, headers, null, bodyObj, null, dataKey)

    suspend inline fun <reified T>  request(
        method: String,
        path: String,
        headers: Map<String, String>?,
        query: Map<String, String>?,
        bodyObj: Any?,
        noinline fallback: (() -> T)?,
        dataKey: String? = null
    ): T = withContext(Dispatchers.IO) {
        try {
            val url = buildUrl(path, query)

            val body = bodyObj?.let {
                val serializer = json.serializersModule.serializer(it::class.java)
                json.encodeToString(serializer, it)
                    .toRequestBody(JSON)
            }

            val request = buildRequest(method, url, headers, body)

            client.newCall(request).execute().use { response ->
                val bodyString = response.body.string()

                if (!response.isSuccessful) {
                    throw ApiException(
                        code = response.code,
                        message = bodyString
                    )
                }

                if (bodyString.isBlank()) {
                    throw ApiException(message = "Empty response body")
                }

                try {
                    return@withContext json.decodeFromString<T>(bodyString)
                } catch (_: Exception) {
                    val element = Json.parseToJsonElement(bodyString)
                    if (element is JsonObject) {
                        // prefer explicit dataKey when provided
                        if (dataKey != null && element[dataKey] != null) {
                            return@withContext json.decodeFromJsonElement(element[dataKey]!!)
                        }
                        // fallback to common wrapper key
                        if (element["data"] != null) {
                            return@withContext json.decodeFromJsonElement(element["data"]!!)
                        }
                    }
                    throw ApiException(message = "Invalid JSON response")
                }
            }
        } catch (e: Exception) {
            if (fallback != null) return@withContext fallback()
            throw when (e) {
                is ApiException -> e
                else -> ApiException(message = e.message ?: "Network error")
            }
        }
    }

    companion object {
        val JSON = "application/json; charset=utf-8".toMediaType()
        private val EMPTY = ByteArray(0).toRequestBody(null)
    }
}