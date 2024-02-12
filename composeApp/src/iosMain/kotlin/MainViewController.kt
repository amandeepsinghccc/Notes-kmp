import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.lifecycle.ApplicationLifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import navigation.RootComponent
import org.co.notes.di.AppModule
import org.koin.core.context.startKoin

@OptIn(ExperimentalDecomposeApi::class)
fun MainViewController(root:RootComponent) = ComposeUIViewController {

    App(root, AppModule())
}
fun initKoin(){
    startKoin{
        modules()
    }
}
