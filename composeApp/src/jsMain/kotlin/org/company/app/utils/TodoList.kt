
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName
@Serializable
class TodoList : ArrayList<TodoListItem>()

@Serializable
data class TodoListItem(
    @SerialName("body")
    val body: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("title")
    val title: String = "",
    @SerialName("userId")
    val userId: Int = 0
)