package org.company.app.theme

import androidx.compose.runtime.*
import org.company.app.boxShadow
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*

val OverrideStyles = GlobalStyleClass
val GlobalStyles = compositionLocalOf { mutableStateOf(OverrideStyles) }

@Composable
fun CssTheme(
    content: @Composable() () -> Unit
) {
    val globalStyleSheet = remember { mutableStateOf(GlobalStyleClass) }

    CompositionLocalProvider(GlobalStyles provides globalStyleSheet) {
        content.invoke()
    }
}

@OptIn(ExperimentalComposeWebApi::class)
object GlobalStyleClass : StyleSheet() {
    init {
        "button" style {
            color(Color.red)
        }
        ".container" style {
            display(DisplayStyle.Flex)
            FlexWrap.Wrap
        }
        ".column" style {
            flexDirection(FlexDirection.Column)
        }
        ".deep" style {
            width(100.percent)
            height(100.percent)
            backgroundColor(Color.lightpink)
            boxShadow(
                offsetX = 0.px,
                offsetY = 0.px,
                blurRadius = 15.px,
                spreadRadius = 5.px,
                color = rgba(255, 0, 0, 0.5)
            )
        }
        ".item" style {
            flex(1)
            padding(10.px)
            marginTop(10.px)
            marginBottom(10.px)
        }
        "#btn" style {
            color(Color.brown)
        }

        ".cupertino-button" style {
            display(DisplayStyle.InlineBlock)
            padding(10.px, 20.px)
            background("linear-gradient(to bottom, #f7f7f7, #e0e0e0)")
            borderRadius(10.px)
            border(1.px, LineStyle.Solid, Color("#c6c6c6"))
            fontFamily("-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif")
            fontSize(16.px)
//            color(Color("#007aff"))
            textAlign("center")
            textDecoration("none")
            boxShadow(
                color = Color.black, blurRadius = 2.px,
                offsetX = 0.px, offsetY = 2.px, spreadRadius = 0.px, opacity = 0.1
            )
            transitions {
                defaultTimingFunction(AnimationTimingFunction.Ease)
                "all" { duration(.2.s) }
            }
        }

        ".cupertino-button:hover" style {
            background("linear-gradient(to bottom, #e0e0e0, #d1d1d1)")
            boxShadow(
                color = Color.black,
                blurRadius = 4.px,
                offsetX = 0.px,
                offsetY = 4.px,
                spreadRadius = 0.px,
                opacity = 0.1
            )
        }

        ".cupertino-button:active" style {
            background("linear-gradient(to bottom, #d1d1d1, #b8b8b8)")
            boxShadow(
                color = Color.black,
                blurRadius = 2.px,
                offsetX = 0.px,
                offsetY = 2.px,
                spreadRadius = 0.px,
                opacity = 0.15
            )
            transform { translateY(1.px) }
        }
    }

    fun override(params: GlobalStyleClass.() -> Unit): GlobalStyleClass {
        return GlobalStyleClass.apply(params)
    }
}