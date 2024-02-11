package navigation.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import navigation.ViewMode
import org.co.notes.NotesModel

class MainScreenComponent(
    componentContext: ComponentContext,
    private val navigateToScreen:(notesModel: NotesModel)->Unit
):ComponentContext by componentContext {
    var notesModel:NotesModel =NotesModel(null,"","",1,1,1)
    private var _notesState = MutableValue(listOf<NotesModel>())
    val noteList:Value<List<NotesModel>> get()  = _notesState
    fun onEvent(events: MainScreenEvents){
        when(events){
            is MainScreenEvents.NoteDetails -> {
                notesModel = events.notesModel
                navigateToScreen(notesModel)
            }

            is MainScreenEvents.SaveNote -> {
                navigateToScreen(notesModel)
                if(events.notesModel!=null){
                    val tempList = _notesState.value.toMutableList()
                    tempList.add(events.notesModel)
                    _notesState.value = tempList
                }
            }
        }
    }
}