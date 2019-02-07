# Weather App

This is a minimal weather Android app that displays a list of cities around the user's location and provides detailed weather information about each city.

## OpenWeatherMap

This app uses the OpenWeatherMap Api to fetch weather data.

In order to run this app you will need an API key:

- Create an account on : https://home.openweathermap.org/users/sign_in

- Copy the default api key.

- On your computer, in C:/users/<your_username>/.gradle open the gradle.properties file (or create it if it doesn't exist)
- Write : OpenWeatherApiKey = "<your_api_key>"

## Run project on Android Studio

Clone this repository

```bash
git clone https://github.com/zachsao/WeatherApp.git
```
Or Download and extract the zip file.

Open Project in Android Studio and click Run.

### Libraries

RxJava

Retrofit for networking

Dagger 2 for Dependency Injection

Data Binding Android Library

Timber for logging

Moshi for parsing JSON

Glide for image display
