package com.example.composeintro

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp


private const val SplashWaitTime = 2000L

@Composable
fun SplashScreen(onTimeout: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val currentOnTimeout by rememberUpdatedState(onTimeout)

        // Create an effect that matches the lifecycle of LandingScreen.
        // If LandingScreen recomposes or onTimeout changes,
        // the delay shouldn't start again.
        LaunchedEffect(Unit) {
            delay(SplashWaitTime)
            currentOnTimeout()
        }

        Image(painterResource(id = R.drawable.ic_launcher_background), contentDescription = null)
    }
}

@Composable
fun CoroutineScopeExample(modifier: Modifier = Modifier) {

    val coroutineScope = rememberCoroutineScope()
    var buttonText by remember {
        mutableStateOf("Hello World!")
    }

    Button(
        onClick = {
            coroutineScope.launch {
                delay5()
                buttonText = "Hello Raghu!"
            }

        },
    ) {
        Text(buttonText)
    }
}

suspend fun delay5() {
    delay(5000L)
}

//

@Composable
fun SensorListener() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // State to hold sensor values (for demonstration)
    var sensorValues by remember { mutableStateOf(FloatArray(3)) }

    DisposableEffect(key1 = lifecycleOwner, key2 = accelerometerSensor) {
        val sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                sensorValues = event.values
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

            }
        }

        // Start listening to the sensor
        sensorManager.registerListener(
            sensorListener,
            accelerometerSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        // Cleanup: Unregister the listener when the effect is disposed
        onDispose {
            sensorManager.unregisterListener(sensorListener)
        }
    }

    // Display sensor values (for demonstration)
    Text("Sensor Values: X=${sensorValues[0]}, Y=${sensorValues[1]}, Z=${sensorValues[2]}")
}


class Ref(var value: Int)

// Note the inline function below which ensures that this function is essentially
// copied at the call site to ensure that its logging only recompositions from the
// original call site.
@Composable
inline fun LogCompositions(tag: String, msg: String) {
    val ref = remember { Ref(0) }
    SideEffect { ref.value++ }
    Log.d(tag, "Compositions: $msg ${ref.value}")
}


data class Message(val message: String)

@Composable
fun MessageList(messages: List<Message>) {
    Box(modifier = Modifier.fillMaxSize()) {
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {

            items(messages.size) { index ->
                Text(messages[index].message)
            }
        }

        // Show the button if the first visible item is past
        // the first item. We use a remembered derived state to
        // minimize unnecessary compositions
        val showButton by remember {
            derivedStateOf {
                listState.firstVisibleItemIndex > 0
            }
        }

        AnimatedVisibility(visible = showButton, Modifier
            .align(Alignment.BottomEnd)) {
            Image(
                modifier = Modifier
                    .padding(16.dp)
                    .size(50.dp)
                    .clip(CircleShape)
                    .clickable {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }                    },
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null
            )
        }
    }
}

@Composable
fun ScrollToTopButton(modifier: Modifier, scrollToTop: () -> Unit) {

    Image(
        modifier = Modifier
            .size(24.dp)
            .clickable {
                scrollToTop()
            },
        painter = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = null
    )
}
