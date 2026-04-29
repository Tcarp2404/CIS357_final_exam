package edu.gvsu.cis.cis357_carpenter_final_exam.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "starred_characters")
data class StarredCharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String?
)