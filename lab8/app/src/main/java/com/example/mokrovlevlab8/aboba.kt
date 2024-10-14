package com.example.mokrovlevlab8

import android.icu.text.DecimalFormat
import java.math.BigDecimal
import java.math.RoundingMode

fun main() {
    println(convert(3.657667, 2.54654))
}

fun convert(from: Double, to: Double): String {
    val result = from / to
    println(result)
    return BigDecimal(result).setScale(2, RoundingMode.DOWN).toString()
}