
package com.list.retrofitapiintegration

import android.os.Bundle
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
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {


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
            Text(text = apiInt, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    apiInt = "Loading..."
                    CoroutineScope(Dispatchers.IO).launch {
                        apiInt = fetchWeatherFact()
                    }
                }
            ) {
                Text("Fetch API ")
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


     fun fetchWeatherFact(): String {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: WeatherApiService = retrofit.create(WeatherApiService::class.java)
        var data = "Empty"

        val call: Call<WeatherResponse?>? = service.getWeatherData("CityName", "YOUR_API_KEY")
        call?.enqueue(object : Callback<WeatherResponse?> {
            override fun onResponse(call: Call<WeatherResponse?>?, response: Response<WeatherResponse?>) {
                if (response.isSuccessful()) {
                    val weatherData: WeatherResponse? = response.body()

                    data = weatherData?.data.toString()
                } else {
                    // Handle the error
                }
            }

            override fun onFailure(call: Call<WeatherResponse?>?, t: Throwable?) {
                // Handle network errors
            }
        })

        return data
    }

    @Serializable
    data class WeatherResponse(val data: List<String>)
}