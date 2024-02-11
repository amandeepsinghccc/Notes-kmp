package org.co.notes.database

import org.co.notes.NotesModel

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = NotesDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.notesDatabaseQueries

    internal fun deleteNoteById(id: Long): Boolean {
        return try {
            dbQuery.transaction {
                dbQuery.deleteNoteById(id)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    internal fun getAllNotes(): List<NotesModel> {
        return dbQuery.getAllNotes(::getNotesModelFromNotes).executeAsList()
    }

    internal fun addNote(notesModel: NotesModel): Boolean {
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

