package edu.gvsu.cis.cis357_carpenter_final_exam.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PageInfo(
    val totalPages: Int? = null,
    val count: Int? = null,
    val previousPage: String? = null,
    val nextPage: String? = null
)

@Serializable
data class DisneyApiResponse(
    val info: PageInfo? = null,
    val data: List<DisneyCharacter> = emptyList()
)
