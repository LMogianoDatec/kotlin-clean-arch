package com.datec.app.module.data.datasources

import com.datec.app.core.network.ApiClient
import com.datec.app.module.data.models.BookDto
import javax.inject.Inject
import javax.inject.Named

class BookRemoteDataSource @Inject constructor(
    @param:Named("books") private val client: ApiClient
) {

    suspend fun getBooks(): List<BookDto> {
        return client.get(
            path = "/books",
            headers = mapOf(
                "Authorization" to "Bearer TU_TOKEN_REAL_AQUI"
            ),
            dataKey = "books"
        )
    }

    suspend fun searchBooks(
        query: String,
        maxResults: Int
    ): List<BookDto> {

        val body = {
            "query" to query
            "maxResults" to maxResults
        }

        return client.post(
            path = "/search",
            bodyObj = body
        )
    }
}