package com.example.mokrovlevlab8

import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var meters: EditText
    private lateinit var parrot: EditText
    private lateinit var boa: EditText
    private lateinit var elephant: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        meters = findViewById(R.id.editTextText)
        parrot = findViewById(R.id.editTextText2)
        boa = findViewById(R.id.editTextText3)
        elephant = findViewById(R.id.editTextText4)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // TODO: узнать как сделать норм
        outState.putInt("meters", meters.text)
    }
}