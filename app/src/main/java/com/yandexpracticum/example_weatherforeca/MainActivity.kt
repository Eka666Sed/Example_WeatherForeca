package com.yandexpracticum.example_weatherforeca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yandexpracticum.example_weatherforeca.domain.ForecaRepository

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val forecaRepository = ForecaRepository()
        forecaRepository.getCurrentWeather()
    }
}