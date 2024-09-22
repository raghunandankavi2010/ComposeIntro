package com.example.composeintro

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.composeintro.animations.CarouselExamples

@Composable
fun MainScreen(modifier: Modifier) {
    Surface(color = Color.White) {
        var showLandingScreen by remember { mutableStateOf(true) }
        if (showLandingScreen) {
            SplashScreen(onTimeout = { showLandingScreen = false })
        } else {
            FlowCollectorUI()
            CarouselExamples()
            //MessageList(messages = (generateMessages(50)))
            //CounterApp()
            //CounterWithHoisting()
            //CounterApp2()
            //TestStateHolder(StateHolderViewModel())
           // TestStateHolderFix(StateHolderViewModel())
           // Try()
        }
    }
}

fun generateMessages(count: Int): List<Message> {
    return List(count) { index ->
        Message("Message ${index + 1}")
    }
}