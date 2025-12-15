package org.company.app.modifier

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.ElementScope
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.TagElement
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.Element
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

/**
 * Base Modifier interface - similar to Jetpack Compose
 */
interface Modifier {
    /**
     * Concatenates this modifier with another
     */
    infix fun then(other: Modifier): Modifier =
        if (other === Modifier) this else CombinedModifier(
            this,
            other
        )

    /**
     * Apply this modifier to the style scope
     */
    fun applyToStyle(styleScope: StyleScope)

    /**
     * Apply this modifier to the attributes scope
     */
    fun applyToAttrs(attrsScope: AttrsScope<Element>) {}

    companion object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {}
        override fun applyToAttrs(attrsScope: AttrsScope<Element>) {}
    }
}

/**
 * Internal class for combining modifiers
 */
private class CombinedModifier(
    private val outer: Modifier,
    private val inner: Modifier
) : Modifier {
    override fun applyToStyle(styleScope: StyleScope) {
        outer.applyToStyle(styleScope)
        inner.applyToStyle(styleScope)
    }

    override fun applyToAttrs(attrsScope: AttrsScope<Element>) {
        outer.applyToAttrs(attrsScope)
        inner.applyToAttrs(attrsScope)
    }
}
// ============= Size Modifiers =============
fun Modifier.fillMaxWidth(): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.width(100.percent)
        }
    })

fun Modifier.fillMaxHeight(): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.height(100.percent)
        }
    })

fun Modifier.fillMaxSize(): Modifier =
    fillMaxWidth().fillMaxHeight()

fun Modifier.width(value: CSSNumeric): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.width(value)
        }
    })

fun Modifier.height(value: CSSNumeric): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.height(value)
        }
    })

fun Modifier.size(
    width: CSSNumeric,
    height: CSSNumeric
): Modifier =
    this.width(width).height(height)

fun Modifier.size(size: CSSNumeric): Modifier =
    this.width(size).height(size)
// ============= Padding Modifiers =============
fun Modifier.padding(all: CSSNumeric): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.padding(all)
        }
    })

fun Modifier.padding(
    horizontal: CSSNumeric = 0.px,
    vertical: CSSNumeric = 0.px
): Modifier = then(object : Modifier {
    override fun applyToStyle(styleScope: StyleScope) {
        styleScope.padding(vertical, horizontal)
    }
})

fun Modifier.padding(
    start: CSSNumeric = 0.px,
    top: CSSNumeric = 0.px,
    end: CSSNumeric = 0.px,
    bottom: CSSNumeric = 0.px
): Modifier = then(object : Modifier {
    override fun applyToStyle(styleScope: StyleScope) {
        styleScope.padding(top, end, bottom, start)
    }
})
// ============= Margin Modifiers =============
fun Modifier.margin(all: CSSNumeric): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.margin(all)
        }
    })

fun Modifier.margin(
    horizontal: CSSNumeric = 0.px,
    vertical: CSSNumeric = 0.px
): Modifier = then(object : Modifier {
    override fun applyToStyle(styleScope: StyleScope) {
        styleScope.margin(vertical, horizontal)
    }
})

fun Modifier.margin(
    start: CSSNumeric = 0.px,
    top: CSSNumeric = 0.px,
    end: CSSNumeric = 0.px,
    bottom: CSSNumeric = 0.px
): Modifier = then(object : Modifier {
    override fun applyToStyle(styleScope: StyleScope) {
        styleScope.margin(top, end, bottom, start)
    }
})
// ============= Background Modifiers =============
fun Modifier.background(color: CSSColorValue): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.backgroundColor(color)
        }
    })
// ============= Border Modifiers =============
fun Modifier.border(
    width: CSSLengthValue = 1.px,
    style: LineStyle = LineStyle.Solid,
    color: CSSColorValue = Color.black
): Modifier = then(object : Modifier {
    override fun applyToStyle(styleScope: StyleScope) {
        styleScope.border(width, style, color)
    }
})

fun Modifier.borderRadius(radius: CSSNumeric): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.borderRadius(radius)
        }
    })
// ============= Click Modifier =============
fun Modifier.clickable(onClick: () -> Unit): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.cursor("pointer")
        }

        override fun applyToAttrs(attrsScope: AttrsScope<Element>) {
            attrsScope.onClick { onClick() }
        }
    })
// ============= Alignment & Arrangement =============
enum class Arrangement {
    Start, End, Center, SpaceBetween, SpaceAround, SpaceEvenly
}

enum class Alignment {
    Start, End, Center, Stretch
}
// ============= Layout Composables =============
@Composable
fun Box(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Start,
    content: @Composable () -> Unit
) {
    Div({
        style {
            display(DisplayStyle.Flex)
            position(Position.Relative)

            when (contentAlignment) {
                Alignment.Start -> {
                    alignItems(AlignItems.FlexStart)
                    justifyContent(JustifyContent.FlexStart)
                }

                Alignment.End -> {
                    alignItems(AlignItems.FlexEnd)
                    justifyContent(JustifyContent.FlexEnd)
                }

                Alignment.Center -> {
                    alignItems(AlignItems.Center)
                    justifyContent(JustifyContent.Center)
                }

                Alignment.Stretch -> {
                    alignItems(AlignItems.Stretch)
                }
            }

            modifier.applyToStyle(this)
        }
        modifier.applyToAttrs(this)
    }) {
        content()
    }
}

@Composable
fun Row(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement = Arrangement.Start,
    verticalAlignment: Alignment = Alignment.Start,
    content: @Composable () -> Unit
) {
    Div({
        style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Row)
            // Horizontal arrangement (main axis)
            when (horizontalArrangement) {
                Arrangement.Start -> justifyContent(
                    JustifyContent.FlexStart
                )

                Arrangement.End -> justifyContent(
                    JustifyContent.FlexEnd
                )

                Arrangement.Center -> justifyContent(
                    JustifyContent.Center
                )

                Arrangement.SpaceBetween -> justifyContent(
                    JustifyContent.SpaceBetween
                )

                Arrangement.SpaceAround -> justifyContent(
                    JustifyContent.SpaceAround
                )

                Arrangement.SpaceEvenly -> justifyContent(
                    JustifyContent.SpaceEvenly
                )
            }
            // Vertical alignment (cross axis)
            when (verticalAlignment) {
                Alignment.Start -> alignItems(AlignItems.FlexStart)
                Alignment.End -> alignItems(AlignItems.FlexEnd)
                Alignment.Center -> alignItems(AlignItems.Center)
                Alignment.Stretch -> alignItems(AlignItems.Stretch)
            }

            modifier.applyToStyle(this)
        }
        modifier.applyToAttrs(this)
    }) {
        content()
    }
}

@Composable
fun Column(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement = Arrangement.Start,
    horizontalAlignment: Alignment = Alignment.Start,
    content: @Composable () -> Unit
) {
    Div({
        style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            // Vertical arrangement (main axis)
            when (verticalArrangement) {
                Arrangement.Start -> justifyContent(
                    JustifyContent.FlexStart
                )

                Arrangement.End -> justifyContent(
                    JustifyContent.FlexEnd
                )

                Arrangement.Center -> justifyContent(
                    JustifyContent.Center
                )

                Arrangement.SpaceBetween -> justifyContent(
                    JustifyContent.SpaceBetween
                )

                Arrangement.SpaceAround -> justifyContent(
                    JustifyContent.SpaceAround
                )

                Arrangement.SpaceEvenly -> justifyContent(
                    JustifyContent.SpaceEvenly
                )
            }
            // Horizontal alignment (cross axis)
            when (horizontalAlignment) {
                Alignment.Start -> alignItems(AlignItems.FlexStart)
                Alignment.End -> alignItems(AlignItems.FlexEnd)
                Alignment.Center -> alignItems(AlignItems.Center)
                Alignment.Stretch -> alignItems(AlignItems.Stretch)
            }

            modifier.applyToStyle(this)
        }
        modifier.applyToAttrs(this)
    }) {
        content()
    }
}
// ============= Weight Modifier for Flex Children =============
fun Modifier.weight(weight: Float): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.property("flex", "$weight 1 0")
        }
    })
// ============= Additional Useful Modifiers =============
fun Modifier.alpha(alpha: Float): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.opacity(alpha)
        }
    })

fun Modifier.shadow(
    offsetX: CSSNumeric = 0.px,
    offsetY: CSSNumeric = 4.px,
    blurRadius: CSSNumeric = 6.px,
    color: CSSColorValue = rgba(0, 0, 0, 0.1)
): Modifier = then(object : Modifier {
    override fun applyToStyle(styleScope: StyleScope) {
        styleScope.property(
            "box-shadow",
            "$offsetX $offsetY $blurRadius $color"
        )
    }
})

fun Modifier.clip(radius: CSSNumeric): Modifier =
    borderRadius(radius)
// ============= Position Modifiers =============
fun Modifier.position(position: Position): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.position(position)
        }
    })

fun Modifier.top(value: CSSLengthOrPercentageValue): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.top(value)
        }
    })

fun Modifier.right(value: CSSLengthOrPercentageValue): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.right(value)
        }
    })

fun Modifier.bottom(value: CSSLengthOrPercentageValue): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.bottom(value)
        }
    })

fun Modifier.left(value: CSSLengthOrPercentageValue): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.left(value)
        }
    })
// ============= Text Modifiers =============
fun Modifier.fontSize(size: CSSNumeric): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.fontSize(size)
        }
    })

fun Modifier.fontWeight(weight: Number): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.fontWeight(weight.toString())
        }
    })

fun Modifier.textAlign(align: String): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.textAlign(align)
        }
    })

fun Modifier.color(color: CSSColorValue): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.color(color)
        }
    })
// ============= Display Modifiers =============
fun Modifier.gap(gap: CSSNumeric): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.property("gap", gap.toString())
        }
    })

fun Modifier.maxWidth(width: CSSNumeric): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.maxWidth(width)
        }
    })

fun Modifier.textDecoration(decoration: String): Modifier =
    then(object : Modifier {
        override fun applyToStyle(styleScope: StyleScope) {
            styleScope.property(
                "text-decoration",
                decoration
            )
        }
    })
// ============= Spacer Composable =============
@Composable
fun Spacer(modifier: Modifier = Modifier) {
    Div({
        style {
            modifier.applyToStyle(this)
        }
        modifier.applyToAttrs(this)
    })
}
// ============= Text Composable =============
@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier
) {
    Div({
        style {
            modifier.applyToStyle(this)
        }
        modifier.applyToAttrs(this)
    }) {
        org.jetbrains.compose.web.dom.Text(text)
    }
}
// ============= Button Composable =============
@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Div({
        style {
            cursor("pointer")
            modifier.applyToStyle(this)
        }
        onClick { onClick() }
        modifier.applyToAttrs(this)
    }) {
        content()
    }
}
// ============= Link Composable =============
@Composable
fun Link(
    href: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    org.jetbrains.compose.web.dom.A(href = href, attrs = {
        style {
            modifier.applyToStyle(this)
        }
        modifier.applyToAttrs(this)
    }) {
        content()
    }
}
// ============= Svg Icon Composable =============
@Composable
fun Svg(
    viewBox: String = "0 0 24 24",
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    TagElement(
        elementBuilder = org.jetbrains.compose.web.dom.ElementBuilder.createBuilder(
            "svg"
        ),
        applyAttrs = {
            attr("viewBox", viewBox)
            attr("xmlns", "http://www.w3.org/2000/svg")
            style {
                modifier.applyToStyle(this)
            }
            modifier.applyToAttrs(this)
        },
        content = content as @Composable (ElementScope<Element>.() -> Unit)?
    )
}

@Composable
fun SvgPath(
    d: String,
    fill: String = "currentColor"
) {
    TagElement(
        elementBuilder = org.jetbrains.compose.web.dom.ElementBuilder.createBuilder(
            "path"
        ),
        applyAttrs = {
            attr("d", d)
            attr("fill", fill)
        },
        content = null
    )
}

@Composable
fun SvgIcon(
    viewBox: String = "0 0 24 24",
    modifier: Modifier = Modifier,
    pathData: String
) {
    Svg(viewBox = viewBox, modifier = modifier) {
        SvgPath(d = "M0 0h24v24H0z", fill = "none")
        SvgPath(d = pathData, fill = "currentColor")
    }
}
// ============= Plan Card Component Example =============
data class PlanFeature(
    val text: String,
    val bold: String? = null
)

@Composable
fun PlanCard(
    price: String,
    period: String,
    title: String,
    description: String,
    features: List<PlanFeature>,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .borderRadius(16.px)
            .shadow(
                offsetX = 0.px,
                offsetY = 30.px,
                blurRadius = 30.px,
                color = rgba(0, 38, 255, 0.205)
            )
            .padding(10.px)
            .background(Color.white)
            .maxWidth(300.px)
    ) {
        // Inner container
        Box(
            modifier = Modifier
                .padding(
                    start = 20.px,
                    top = 40.px,
                    end = 20.px,
                    bottom = 20.px
                )
                .background(rgb(236, 240, 255))
                .borderRadius(12.px)
                .position(Position.Relative)
        ) {
            // Pricing badge
            Box(
                modifier = Modifier
                    .position(Position.Absolute)
                    .top(0.px)
                    .right(0.px)
                    .background(rgb(190, 214, 251))
                    .borderRadius(99.em)
                    .padding(
                        horizontal = 0.75.em,
                        vertical = 0.625.em
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.Center,
                    modifier = Modifier.gap(0.25.em)
                ) {
                    Text(
                        price,
                        modifier = Modifier
                            .fontSize(1.25.cssRem)
                            .fontWeight(600)
                            .color(rgb(66, 84, 117))
                    )
                    Text(
                        period,
                        modifier = Modifier
                            .fontSize(0.75.em)
                            .color(rgb(112, 122, 145))
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Title
                Text(
                    title,
                    modifier = Modifier
                        .fontWeight(600)
                        .fontSize(1.25.cssRem)
                        .color(rgb(66, 86, 117))
                )

                Spacer(modifier = Modifier.height(0.75.cssRem))
                // Description
                Text(
                    description,
                    modifier = Modifier.color(
                        rgb(
                            105,
                            126,
                            145
                        )
                    )
                )

                Spacer(modifier = Modifier.height(1.cssRem))
                // Features list
                Column(modifier = Modifier.gap(0.75.cssRem)) {
                    features.forEach { feature ->
                        FeatureItem(feature)
                    }
                }

                Spacer(modifier = Modifier.height(1.25.cssRem))
                // Action button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onButtonClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(rgb(101, 88, 211))
                            .borderRadius(6.px)
                            .padding(
                                horizontal = 0.75.em,
                                vertical = 0.625.em
                            )
                            .fontWeight(500)
                            .fontSize(1.125.cssRem)
                            .textAlign("center")
                            .color(Color.white)
                            .textDecoration("none")
                    ) {
                        Text(buttonText)
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureItem(feature: PlanFeature) {
    Row(
        verticalAlignment = Alignment.Center,
        modifier = Modifier.gap(0.5.cssRem)
    ) {
        // Checkmark icon
        Box(
            modifier = Modifier
                .size(20.px)
                .background(rgb(31, 202, 197))
                .borderRadius(50.percent),
            contentAlignment = Alignment.Center
        ) {
            Text("♂\uFE0F")
//            SvgIcon(
//                modifier = Modifier.size(14.px).color(Color.white),
//                pathData = "M10 15.172l9.192-9.193 1.415 1.414L10 18l-6.364-6.364 1.414-1.414z"
//            )
        }
        // Feature text
        if (feature.bold != null) {
            Row(modifier = Modifier.gap(0.25.em)) {
                if (feature.text.isNotEmpty()) {
                    Text(
                        feature.text,
                        modifier = Modifier.color(
                            rgb(
                                105,
                                126,
                                145
                            )
                        )
                    )
                }
                Text(
                    feature.bold,
                    modifier = Modifier
                        .fontWeight(600)
                        .color(rgb(66, 82, 117))
                )
            }
        } else {
            Text(
                feature.text,
                modifier = Modifier.color(
                    rgb(
                        105,
                        126,
                        145
                    )
                )
            )
        }
    }
}
// ============= Usage Example =============
@Composable
fun ExampleUsage() {
    var inputState by remember { mutableStateOf("Professional") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .margin(16.px)
            .background(Color.aquamarine),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Center
    ) {
        // Plan Card Example
        PlanCard(
            price = "$49",
            period = "/ m",
            title = inputState,
            description = "This plan is for those who have a team already and running a large business.",
            features = listOf(
                PlanFeature("20", " team members"),
                PlanFeature("Plan ", "team meetings"),
                PlanFeature("File sharing")
            ),
            buttonText = "Choose plan",
            onButtonClick = { println("Plan selected!") }
        )

        Spacer(modifier = Modifier.height(16.px))
        // Basic Layout Example
        PlanCard(
            price = "$49",
            period = "/ m",
            title = inputState,
            description = "This plan is for those who have a team already and running a large business.",
            features = listOf(
                PlanFeature("", "20 team members"),
                PlanFeature("Plan ", "team meetings"),
                PlanFeature("File sharing")
            ),
            buttonText = "Choose plan",
            onButtonClick = { println("Plan selected!") }
        )

        Input(type = InputType.Text) {
            value(inputState)
            onInput { event ->
                inputState = event.value
            }
        }
    }
}
/*
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.px)
            .background(Color.white),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.px)
                .background(Color.lightgray)
                .borderRadius(8.px),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Item 1")
            Text("Item 2")
            Text("Item 3")
        }

        Box(
            modifier = Modifier
                .size(100.px)
                .background(Color.blue)
                .clickable { println("Clicked!") }
                .borderRadius(50.px),
            contentAlignment = Alignment.Center
        ) {
            Text("Click Me")
        }

        // Weighted items in Row
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(50.px)
                    .background(Color.red)
            ) {}
            Box(
                modifier = Modifier
                    .weight(2f)
                    .height(50.px)
                    .background(Color.green)
            ) {}
        }
    }
}
*/

// ============= Usage Example =============
fun main() {
    renderComposable(rootElementId = "root") {
        ExampleUsage()
        Text()
    }
}
