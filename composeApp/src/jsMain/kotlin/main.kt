import RComponent.Child
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.start
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.company.app.Post
import org.company.app.theme.CssTheme
import org.company.app.theme.GlobalStyles
import org.company.app.theme.OverrideStyles
import org.jetbrains.compose.web.dom.*;
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.renderComposable

@OptIn(ExperimentalComposeUiApi::class, DelicateCoroutinesApi::class, InternalComposeApi::class)
fun main() {
    val lc = LifecycleRegistry()

    val root = DefaultRComponent(
        componentContext = DefaultComponentContext(lifecycle = lc)
    )
    lc.start()
    renderComposable(rootElementId = "root") {
        CssTheme {
            P { Text(
                """
                |Hello. This page is written entirely in Compose HTML.
                |I used Decompose for state storage and basic navigation.
                |It's lame, I know, but in the end it's proof-of-concept :D
                """.trimMargin()) }
            Style(GlobalStyles.current.value)
            MyBody()
            Button({
                onClick {
                    OverrideStyles.override {
                        "button" style {
                            color(Color.blueviolet)
                        }
                    }
                }
            }) {
                Text("Hey its colored button!")
            }
            val root3 = DefaultRComponent(DefaultComponentContext(lc))
            InteractiveComponent(component = root3)
            CupertinoButtons()
        }
        Div({
            classes("deep")
            style { margin(5.px) }
        }) { Text("BoxShadow impl test!") }
        RootContentHTML(root)
    }

//    onWasmReady {
//        CanvasBasedWindow("Multi") {
//            org.company.app.RootContent(root2, Modifier.fillMaxSize())
//        }
//    }
}

@Composable
fun RootContentHTML(component: RComponent) {
    val stack by component.stack.subscribeAsState()
    when (val child = stack.active.instance) {
        is Child.DetailsChild -> DetailsContent(child.component)
        is Child.ListChild -> ListContent(child.component)
    }
}

@Composable
fun CupertinoButtons() {
    Div({ classes("container", "column") }) {
        repeat(5) {
            Div({
                onClick { window.alert("Wow u pressed button!") }
                classes("cupertino-button", "item")
            }) { Text("Кнопки в стиле Cupertino") }
        }
    }
}

@Composable
fun DetailsContent(component: DetailsComponent) {
    val state by component.model.subscribeAsState()
    Button({
        onClick { component.onBackPressed() }
    }) { Text("Go back") }
    Div({
        classes("container", "column")
    }) {
        TextItem(state.id)
        TextItem(state.title)
        TextItem(state.description)
        TextItem(state.author)
    }
}

@Composable
fun TextItem(text: String) {
    Div({ classes("item") }) { Text(text) }
}

@Composable
fun ListContent(component: ListComponent) {
    val model by component.model.subscribeAsState()
    model.items.forEach { item ->
        Div({
            onClick { component.onItemClicked(item) }
        }) {
            Text(item.title)
        }
    }
}

interface RComponent {
    val temp: Value<Temp>
    fun changeTemp(applier: Temp)
    fun fetch()
    val text: Value<String>
    val stack: Value<ChildStack<*, Child>>
    fun onBackClicked(toIndex: Int)

    sealed class Child {
        class ListChild(val component: ListComponent) : Child()
        class DetailsChild(val component: DetailsComponent) : Child()
    }
}

class DefaultRComponent(componentContext: ComponentContext) : RComponent, ComponentContext by componentContext {
    private var _temp: MutableValue<Temp> = MutableValue(Temp(10, 20))
    override val temp: Value<Temp> = _temp
    override fun changeTemp(applier: Temp) {
        _temp.value = applier
    }

    override fun fetch() {
        this.coroutineScope().launch {
            val s = window
                .fetch("https://jsonplaceholder.typicode.com/posts")
                .await()
                .text()
                .await()

            println(s)
            Json.decodeFromString<List<TodoListItem>>(s)
                .let {
                    println(it)
                    _text.value = it.first().title
                }
        }
    }

    private var _text = MutableValue("Press me and i fetch some data from jsonplaceholder.com")
    override val text: Value<String>
        get() = _text

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.List,
            handleBackButton = true,
            childFactory = ::child
        )

    private fun child(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            is Config.Details -> Child.DetailsChild(detailsComponent(componentContext, config))
            is Config.List -> Child.ListChild(listComponent(componentContext))
        }

    private fun listComponent(componentContext: ComponentContext): ListComponent =
        DefaultListComponent(
            componentContext = componentContext,
            onItemSelected = {  // Supply dependencies and callbacks
                navigation.push(Config.Details(post = it)) // Push the details component
            },
        )

    private fun detailsComponent(componentContext: ComponentContext, config: Config.Details): DetailsComponent =
        DefaultDetailsComponent(
            componentContext = componentContext,
            post = config.post, // Supply arguments from the configuration
            onFinished = navigation::pop, // Pop the details component
        )

    override fun onBackClicked(toIndex: Int) {
        navigation.popTo(index = toIndex)
    }

    @Serializable // kotlinx-serialization plugin must be applied
    private sealed interface Config {
        @Serializable
        data object List : Config

        @Serializable
        data class Details(val post: Post) : Config
    }
}

interface DetailsComponent {
    val model: Value<Post> // 1
    fun onBackPressed() // 2
}

class DefaultDetailsComponent(
    val componentContext: ComponentContext,
    val post: Post,
    private val onFinished: () -> Unit
) : DetailsComponent {
    override val model: Value<Post> = MutableValue(post)

    override fun onBackPressed() = onFinished()
}

interface ListComponent {
    val model: Value<Model>

    fun onItemClicked(post: Post)
}

class DefaultListComponent(
    componentContext: ComponentContext,
    private val onItemSelected: (post: Post) -> Unit,
) : ListComponent {
    override val model: Value<Model> =
        MutableValue(Model(items = (0..100).map {
            Post(
                "ID $it",
                "Author $it",
                "Title $it",
                "Description $it"
            )
        }))

    override fun onItemClicked(post: Post) {
        onItemSelected(post)
    }
}

data class Model(val items: List<Post>)

data class Temp(val c: Number, val f: Number)

@Composable
fun InteractiveComponent(component: RComponent) {
    val temp by component.temp.subscribeAsState()
    val text by component.text.subscribeAsState()
    Div({
        onClick { component.changeTemp(Temp(213, 23231)) }
    }) {
        Text("Temp is ${temp.c} and ${temp.f}. Press to change!")
    }
    Div({ onClick { component.fetch() } }) {
        Text(text)
    }
}

@Composable
fun MyBody() {
    Div({
        classes("container")
    }) {
        repeat(5) {
            Div({
                classes("item")
            }) {
                Text("Hello Kotlin!")
            }
        }
    }
}
