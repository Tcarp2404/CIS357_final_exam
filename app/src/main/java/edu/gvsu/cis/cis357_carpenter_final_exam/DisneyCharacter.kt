package edu.gvsu.cis.cis357_carpenter_final_exam.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DisneyCharacter(
    @SerialName("_id") val id: Int,
    val name: String,
    val films: List<String> = emptyList(),
    val tvShows: List<String> = emptyList(),
    val allies: List<String> = emptyList(),
    val enemies: List<String> = emptyList(),
    val imageUrl: String? = null
)
