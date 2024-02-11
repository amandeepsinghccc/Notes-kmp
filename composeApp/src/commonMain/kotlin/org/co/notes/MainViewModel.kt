package org.co.notes

import androidx.compose.ui.graphics.toArgb
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.co.notes.domain.DataSource

class MainViewModel(
    private val dataSource: DataSource
) : ViewModel() {
    private val _stateUi = MutableStateFlow<StateUi>(StateUi())
//    val stateUi: StateFlow<StateUi> get() = _stateUi.asStateFlow()

    val stateUi = combine(
        _stateUi,
        dataSource.getAllNotes()
    ) { state, contacts ->
        state.copy(
            notesList = contacts
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), StateUi())

    fun createNote(notesModel: NotesModel, onComplete: (id:Long) -> Unit) {
        viewModelScope.launch {
            val res = dataSource.addNote(
                notesModel
            )
            onComplete(res)
        }
//        println("************$tempList")
    }

    fun deleteNoteById(id: Long, onComplete: () -> Unit) {
        viewModelScope.launch {
            val result = dataSource.deleteNoteById(id)
            if (result) {
                onComplete()
            }
        }
    }

    fun setSelectedNote(model: NotesModel?) {
        _stateUi.update {
            it.copy(
                selectedModel = model
            )
        }
    }
}