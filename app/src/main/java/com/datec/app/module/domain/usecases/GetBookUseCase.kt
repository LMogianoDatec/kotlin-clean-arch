package com.datec.app.module.domain.usecases

import com.datec.app.module.domain.entity.Book
import com.datec.app.module.domain.repositories.BookRepository
import javax.inject.Inject
import kotlin.collections.filter

class GetBooksUseCase @Inject constructor(
    private val repository: BookRepository
) {

    suspend operator fun call(): List<Book> {
        val books = repository.getBooks()

        // val filteredBooks = books.filter { it.pageCount ?: 0 > 100 }
        val sortedBooks = filteredBooks.sortedBy { it.title }

        return filteredBooks
    }
}