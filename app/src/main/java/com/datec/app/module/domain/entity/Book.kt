package com.datec.app.module.domain.entity

data class Book(
    val id: String,
    val title: String,
    val authors: List<String>,
    val subtitle: String? = null,
    val description: String? = null,
    val thumbnailUrl: String? = null,
    val pageCount: Int? = null,
    val averageRating: Double? = null,
    val categories: List<String>? = null,
    val language: String? = null,
    val shelf: String? = null,
)