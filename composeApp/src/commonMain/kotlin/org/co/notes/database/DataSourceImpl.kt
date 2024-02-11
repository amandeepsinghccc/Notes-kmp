package org.co.notes.database

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.flow
import org.co.notes.NotesModel
import org.co.notes.domain.DataSource

class DataSourceImpl(
    database: NotesDatabase
):DataSource {
    private val dbQuery = database.notesDatabaseQueries
    override fun getAllNotes(): Flow<List<NotesModel>> {
        return dbQuery.getAllNotes(::getNotesModelFromNotes).asFlow().mapToList()
    }

    override suspend fun addNote(notesModel: NotesModel):Long {
        var lastInsertedId:Long = -1
        return try {
            dbQuery.transaction {
                dbQuery.insertNote(
                    notesModel.id,
                    notesModel.title,
                    notesModel.body,
                    notesModel.colorHex,
                    notesModel.createdDate,
                    notesModel.updatedDate
                )
                lastInsertedId = dbQuery.getLastId().executeAsOne()
            }
            lastInsertedId
        } catch (e: Exception) {
            lastInsertedId
        }
    }

    override suspend fun deleteNoteById(id: Long):Boolean {
        return try {
            dbQuery.transaction {
                dbQuery.deleteNoteById(id)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun getNotesModelFromNotes(
        id: Long,
        title: String,
        body: String,
        colorHex: Long,
        created: Long,
        updated: Long
    ): NotesModel {
        return NotesModel(
            id = id,
            title = title,
            body = body,
            colorHex = colorHex,
            updatedDate = updated,
            createdDate = created
        )
    }
}