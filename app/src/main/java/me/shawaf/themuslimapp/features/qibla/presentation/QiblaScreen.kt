package me.shawaf.themuslimapp.features.qibla.presentation

import android.Manifest
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shawaf.themuslimapp.features.qibla.presentation.viewmodel.QiblaViewModel
import kotlin.math.abs

private val Gold = Color(0xFFE7CE9A)

@Composable
fun QiblaScreen(qiblaViewModel: QiblaViewModel) {
    val state by qiblaViewModel.viewState.collectAsState()
    val heading = rememberDeviceHeading()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) {
        qiblaViewModel.handleIntent(
            me.shawaf.themuslimapp.features.qibla.presentation.viewmodel.QiblaIntent.Load,
        )
    }
    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ),
        )
    }

    // Angle of the Qibla relative to where the device currently points.
    val relative = normalizeAngle(state.bearing - heading)
    val facingQibla = abs(relative) < 8f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Header
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Qibla Compass", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Text(
                state.locationLabel + if (state.usingDefaultLocation) " · default" else "",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
            )
        }

        Spacer(Modifier.height(10.dp))
        Text(
            "Direction to Makkah",
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            "${state.bearing.toInt()}°",
            color = Gold,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
        )

        Spacer(Modifier.height(18.dp))

        Compass(heading = heading, qiblaRelative = relative)

        Spacer(Modifier.height(20.dp))

        // Facing indicator
        Text(
            text = if (facingQibla) "✓ You're facing the Qibla" else "Rotate until the arrow points up",
            color = if (facingQibla) Color(0xFF8FE0B0) else Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clip(RoundedCornerShape(22.dp))
                .background(
                    if (facingQibla) Color(0xFF123D30) else MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                )
                .padding(horizontal = 18.dp, vertical = 9.dp),
        )

        Spacer(Modifier.height(18.dp))

        // Info card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            InfoCell("Distance", "${state.distanceKm} km")
            InfoCell("Qibla bearing", "${state.bearing.toInt()}° from N")
        }
    }
}

@Composable
private fun InfoCell(label: String, value: String) {
    Column {
        Text(label, color = Color.White.copy(alpha = 0.55f), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(2.dp))
        Text(value, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun Compass(heading: Float, qiblaRelative: Float) {
    Box(
        modifier = Modifier.size(300.dp),
        contentAlignment = Alignment.Center,
    ) {
        val ringColor = Color.White.copy(alpha = 0.3f)

        // Static outer ring
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = ringColor,
                radius = size.minDimension / 2 - 2.dp.toPx(),
                style = Stroke(width = 2.dp.toPx()),
            )
        }

        // Rotating cardinal ring (ticks + N/E/S/W), aligned to real north.
        Box(modifier = Modifier
            .fillMaxSize()
            .rotate(-heading)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2
                val cy = size.height / 2
                val outer = size.minDimension / 2 - 2.dp.toPx()
                for (i in 0 until 72) {
                    val angle = Math.toRadians((i * 5).toDouble())
                    val isMajor = i % 9 == 0
                    val len = if (isMajor) 16.dp.toPx() else 7.dp.toPx()
                    val sx = cx + (outer) * Math.sin(angle).toFloat()
                    val sy = cy - (outer) * Math.cos(angle).toFloat()
                    val ex = cx + (outer - len) * Math.sin(angle).toFloat()
                    val ey = cy - (outer - len) * Math.cos(angle).toFloat()
                    drawLine(
                        color = if (i == 0) Gold else ringColor,
                        start = Offset(sx, sy),
                        end = Offset(ex, ey),
                        strokeWidth = if (isMajor) 2.5.dp.toPx() else 1.dp.toPx(),
                        cap = StrokeCap.Round,
                    )
                }
            }
            Text("N", color = Gold, fontSize = 16.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 26.dp))
            Text("S", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 26.dp))
            Text("E", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp,
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 28.dp))
            Text("W", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp,
                modifier = Modifier.align(Alignment.CenterStart).padding(start = 28.dp))
        }

        // Qibla pointer (rotates to the Qibla relative to the device)
        Box(modifier = Modifier
            .fillMaxSize()
            .rotate(qiblaRelative)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2
                val cy = size.height / 2
                val tipY = size.height * 0.14f
                drawLine(
                    color = Gold,
                    start = Offset(cx, cy),
                    end = Offset(cx, tipY + 10.dp.toPx()),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round,
                )
                val head = Path().apply {
                    moveTo(cx, tipY)
                    lineTo(cx - 10.dp.toPx(), tipY + 18.dp.toPx())
                    lineTo(cx + 10.dp.toPx(), tipY + 18.dp.toPx())
                    close()
                }
                drawPath(head, Gold)
            }
            // Kaaba marker at the tip
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 30.dp)
                    .size(22.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF16231E)),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(Gold),
                )
            }
        }

        // Centre hub
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Gold),
        )
    }
}

/** Registers the rotation-vector sensor and returns a smoothed azimuth (degrees from north). */
@Composable
private fun rememberDeviceHeading(): Float {
    val context = LocalContext.current
    var heading by remember { mutableFloatStateOf(0f) }

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        val listener = object : SensorEventListener {
            private val rotationMatrix = FloatArray(9)
            private val orientation = FloatArray(3)

            override fun onSensorChanged(event: SensorEvent) {
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                SensorManager.getOrientation(rotationMatrix, orientation)
                var azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                if (azimuth < 0) azimuth += 360f
                // Low-pass smoothing along the shortest angular path.
                val delta = normalizeAngle(azimuth - heading)
                heading = (heading + delta * 0.15f + 360f) % 360f
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        if (rotationSensor != null) {
            sensorManager.registerListener(listener, rotationSensor, SensorManager.SENSOR_DELAY_UI)
        }
        onDispose { sensorManager.unregisterListener(listener) }
    }

    return heading
}

/** Normalises an angle in degrees to the range (-180, 180]. */
private fun normalizeAngle(angle: Float): Float {
    var a = angle % 360f
    if (a > 180f) a -= 360f
    if (a < -180f) a += 360f
    return a
}
