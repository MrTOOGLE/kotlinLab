package com.example.mokrovlevlab8

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged

class MainActivity : AppCompatActivity() {
    private lateinit var monkey: EditText
    private lateinit var parrot: EditText
    private lateinit var boa: EditText
    private lateinit var elephant: EditText

    private val monkeyInMeters: Double = 0.45
    private val parrotInMeters: Double = 0.125
    private val boaInMeters: Double = 4.0
    private val elephantInMeters: Double = 6.0
    private var flag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        monkey = findViewById(R.id.editTextText)
        parrot = findViewById(R.id.editTextText2)
        boa = findViewById(R.id.editTextText3)
        elephant = findViewById(R.id.editTextText4)

        monkey.doAfterTextChanged {
            Data.monkey = monkey.text.toString()

            if (!flag) {
                flag = true
                convert(Data.monkey, monkeyInMeters, "monkey")
            }
        }

        parrot.doAfterTextChanged {
            Data.parrot = parrot.text.toString()

            if (!flag) {
                flag = true
                convert(Data.parrot, parrotInMeters, "parrot")
            }
        }

        boa.doAfterTextChanged {
            Data.boa = boa.text.toString()
            Thread.sleep(100)

            if (!flag) {
                flag = true
                convert(Data.boa, boaInMeters, "boa")
            }
        }

        elephant.doAfterTextChanged {
            Data.elephant = elephant.text.toString()

            if (!flag) {
                flag = true
                convert(Data.elephant, elephantInMeters, "elephant")
            }
        }
    }

    private fun convert(animalNum: String, animalInMeters: Double, animalType: String) {
        try {
            val meters: Double = animalNum.toDouble() * animalInMeters
            when (animalType) {
                "monkey" -> {
                    parrot.setText("")
                    boa.setText("")
                    elephant.setText("")

                    parrot.hint = (meters / parrotInMeters).toString()
                    boa.hint = (meters / boaInMeters).toString()
                    elephant.hint = (meters / elephantInMeters).toString()
                }
                "parrot" -> {
                    monkey.setText("")
                    boa.setText("")
                    elephant.setText("")

                    monkey.hint = (meters / monkeyInMeters).toString()
                    boa.hint = (meters / boaInMeters).toString()
                    elephant.hint = (meters / elephantInMeters).toString()
                }
                "boa" -> {
                    monkey.setText("")
                    parrot.setText("")
                    elephant.setText("")

                    monkey.hint = (meters / monkeyInMeters).toString()
                    parrot.hint = (meters / parrotInMeters).toString()
                    elephant.hint = (meters / elephantInMeters).toString()
                }
                "elephant" -> {
                    monkey.setText("")
                    parrot.setText("")
                    boa.setText("")

                    monkey.hint = (meters / monkeyInMeters).toString()
                    parrot.hint = (meters / parrotInMeters).toString()
                    boa.hint = (meters / boaInMeters).toString()
                }
            }
        } catch (_: NumberFormatException) {
            Toast.makeText(applicationContext, "Ошибка ввода", Toast.LENGTH_SHORT).show()
        }
        flag = false
    }
}