package com.example.lab12.src

import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lab12.R
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ProductAdapter
    private val productList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация списка товаров
        initProductList()

        // Настройка RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = ProductAdapter(productList)
        recyclerView.adapter = adapter

        // Обработка нажатия на кнопку корзины
        adapter.setOnCartClickListener { product, position ->
            // Изменение состояния товара
            product.isInCart = !product.isInCart

            // Показ соответствующего сообщения
            if (product.isInCart) {
                showColoredSnackbar(
                    getString(R.string.product_added, product.name),
                    ContextCompat.getColor(this, R.color.colorSuccess)
                )
            } else {
                showColoredSnackbar(
                    getString(R.string.product_removed, product.name),
                    ContextCompat.getColor(this, R.color.colorError)
                )
            }

            // Обновление элемента в списке
            adapter.notifyItemChanged(position)
        }
    }

    private fun initProductList() {
        // Получаем данные из ресурсов
        val names = resources.getStringArray(R.array.product_names)
        val pricesStrings = resources.getStringArray(R.array.product_prices)
        val images = resources.obtainTypedArray(R.array.product_images)

        // Создаем список товаров
        for (i in names.indices) {
            productList.add(
                Product(
                    name = names[i],
                    price = pricesStrings[i].toDouble(),
                    imageResource = images.getResourceId(i, 0)
                )
            )
        }

        // Освобождаем ресурсы TypedArray
        images.recycle()
    }

    private fun showColoredSnackbar(message: String, color: Int) {
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT
        )

        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(color)

        // Находим и изменяем цвет текста (при необходимости)
        val textView = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))

        snackbar.show()
    }
}