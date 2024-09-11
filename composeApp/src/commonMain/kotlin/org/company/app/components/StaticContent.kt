package org.company.app.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import multiplatform_app.composeapp.generated.resources.*
import multiplatform_app.composeapp.generated.resources.IndieFlower_Regular
import multiplatform_app.composeapp.generated.resources.Res
import multiplatform_app.composeapp.generated.resources.cyclone
import multiplatform_app.composeapp.generated.resources.ic_cyclone
import org.company.app.openUrl
import org.company.app.theme.LocalThemeIsDark
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import kotlin.run

@Composable
fun StaticContent() {
    Text(
        text = stringResource(Res.string.cyclone),
        fontFamily = FontFamily(Font(Res.font.IndieFlower_Regular)),
        style = MaterialTheme.typography.displayLarge
    )

    var isAnimate by remember { mutableStateOf(false) }
    val transition = rememberInfiniteTransition()
    val rotate by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing)
        )
    )

    Image(
        modifier = Modifier
            .size(250.dp)
            .padding(16.dp)
            .run { if (isAnimate) rotate(rotate) else this },
        imageVector = vectorResource(Res.drawable.ic_cyclone),
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
        contentDescription = null
    )

    ElevatedButton(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .widthIn(min = 200.dp),
        onClick = { isAnimate = !isAnimate },
        content = {
            Icon(vectorResource(Res.drawable.ic_rotate_right), contentDescription = null)
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(
                stringResource(if (isAnimate) Res.string.stop else Res.string.run)
            )
        }
    )

    var isDark by LocalThemeIsDark.current
    val icon = remember(isDark) {
        if (isDark) Res.drawable.ic_light_mode
        else Res.drawable.ic_dark_mode
    }

    ElevatedButton(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).widthIn(min = 200.dp),
        onClick = { isDark = !isDark },
        content = {
            Icon(vectorResource(icon), contentDescription = null)
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(stringResource(Res.string.theme))
        }
    )

    TextButton(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).widthIn(min = 200.dp),
        onClick = { openUrl("https://github.com/terrakok") },
    ) {
        Text(stringResource(Res.string.open_github))
    }
}