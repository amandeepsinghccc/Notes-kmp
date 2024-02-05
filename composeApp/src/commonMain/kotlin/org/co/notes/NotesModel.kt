package org.co.notes

data class NotesModel(
    val title:String,
    val body:String,
    val createdDate:String,
    val updatedDate:String
)
data class StateUi(
    val notesList : List<NotesModel> = listOf()
)