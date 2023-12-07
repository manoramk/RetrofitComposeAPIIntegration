
package com.list.retrofitapiintegration

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.retrofit.integration.repository.MainRepository
import com.retrofit.integration.viewModel.MainViewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    private val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(repository = MainRepository()) as T
        }
    }
    private val viewModel: MainViewModel by viewModels{
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApiIntAppTheme()
        }

    }

    @Composable
    fun ApiIntApp() {
        var apiInt by remember { mutableStateOf("Press the button Fetch API ") }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = apiInt, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(16.dp).padding(10.dp))
            Button(
                onClick = {
                    apiInt = "Loading..."
                    apiInt = fetchWeatherFact()
                }
            ) {
                Text("Fetch GET API ")
                Spacer(modifier = Modifier.height(16.dp).padding(20.dp)
                    .verticalScroll(rememberScrollState()))
            }
        }
    }

    @Composable
    fun ApiIntAppTheme() {
        MaterialTheme(
            typography = Typography()
        ) {
            ApiIntApp()
        }
    }


     private fun fetchWeatherFact(): String {

         viewModel.getPost()
         var data = "No Data"
        // Observe changes in the LiveData
         viewModel.post.observe(this, Observer { post ->
             // Update UI with the post data
             for (i in 0 .. 1) {
                 data = post.message?.toString().toString()
             }
         })

        return data
    }

    @Serializable
    data class WeatherResponse(val data: List<String>)
}