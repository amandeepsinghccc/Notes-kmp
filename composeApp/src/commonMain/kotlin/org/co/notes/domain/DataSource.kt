package org.co.notes.domain

import kotlinx.coroutines.flow.Flow
import org.co.notes.NotesModel

interface DataSource {
    fun getAllNotes(): Flow<List<NotesModel>>
    suspend fun addNote(notesModel:NotesModel):Long
    suspend fun deleteNoteById(id:Long):Boolean
}