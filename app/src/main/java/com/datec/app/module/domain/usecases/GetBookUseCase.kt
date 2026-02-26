package com.datec.app.module.domain.usecases

import com.datec.app.module.domain.entity.Book
import com.datec.app.module.domain.repositories.BookRepository
import javax.inject.Inject
import kotlin.collections.filter

class GetBooksUseCase @Inject constructor(
    private val repository: BookRepository
) {

    suspend operator fun invoke(): List<Book> {
        val books = repository.getBooks()

        return books
    }
}