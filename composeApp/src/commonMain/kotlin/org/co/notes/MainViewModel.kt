package org.co.notes

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
import org.co.notes.domain.DateTimeUtil

class MainViewModel(
    private val dataSource: DataSource
) : ViewModel() {
    private val _stateUi = MutableStateFlow(StateUi())
//    val stateUi: StateFlow<StateUi> get() = _stateUi.asStateFlow()
    val stateUi:StateFlow<StateUi> get() = _stateUi.asStateFlow()
  /*  val stateUi = combine(
        _stateUi,
        dataSource.getAllNotes()
    ) { state, contacts ->
        state.copy(
            notesList = contacts
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), StateUi())*/

    init {
        viewModelScope.launch {
            dataSource.getAllNotes().collect{list->
                _stateUi.update {
                    it.copy(
                        notesList = list
                    )
                }
            }
        }
    }
    fun createNote(notesModel: NotesModel, onComplete: (id: Long) -> Unit) {
        viewModelScope.launch {
            val res = dataSource.addNote(
                if (notesModel.id != null) {
                    notesModel.copy(updatedDate = DateTimeUtil.toEpochMillis(DateTimeUtil.now()))
                } else {
                    notesModel.copy(
                        createdDate = DateTimeUtil.toEpochMillis(DateTimeUtil.now()),
                        updatedDate = DateTimeUtil.toEpochMillis(DateTimeUtil.now())
                    )
                }
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

    fun sortList(sortBy: Int) {
        viewModelScope.launch {
            if (sortBy != 0) {
                val tempList = stateUi.value.notesList.toMutableList().sortedBy {
                    when (sortBy) {
                        1 -> {
                            it.updatedDate
                        }

                        2 -> {
                            it.createdDate
                        }

                        else -> {
                            it.createdDate
                        }
                    }

                }
                _stateUi.value = _stateUi.value.copy(notesList = tempList)

            } else {
                val tempList = stateUi.value.notesList.toMutableList().sortedBy {
                    it.title
                }
                _stateUi.value = _stateUi.value.copy(notesList = tempList)
            }
            println("Random list ${stateUi.value.notesList}")
        }
    }
}