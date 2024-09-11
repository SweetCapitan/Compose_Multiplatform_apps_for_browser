package org.company.app

import androidx.compose.runtime.Composable
import org.company.app.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
internal fun App() = AppTheme {
//    val lifecycle = LifecycleRegistry()
//
//    val kodein = DI {
//        bindSingleton { RootComponent(componentContext = DefaultComponentContext(lifecycle)) }
//    }
//
//    val controller: RootComponent by kodein.instance()
//    RootComposable(controller)
}

//@Composable
//private fun RootComposable(component: RootComponent) {
//    val model by component.model.subscribeAsState()
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .windowInsetsPadding(WindowInsets.safeDrawing)
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        StaticContent()
//        val urlParams = remember {
//            URLParamParser(urlString = getInfoFromUrl())
//                .getAllParams()
//        }
//        Row(horizontalArrangement = Arrangement.SpaceBetween) {
//            urlParams.forEach { (_, v) ->
//                Box {
//                    Text(v)
//                }
//            }
//        }
//        Text(component.onEvent("shit"))
//        AnimatedContent()
//    }
//}

//data class Model(val items: List<String>)
//
//class RootComponent(componentContext: ComponentContext) : ComponentContext by componentContext {
//    val model: Value<Model> = MutableValue(Model(List(100) { "Item$it" }))
//
//    fun onEvent(item: String): String {
//        return model.value.items[49]
//    }
//}

internal expect fun openUrl(url: String?)
