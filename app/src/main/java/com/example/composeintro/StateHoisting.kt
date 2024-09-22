package com.example.composeintro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


// Example1
@Composable
fun CounterWithHoisting() {
    var count by remember { mutableIntStateOf(0) }

    Column {
        CounterDisplay2(count)
        CounterControls(count, {
            count--
        }, {
            count++
        })
    }
}

@Composable
fun CounterDisplay2(count: Int) {
    Text("Count: $count")
}

@Composable
fun CounterControls(count: Int, decrement: () -> Unit, increment: () -> Unit) {
    Row {
        Button(onClick = { increment() }) { Text("Increment $count") }

        Button(onClick = { decrement() }) { Text("Decrement") }
    }
}
//// Example2

data class CounterState(val count: Int)

@Composable
fun CounterApp2() {
    val counterState = remember { mutableStateOf(CounterState(0)) }
    Column {
        Counter(counterState.value.count)
        IncrementButton(onClick = {
            counterState.value = counterState.value.copy(count = counterState.value.count + 1)
        })
    }
}

@Composable
fun IncrementButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Increment")
    }
}

@Composable
fun Counter(count: Int) {
    Text("Count: $count")
}

//// Example 3


class CounterViewModel : ViewModel() {
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count

    fun incrementCount() {
        _count.value++
    }
}

@Composable
fun CounterApp(viewModel: CounterViewModel) {
    val count = viewModel.count.collectAsStateWithLifecycle()
    Column {
        Counter(count.value)
        IncrementButton(onClick = viewModel::incrementCount)
    }
}

data class StateHolder(val name: String, val age: Int)
//// Example 4


class StateHolderViewModel : ViewModel() {
    private val _stateHolder = MutableStateFlow(StateHolder("InitialName", 0))
    val stateHolder: StateFlow<StateHolder> = _stateHolder

    fun changeName() {
        _stateHolder.value = _stateHolder.value.copy(name = "Raghu")
    }
}

@Composable
fun TestStateHolder(viewModel: StateHolderViewModel) {
    val count = viewModel.stateHolder.collectAsStateWithLifecycle()
    val stateHolder = count.value
    Column {
        DisplayName(stateHolder = stateHolder)
        DisplayAge(stateHolder = stateHolder)
        ChangeNameButton(onClick = viewModel::changeName)
    }
}

@Composable
fun ChangeNameButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("ChangeName")
    }
}

@Composable
fun DisplayName(stateHolder: StateHolder) {
    Text(stateHolder.name)
}

@Composable
fun DisplayAge(stateHolder: StateHolder) {
    Text("${stateHolder.age}")
}

@Composable
fun TestStateHolderFix(viewModel: StateHolderViewModel) {
    val count = viewModel.stateHolder.collectAsStateWithLifecycle()
    val stateHolder = count.value
    Column {
        DisplayNameFix(name = stateHolder.name)
        DisplayAgeFix(age = stateHolder.age)
        ChangeNameButton(onClick = viewModel::changeName)
    }
}

@Composable
fun DisplayNameFix(name: String) {
    Text(name)
}

@Composable
fun DisplayAgeFix(age: Int) {
    Text("$age")
}


@Composable
fun Try(modifier: Modifier = Modifier) {

    Row(
        modifier = Modifier
            .padding(top = 100.dp)
            .fillMaxWidth()
            .background(Color.Cyan)

    ) {

        Text("Price")

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .width(50.dp)
                .background(Color.Yellow)
        ) {
            Text("s99")
        }
    }

}