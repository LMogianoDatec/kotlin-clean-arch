package com.datec.app.module.data.models

import kotlinx.serialization.Serializable
import com.datec.app.module.domain.entity.Book

@Serializable
data class BookDto(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val authors: List<String> = emptyList(),
    val publisher: String? = null,
    val publishedDate: String? = null,
    val description: String? = null,
    val industryIdentifiers: List<IndustryIdentifierDto>? = null,
    val readingModes: ReadingModesDto? = null,
    val pageCount: Int? = null,
    val printType: String? = null,
    val categories: List<String>? = null,
    val averageRating: Double? = null,
    val ratingsCount: Int? = null,
    val maturityRating: String? = null,
    val allowAnonLogging: Boolean? = null,
    val contentVersion: String? = null,
    val panelizationSummary: PanelizationSummaryDto? = null,
    val imageLinks: ImageLinksDto? = null,
    val language: String? = null,
    val previewLink: String? = null,
    val infoLink: String? = null,
    val canonicalVolumeLink: String? = null,
    val shelf: String? = null,
)

@Serializable
data class IndustryIdentifierDto(
    val type: String? = null,
    val identifier: String? = null,
)

@Serializable
data class ReadingModesDto(
    val text: Boolean? = null,
    val image: Boolean? = null,
)

@Serializable
data class PanelizationSummaryDto(
    val containsEpubBubbles: Boolean? = null,
    val containsImageBubbles: Boolean? = null,
)

@Serializable
data class ImageLinksDto(
    val thumbnail: String? = null,
    val smallThumbnail: String? = null,
)

fun BookDto.toDomain(): Book = Book(
    id = this.id,
    title = this.title,
    authors = this.authors,
    subtitle = this.subtitle,
    description = this.description,
    thumbnailUrl = this.imageLinks?.thumbnail ?: this.imageLinks?.smallThumbnail,
    pageCount = this.pageCount,
    averageRating = this.averageRating,
    categories = this.categories,
    language = this.language,
    shelf = this.shelf
)