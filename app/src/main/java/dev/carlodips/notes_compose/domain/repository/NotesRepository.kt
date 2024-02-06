package dev.carlodips.notes_compose.domain.repository

import dev.carlodips.notes_compose.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun insertNote(note: Note): Long

    suspend fun deleteNote(note: Note)

    suspend fun getNoteById(id: Int): Note?

    fun getNotes(): Flow<List<Note>>

    suspend fun setArchivedNote(noteId: Int, isArchived: Boolean)

    suspend fun setLockedNote(noteId: Int, isLocked: Boolean)
}
