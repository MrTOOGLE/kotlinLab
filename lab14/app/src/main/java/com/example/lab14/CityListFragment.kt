package com.example.lab14

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab14.databinding.FragmentCityListBinding

class CityListFragment : Fragment() {

    private var _binding: FragmentCityListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CityAdapter
    private var layoutManagerState: Parcelable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализируем данные
        Common.initCities(requireContext())

        // Настраиваем адаптер
        adapter = CityAdapter { position ->
            onCitySelected(position)
        }

        // Настраиваем RecyclerView
        binding.recyclerViewCities.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCities.adapter = adapter

        // Загружаем данные
        adapter.submitList(Common.cities)

        // Восстанавливаем позицию прокрутки если есть
        layoutManagerState?.let {
            (binding.recyclerViewCities.layoutManager as LinearLayoutManager)
                .onRestoreInstanceState(it)
        }
    }

    private fun onCitySelected(position: Int) {
        val city = Common.cities[position]

        // Передаем данные в родительскую активность
        (activity as? MainActivity)?.onCitySelected(city)
    }

    override fun onPause() {
        super.onPause()
        // Сохраняем позицию прокрутки
        layoutManagerState = (binding.recyclerViewCities.layoutManager as LinearLayoutManager)
            .onSaveInstanceState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}