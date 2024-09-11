package org.company.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: String,
    val author: String,
    val title: String,
    val description: String,
)

interface DetailComponent {
    val model: Value<Post> // 1

    fun onBackPressed() // 2
}

class DefaultDetailComponent(
    componentContext: ComponentContext,
    post: Post,
    private val onFinished: () -> Unit,
) : DetailComponent, ComponentContext by componentContext {

    override val model: Value<Post> = MutableValue(post)

    override fun onBackPressed() = onFinished()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailContent(
    component: DetailComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.model.subscribeAsState() // 1

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Detail") },
                navigationIcon = {
                    IconButton(onClick = component::onBackPressed) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(state.title)
            Text(state.description)
            Text(state.author)
        }
    }
}

interface ListComponent {
    val model: Value<List<Post>>

    fun onPostClicked(post: Post)
}

class DefaultListComponent(
    componentContext: ComponentContext,
    private val postClicked: (Post) -> Unit,
) : ListComponent, ComponentContext by componentContext {

    override val model: Value<List<Post>> = MutableValue(
        (0..16).map { // 1
            Post(
                id = it.toString(),
                title = "Title-#$it",
                description = "Description-#$it",
                author = "Author-#$it",
            )
        }
    )

    override fun onPostClicked(post: Post) = postClicked(post) // 2
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListContent(
    component: ListComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.model.subscribeAsState()

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("List") }) }
    ) { paddingValues ->
        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier.padding(paddingValues)
        ) {
            items(state) { post ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { component.onPostClicked(post) }
                        .padding(16.dp)
                ) {
                    Text(post.title)
                }
            }
        }
    }
}

interface RootComponent {
    val stack: Value<ChildStack<*, Child>> // 1

    sealed interface Child { // 2
        class List(val component: ListComponent) : Child
        class Detail(val component: DetailComponent) : Child
    }
}

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    private val nav = StackNavigation<Config>() // 1

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack( // 2
        source = nav,
        serializer = Config.serializer(),
        initialConfiguration = Config.List,
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child = when (config) { // 3
        Config.List -> RootComponent.Child.List(
            DefaultListComponent(
                componentContext = componentContext,
                postClicked = { post ->
                    nav.pushNew(Config.Detail(post)) // 4
                }
            )
        )

        is Config.Detail -> RootComponent.Child.Detail(
            DefaultDetailComponent(
                componentContext = componentContext,
                post = config.post,
                onFinished = {
                    nav.pop() // 5
                },
            )
        )
    }

    @Serializable
    private sealed interface Config { // 6
        @Serializable
        data object List : Config

        @Serializable
        data class Detail(val post: Post) : Config
    }
}

@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier,
) {
    Children( // 1
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation(slide()), // 2
    ) {
        when (val child = it.instance) { // 3
            is RootComponent.Child.Detail -> DetailContent(
                component = child.component,
                modifier = Modifier.fillMaxSize(),
            )

            is RootComponent.Child.List -> ListContent(
                component = child.component,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}