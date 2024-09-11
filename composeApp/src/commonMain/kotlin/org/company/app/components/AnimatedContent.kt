package org.company.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import io.github.alexzhirkevich.compottie.*
import org.company.app.animations.lottieData

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnimatedContent() {
    val composition by rememberLottieComposition(LottieCompositionSpec.JsonString(lottieData))
//        val composition by rememberLottieComposition(LottieCompositionSpec.JsonString(lottieData))
//        val composition by rememberLottieComposition(LottieCompositionSpec.JsonString(data))

    val progress = animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    val painter = rememberLottiePainter(composition, progress.value)

    FlowRow {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painter,
                contentDescription = null
            )
            Text("Image with LottiePainter")
        }
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                LottieAnimation(
//                    composition = composition,
//                    progress = { progress.value }
//                )
//                Text("LottieAnimation")
//            }
    }
}