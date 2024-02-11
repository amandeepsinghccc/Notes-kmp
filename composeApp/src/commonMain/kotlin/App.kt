import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import navigation.RootComponent
import org.co.notes.MainViewModel
import org.co.notes.appTheme
import org.co.notes.di.AppModule
import org.co.notes.screens.EditScreen
import org.co.notes.screens.MainScreen

@Composable
fun App(rootComponent: RootComponent, appModule: AppModule) {
    appTheme(darkTheme = false,true) {
        val childStack by rootComponent.childStack.subscribeAsState()
        val viewModel = getViewModel(
            key = "notes",
            factory = viewModelFactory {
                MainViewModel(appModule.dataSource)
            }
        )
        val state by viewModel.stateUi.collectAsState()
        LaunchedEffect(state.notesList){
            println("What list ${state.notesList}")
        }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Children(
                stack = childStack,
                animation = stackAnimation(slide())
            ){
                when(val instance = it.instance){
                    is RootComponent.Child.EditScreen -> {
                        EditScreen(viewModel,state,instance.editScreenComponent)
                    }
                    is RootComponent.Child.MainScreen -> {
                        MainScreen(mainViewModel = viewModel,state,instance.mainScreenComponent)
                    }
                }
            }
        }
    }
}