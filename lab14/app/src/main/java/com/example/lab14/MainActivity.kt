package com.example.lab14

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.lab14.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isTwoPane = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Определяем режим отображения (одна или две панели)
        isTwoPane = findViewById<View>(R.id.fragment_city_info) != null

        if (savedInstanceState == null) {
            if (isTwoPane) {
                // В двухпанельном режиме добавляем оба фрагмента
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_city_info, CityInfoFragment.newInstance(-1), "city_info")
                    .replace(R.id.fragment_city_list, CityListFragment())
                    .commit()
            } else {
                // В однопанельном режиме только список городов
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_city_list, CityListFragment())
                    .commit()
            }
        }
    }

    fun onCitySelected(city: City) {
        val cityIndex = Common.cities.indexOf(city)

        if (isTwoPane) {
            // В двухпанельном режиме обновляем правый фрагмент
            val infoFragment = supportFragmentManager.findFragmentByTag("city_info") as? CityInfoFragment
            infoFragment?.updateCity(city)
        } else {
            // В однопанельном режиме заменяем фрагмент
            val fragment = CityInfoFragment.newInstance(cityIndex)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_city_list, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}