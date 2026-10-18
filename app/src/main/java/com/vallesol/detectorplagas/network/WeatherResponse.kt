package com.vallesol.detectorplagas.network

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("main") val main: MainResponse,
    @SerializedName("weather") val weather: List<WeatherDescription>
)

data class MainResponse(
    @SerializedName("temp") val temp: Double,
    @SerializedName("humidity") val humidity: Int
)

data class WeatherDescription(
    @SerializedName("description") val description: String
)