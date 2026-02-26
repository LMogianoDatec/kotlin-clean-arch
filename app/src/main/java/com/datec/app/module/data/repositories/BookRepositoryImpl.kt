package com.datec.app.module.data.repositories

import com.datec.app.module.data.datasources.BookRemoteDataSource
import com.datec.app.module.data.models.toDomain
import com.datec.app.module.domain.entity.Book
import com.datec.app.module.domain.repositories.BookRepository
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val remoteDataSource: BookRemoteDataSource
) : BookRepository {

    override suspend fun getBooks(): List<Book> {
        return remoteDataSource
            .getBooks()
            .map { it.toDomain() }
    }

    override suspend fun searchBooks(
        query: String,
        maxResults: Int
    ): List<Book> {
        return remoteDataSource
            .searchBooks(query, maxResults)
            .map { it.toDomain() }
    }
}