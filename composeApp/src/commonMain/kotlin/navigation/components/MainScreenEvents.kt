package navigation.components

import navigation.ViewMode
import org.co.notes.NotesModel

sealed interface MainScreenEvents {
    data class SaveNote(val notesModel: NotesModel?):MainScreenEvents
    data class NoteDetails(val notesModel: NotesModel):MainScreenEvents
}