package edu.gvsu.cis.cis357_carpenter_final_exam.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StarredCharacterDao {

    @Query("SELECT * FROM starred_characters")
    fun getAllStarred(): Flow<List<StarredCharacterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: StarredCharacterEntity)

    @Query("DELETE FROM starred_characters WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT COUNT(*) FROM starred_characters WHERE id = :id")
    suspend fun isStarred(id: Int): Int
}