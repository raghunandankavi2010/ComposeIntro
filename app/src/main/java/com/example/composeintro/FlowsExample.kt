package com.example.composeintro

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart


@Composable
fun FlowCollectorUI(viewModel: FlowExampleViewModel = viewModel()) {

    val result = viewModel.resultData2.collectAsStateWithLifecycle()
    var text by remember {
        mutableStateOf("")
    }

    when (val state: UiState<String> = result.value) {
        UiState.Default -> {

        }

        UiState.Loading -> {

        }

        is UiState.Success -> {
            text = state.data
        }
    }

    var count by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            count = count.inc()
        }) {
            Text("Click Me")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("$text $count")
    }

}


class FlowExampleViewModel : ViewModel() {

    // used a trigger but for now its dummy!
    val trigger = MutableStateFlow("")

    // get the data from api fake data for now!
    @OptIn(ExperimentalCoroutinesApi::class)
    val resultData2: StateFlow<UiState<String>> =
        stateFlow(viewModelScope, UiState.Success("")) { subscriptionCount ->
            trigger.flowWhileShared(
                subscriptionCount, SharingStarted.WhileSubscribed()
            ).distinctUntilChanged().flatMapLatest {
                Log.i("Making api call ", "Now!")
                getDataFromApi()
            }
        }


    private fun getDataFromApi(): Flow<UiState<String>> {

        return getFakeApiData().asResult().map { currenState ->
            when (currenState) {
                is DataResult.Error -> {
                    UiState.Default
                }

                DataResult.Loading -> {
                    UiState.Loading
                }

                is DataResult.Success -> {
                    UiState.Success(currenState.data)
                }
            }
        }
    }
}

fun getFakeApiData(): Flow<String> = flow<String> {
    delay(5000)
    emit("Hello Raghu")
}


fun <T> Flow<T>.asResult(): Flow<DataResult<T>> = map<T, DataResult<T>> {
    DataResult.Success(it)
}.onStart {
    emit(DataResult.Loading)
}.catch { exception ->

    when (exception) {

        is Exception -> {
            emit(DataResult.Error("Something went wrong!"))
        }
    }
}


sealed interface DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>
    data class Error(
        val errorMessage: String,
    ) : DataResult<Nothing>

    data object Loading : DataResult<Nothing>
}


sealed interface UiState<out T> {

    // Initial state
    data object Default : UiState<Nothing>

    // Loading State
    data object Loading : UiState<Nothing>

    // Success state with some data
    data class Success<out T>(val data: T) : UiState<T>

}
