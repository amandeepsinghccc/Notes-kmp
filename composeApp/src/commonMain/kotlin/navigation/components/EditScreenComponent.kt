package navigation.components

import com.arkivanov.decompose.ComponentContext
import navigation.ViewMode
import org.co.notes.NotesModel

class EditScreenComponent (
    val notesModel: NotesModel,
    componentContext: ComponentContext,
    private val onGoBack:()->Unit
): ComponentContext by componentContext {
    fun goBack(){
        onGoBack()
    }
}