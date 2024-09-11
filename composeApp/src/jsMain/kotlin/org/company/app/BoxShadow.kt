package org.company.app

import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.CSSNumeric
import org.jetbrains.compose.web.css.CSSStyleValue
import org.jetbrains.compose.web.css.StyleScope


class CSSBoxShadow : CSSStyleValue {
    var offsetX: CSSNumeric? = null
    var offsetY: CSSNumeric? = null
    var blurRadius: CSSNumeric? = null
    var spreadRadius: CSSNumeric? = null
    var color: CSSColorValue? = null
    var opacity: Double? = null
    var inset: Boolean = false

    override fun equals(other: Any?): Boolean {
        return if (other is CSSBoxShadow) {
            offsetX == other.offsetX && offsetY == other.offsetY && blurRadius == other.blurRadius &&
                    spreadRadius == other.spreadRadius && color == other.color && inset == other.inset
        } else false
    }

    override fun toString(): String {
        val values = listOfNotNull(
            if (inset) "inset" else null,
            offsetX, offsetY, blurRadius, spreadRadius, color
        )
        return values.joinToString(" ")
    }
}

fun CSSBoxShadow.offsetX(value: CSSNumeric) {
    offsetX = value
}

fun CSSBoxShadow.offsetY(value: CSSNumeric) {
    offsetY = value
}

fun CSSBoxShadow.blurRadius(value: CSSNumeric) {
    blurRadius = value
}

fun CSSBoxShadow.spreadRadius(value: CSSNumeric) {
    spreadRadius = value
}

fun CSSBoxShadow.inset(isInset: Boolean) {
    inset = isInset
}

fun CSSBoxShadow.opacity(value: Double) {
    opacity = value
}

fun CSSBoxShadow.color(value: CSSColorValue) {
    color = value
}

fun StyleScope.boxShadow(
    offsetX: CSSNumeric? = null,
    offsetY: CSSNumeric? = null,
    blurRadius: CSSNumeric? = null,
    spreadRadius: CSSNumeric? = null,
    color: CSSColorValue? = null,
    opacity: Double? = null,
    inset: Boolean? = false,
) {
    boxShadowBuilder {
        offsetX?.let { offsetX(it) }
        offsetY?.let { offsetY(it) }
        blurRadius?.let { blurRadius(it) }
        spreadRadius?.let { spreadRadius(it) }
        color?.let { color(it) }
        inset?.let { inset(it) }
        opacity?.let { opacity(it) }
        color?.let { color(it) }
    }
}

private fun StyleScope.boxShadowBuilder(boxShadowBuild: CSSBoxShadow.() -> Unit) {
    property("box-shadow", CSSBoxShadow().apply(boxShadowBuild))
}
