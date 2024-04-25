package com.jonecx.qio.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun QioCircularProgress(
    modifier: Modifier = Modifier,
    arcColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    circleColor: Color = MaterialTheme.colorScheme.secondary,
    strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth - 2.dp,
    sweepAngle: Float = 60f,
    size: Dp = 48.dp,
) {
    val transition = rememberInfiniteTransition(label = "Indeterminate circular progress bar")
    val currentArcStarting by transition.animateValue(
        initialValue = 0,
        targetValue = 360,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = LinearEasing,
            ),
        ),
        label = "Indeterminate Circular progress bar animation value",
    )

    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Square)
    }

    Canvas(
        modifier = modifier
            .testTag("qio_indeterminate_circular_progress_bar")
            .size(size)
            .padding(strokeWidth / 2),
    ) {
        drawCircle(circleColor, style = stroke)
        drawArc(
            color = arcColor,
            startAngle = currentArcStarting.toFloat() - 90f,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = stroke,
        )
    }
}

@Preview
@Composable
fun QioCircularProgressPreview() {
    QioCircularProgress()
}
