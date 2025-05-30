package com.example.lab13

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab13.databinding.ActivityCityListBinding

class CityListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCityListBinding
    private lateinit var adapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация RecyclerView
        adapter = CityAdapter { position ->
            // При клике на город возвращаем результат
            val intent = Intent()
            intent.putExtra("city_index", position)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Загружаем список городов
        adapter.submitList(Common.cities)
    }
}