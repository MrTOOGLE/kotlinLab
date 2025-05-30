package com.example.lab12.src

data class Product(
    val name: String,
    val price: Double,
    val imageResource: Int,
    var isInCart: Boolean = false
)