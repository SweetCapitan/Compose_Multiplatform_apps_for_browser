import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.CanvasBasedWindow
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.company.app.DefaultRootComponent
import org.company.app.RootContent

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val lifycycle = LifecycleRegistry()

    val root = DefaultRootComponent(DefaultComponentContext(lifycycle))

    CanvasBasedWindow("Multiplatform App") {
//        App()
        RootContent(root, Modifier.fillMaxSize())
    }
}