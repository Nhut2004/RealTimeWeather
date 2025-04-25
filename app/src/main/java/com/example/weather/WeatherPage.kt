package com.example.weather

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weather.ViewModel.WeatherViewModel
import com.example.weather.api.NetworkResponse
import com.example.weather.api.NetworkResponse.Error
import com.example.weather.api.WeatherModel

@Composable
fun WeatherPageApp(viewModel: WeatherViewModel){

    var city by rememberSaveable { mutableStateOf("") }

    val weatherResult = viewModel.weatherResult.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = city,
                onValueChange = {
                    city = it
                },
                modifier = Modifier.weight(1f),
                label = {
                    Text(
                        text = "Search your location : "
                    )
                }
            )
            IconButton(
                onClick = {
                    if (city.isNotBlank()) {
                        viewModel.getData(city)
                    } else {
                        Log.d("WeatherPageApp", "City is empty")
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "")
            }
        }

        weatherResult.value?.let { result ->
            when (result) {
                is Error<*> -> {
                    Text(text = result.toString())
                }
                NetworkResponse.Loading -> {
                    CircularProgressIndicator()
                }
                is NetworkResponse.Success -> {
                    WeatherDetails(data = result.data)
                }
            }
        } ?: Text(text = "Chưa có dữ liệu")

    }

}

@Composable
fun WeatherDetails(data: WeatherModel){
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ){
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Icon",
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = data.location.name,
                fontSize = 30.sp
            )

        }
        Text(
            text = data.location.country,
            fontSize = 18.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "${data.current.temp_c} °C",
            fontSize = 55.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        AsyncImage(
            modifier = Modifier.size(160.dp),
            model = "https:${data.current.condition.icon}",
            contentDescription = "Condition Icon",


        )
        Log.d("WeatherIcon", "Image URL: https:${data.current.condition.icon}")
        Text(
            text = data.current.condition.text,
            fontSize = 20.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherCard("Humidity", data.current.humidity.toString())
                    WeatherCard("Wind Speed", data.current.wind_kph.toString())
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherCard("UV", data.current.uv.toString())
                    WeatherCard("Participation", data.current.precip_mm.toString())
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    val localTime = data.location.localtime.toString()
                    val dateParts = localTime.split(" ")[0].split("-") // [YYYY, MM, DD]
                    val timePart = localTime.split(" ")[1] // HH:mm

                    WeatherCard("Local Date", "${dateParts[2]}/${dateParts[1]}/${dateParts[0]}")
                    WeatherCard("Local Time", timePart)

                }

            }

        }

    }

}

@Composable
fun WeatherCard(key: String, value: String){
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = key,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )


    }
}
