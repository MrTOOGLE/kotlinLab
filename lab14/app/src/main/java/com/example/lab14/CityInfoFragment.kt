package com.example.lab14

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.lab14.databinding.FragmentCityInfoBinding

class CityInfoFragment : Fragment() {

    private var _binding: FragmentCityInfoBinding? = null
    private val binding get() = _binding!!

    private var currentCity: City? = null

    companion object {
        private const val ARG_CITY_INDEX = "city_index"

        fun newInstance(cityIndex: Int): CityInfoFragment {
            val fragment = CityInfoFragment()
            val args = Bundle()
            args.putInt(ARG_CITY_INDEX, cityIndex)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализируем данные
        Common.initCities(requireContext())

        // Получаем индекс города из аргументов
        arguments?.getInt(ARG_CITY_INDEX, -1)?.let { cityIndex ->
            if (cityIndex != -1 && cityIndex < Common.cities.size) {
                currentCity = Common.cities[cityIndex]
                updateCityInfo()
            } else {
                // Показываем заглушку
                showPlaceholder()
            }
        } ?: showPlaceholder()

        // Настраиваем кнопку карты
        binding.buttonShowOnMap.setOnClickListener {
            showCityOnMap()
        }
    }

    fun updateCity(city: City) {
        currentCity = city
        if (_binding != null) {
            updateCityInfo()
        }
    }

    private fun showPlaceholder() {
        binding.apply {
            textCity.text = "Выберите город из списка"
            textDistrict.text = ""
            textRegion.text = ""
            textPostalCode.text = ""
            textTimezone.text = ""
            textPopulation.text = ""
            textFounded.text = ""
            buttonShowOnMap.isEnabled = false
        }
    }

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
                buttonShowOnMap.isEnabled = true
            }
        }
    }

    private fun showCityOnMap() {
        currentCity?.let { city ->
            val mapIntent = Uri.parse("geo:${city.lat},${city.lon}").let { location ->
                Intent(Intent.ACTION_VIEW, location)
            }
            try {
                startActivity(mapIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "Приложение для карт не найдено", Toast.LENGTH_SHORT).show()
            }
        } ?: Toast.makeText(requireContext(), "Город не выбран", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}