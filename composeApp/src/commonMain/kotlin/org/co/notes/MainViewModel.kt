package org.co.notes

import androidx.compose.ui.input.key.Key.Companion.Calendar
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _notesList = MutableStateFlow<List<NotesModel>>(listOf())
    val notesList : StateFlow<List<NotesModel>> get() = _notesList.asStateFlow()

    fun createNote(title:String,body:String){
        val tempList = _notesList.value.toMutableList()
        tempList.add(NotesModel(title,body,"",""))
        _notesList.value = tempList
    }
}