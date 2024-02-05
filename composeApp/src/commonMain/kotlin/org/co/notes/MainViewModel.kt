package org.co.notes

import androidx.compose.ui.input.key.Key.Companion.Calendar
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    private val _notesList = MutableStateFlow<StateUi>(StateUi())
    val notesList: StateFlow<StateUi> get() = _notesList.asStateFlow()

    /*init {
        val tempList = listOf(
            NotesModel("dasdhfsdafsd","","",""),
            NotesModel("dasdhfsdafsd","","",""),
            NotesModel("dasdhfsdafsd","","",""),
            NotesModel("dasdhfsdafsd","","",""),
            NotesModel("dasdhfsdafsd","","",""),
            NotesModel("dasdhfsdafsd","","",""),
            NotesModel("dasdhfsdafsd","","",""),
            NotesModel("dasdhfsdafsd","","",""),
            NotesModel("dasdhfsdafsd","","",""),
            NotesModel("dasdhfsdafsd","","",""),

        )
        _notesList.update {
            it.copy(
                notesList = tempList
            )
        }
    }*/

    fun createNote(title: String, body: String) {
        val tempList = _notesList.value.notesList.toMutableList()
        tempList.add(NotesModel(title, body, "", ""))
        _notesList.update {
            it.copy(
                notesList = tempList
            )
        }
        println("************$tempList")
    }
}