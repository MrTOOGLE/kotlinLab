package com.example.mokrovlevlab8

import java.math.BigDecimal
import java.math.RoundingMode

object Converter {
    var meter = 1
    var parrot = 0.13
    var boa = 5
    var elephant = 2.5

    // TODO - переделать, так как конвертация идёт от одного изменённого значения к 3ём другим
    fun convert(from: Double, to: Double): String {
        val result = from / to
        return BigDecimal(result).setScale(2, RoundingMode.DOWN).toString()
    }
}