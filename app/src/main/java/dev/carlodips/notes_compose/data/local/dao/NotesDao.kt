package dev.carlodips.notes_compose.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.carlodips.notes_compose.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note): Long

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM Note WHERE noteId = :id")
    suspend fun getNoteById(id: Int): Note?

    @Query("SELECT * FROM Note")
    fun getNotes(): Flow<List<Note>>

    @Query("UPDATE Note SET isHidden = :isHidden WHERE noteId = :noteId")
    suspend fun setHiddenNote(
        noteId: Int,
        isHidden: Boolean
    )

    @Query("UPDATE Note SET isLocked = :isLocked WHERE noteId = :noteId")
    suspend fun setLockedNote(
        noteId: Int,
        isLocked: Boolean
    )
}
