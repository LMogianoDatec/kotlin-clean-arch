package com.datec.app.module.domain.repositories

import com.datec.app.module.domain.entity.Book

interface BookRepository {

    suspend fun getBooks(): List<Book>

    suspend fun searchBooks(
        query: String,
        maxResults: Int
    ): List<Book>
}