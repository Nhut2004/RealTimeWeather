package com.example.weather.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.api.Constant
import com.example.weather.api.NetworkResponse
import com.example.weather.api.RetrofitInstance
import com.example.weather.api.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult = _weatherResult


    fun getData(city: String) {
        _weatherResult.postValue(NetworkResponse.Loading) // Dùng postValue để cập nhật từ luồng IO
        viewModelScope.launch(Dispatchers.IO) {
            try {

                val response = weatherApi.getWeather(Constant.apiKey, city)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.postValue(NetworkResponse.Success(it)) // Dùng postValue thay vì .value
                    } ?: _weatherResult.postValue(NetworkResponse.Error("No data received"))
                } else {
                    _weatherResult.postValue(NetworkResponse.Error("Fail to load data: ${response.message()}"))
                }
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Exception: ${e.message}", e)
                _weatherResult.postValue(NetworkResponse.Error("Error: ${e.message}"))
            }
        }
    }


}