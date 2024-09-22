package com.example.mokrovlevlab6

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    private lateinit var a: EditText
    private lateinit var b: EditText
    private lateinit var c: EditText
    private lateinit var answer: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        a = findViewById(R.id.editTextText)
        b = findViewById(R.id.editTextText2)
        c = findViewById(R.id.editTextText3)
        answer = findViewById(R.id.textView8)
    }

    override fun onResume() {
        super.onResume()
        //TODO: как-то улучшить
        a.doAfterTextChanged {
            b.doAfterTextChanged {
                c.doAfterTextChanged {
                    calc()
                }
            }
        }
    }

    private fun calc() {
        val aa = a.text.toString().toDoubleOrNull()
        val bb = b.text.toString().toDoubleOrNull()
        val cc = c.text.toString().toDoubleOrNull()

        if (aa == null || bb == null || cc == null) {
            answer.setText("Неверный ввод")
            //Toast.makeText(this, "╚(•⌂•)╝", Toast.LENGTH_SHORT).show()
        } else {
            val D: Double = (bb * bb) - (4 * aa * cc)
            if (D < 0) {
                answer.setText("Корней нет")
            } else if (D == 0.0) {
                answer.setText("Найден один корень:\n${-bb / (2 * aa)}")
            } else {
                answer.setText("Найдено два корня:\n${(-bb + sqrt(D)) / (2 * aa)}\n${(-bb - sqrt(D)) / (2 * aa)}")
            }
        }
    }
}