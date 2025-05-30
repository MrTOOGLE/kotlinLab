package com.example.lab13

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

import com.example.lab13.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentCity: City? = null

    private val citySelectionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val cityIndex = data?.getIntExtra("city_index", -1) ?: -1
            if (cityIndex != -1) {
                currentCity = Common.cities[cityIndex]
                updateCityInfo()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация списка городов
        Common.initCities(this)

        // Обработчики кнопок
        binding.buttonSelectCity.setOnClickListener {
            val intent = Intent(this, CityListActivity::class.java)
            citySelectionLauncher.launch(intent)
        }

        binding.buttonShowOnMap.setOnClickListener {
            currentCity?.let { city ->
                val mapIntent = Uri.parse("geo:${city.lat},${city.lon}").let { location ->
                    Intent(Intent.ACTION_VIEW, location)
                }
                try {
                    startActivity(mapIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, "Приложение для карт не найдено", Toast.LENGTH_SHORT).show()
                }
            } ?: Toast.makeText(this, "Сначала выберите город", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateCityInfo() {
        currentCity?.let { city ->
            binding.apply {
                textCity.text = "Город: ${city.title}"
                textDistrict.text = "Федеральный округ: ${city.district}"
                textRegion.text = "Регион: ${city.region}"
                textPostalCode.text = "Почтовый индекс: ${city.postalCode}"
                textTimezone.text = "Часовой пояс: ${city.timezone}"
                textPopulation.text = "Население: ${city.population}"
                textFounded.text = "Основан в: ${city.founded} году"
            }
        }
    }
}