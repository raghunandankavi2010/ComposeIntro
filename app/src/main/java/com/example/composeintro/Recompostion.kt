package com.example.composeintro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CounterApp() {
    var counter by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Counter Demo Start", fontSize = 24.sp)
        CounterDisplay(counter) // counter is read here so this also recompose
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { counter++ }) {
            Text(text = "Increment")
        }
    }
}

@Composable
fun CounterDisplay(count: Int) {
    Text(text = "Counter: $count", fontSize = 24.sp) // counter is read here so this also recompose
    Text(text = "Counter Demo End ", fontSize = 24.sp)
}
