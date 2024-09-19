package com.example.mokrovlevlab4

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var sumBtn: Button
    val minBtn: Button = findViewById(R.id.button2)
    val multBtn: Button = findViewById(R.id.button3)
    val divBtn: Button = findViewById(R.id.button4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val editText: EditText = findViewById(R.id.editText)
        val editText2: EditText = findViewById(R.id.editText2)

        val txtView: TextView = findViewById(R.id.textView2)

        sumBtn.setOnClickListener {
            txtView.text = calc("sum", editText, editText2)
        }
        minBtn.setOnClickListener {
            txtView.text = calc("min", editText, editText2)
        }
        multBtn.setOnClickListener {
            txtView.text = calc("mult", editText, editText2)
        }
        divBtn.setOnClickListener {
            txtView.text = calc("div", editText, editText2)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun calc(type: String, editText: EditText, editText2: EditText) : String {
        val num = editText.text.toString().toFloatOrNull()
        val num2 = editText2.text.toString().toFloatOrNull()
        if (num == null || num2 == null) {
            Toast.makeText(this, "╚(•⌂•)╝", Toast.LENGTH_SHORT).show()
            return "Одно из значений не было введено или введено некорректно"
        }

        return when (type) {
            "sum" -> "$num + $num2 = ${num + num2}"
            "min" -> "$num - $num2 = ${num - num2}"
            "mult" -> "$num × $num2 = ${num * num2}"
            "div" -> "$num ÷ $num2 = ${num / num2}"
            else -> "Некорректный ввод ಠ╭╮ಠ"
        }
    }

    fun onClick(view: View) {
        
    }
}