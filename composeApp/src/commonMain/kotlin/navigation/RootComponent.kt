package navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import kotlinx.serialization.Serializable
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.pushNew
import navigation.components.EditScreenComponent
import navigation.components.MainScreenComponent
import org.co.notes.NotesModel

class RootComponent(componentContext: ComponentContext):ComponentContext by componentContext {
    private val navigation = StackNavigation<Configuration>()
    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.MainScreen ,
        handleBackButton = true,
        childFactory = ::createChild
    )
    sealed class Child {
        data class MainScreen(val mainScreenComponent: MainScreenComponent) : Child()
        data class EditScreen(val editScreenComponent: EditScreenComponent) : Child()
    }

    @OptIn(ExperimentalDecomposeApi::class)
    private fun createChild(
        config: Configuration,
        context: ComponentContext
    ): Child {
        return when (config) {
            is Configuration.MainScreen -> Child.MainScreen(MainScreenComponent(context, navigateToScreen ={
                navigation.pushNew(Configuration.EditScreen(it))
            } ))
            is Configuration.EditScreen -> Child.EditScreen(
                EditScreenComponent(
                    config.notesModel,
                    context,
                    onGoBack = {
                        navigation.pop()
                    }
                )
            )
        }
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object MainScreen : Configuration()

        @Serializable
        data class EditScreen(val notesModel: NotesModel) : Configuration()
    }
}

enum class ViewMode {
    ADD_NOTE,
    DELETE_NOTE,
    EDIT_NOTE
}