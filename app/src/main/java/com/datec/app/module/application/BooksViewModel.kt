package com.datec.app.module.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.datec.app.module.domain.entity.Book
import com.datec.app.module.domain.repositories.BookRepository
import com.datec.app.core.network.ApiException


sealed class HomeState {
    object Idle : HomeState()
    object Loading : HomeState()
    data class Loaded(val books: List<Book>) : HomeState()
    data class Error(val message: String) : HomeState()
}

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    // Keep simple list flow for existing UI consumers
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books.asStateFlow()

    // New UI state flow (Loading / Loaded / Error)
    private val _state = MutableStateFlow<HomeState>(HomeState.Idle)
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun getBooks() {
        viewModelScope.launch {
            _state.value = HomeState.Loading
            try {
                val result = bookRepository.getBooks()
                _books.value = result
                _state.value = HomeState.Loaded(result)
            } catch (e: Exception) {
                val message = when (e) {
                    is ApiException -> e.message ?: "API error"
                    else -> e.message ?: "Unknown error"
                }
                _books.value = emptyList()
                _state.value = HomeState.Error(message)
            }
        }
    }

    fun searchBooks(query: String, maxResults: Int = 10) {
        viewModelScope.launch {
            _state.value = HomeState.Loading
            try {
                val result = bookRepository.searchBooks(query, maxResults)
                _books.value = result
                _state.value = HomeState.Loaded(result)
            } catch (e: Exception) {
                val message = when (e) {
                    is ApiException -> e.message ?: "API error"
                    else -> e.message ?: "Unknown error"
                }
                _books.value = emptyList()
                _state.value = HomeState.Error(message)
            }
        }
    }
}
