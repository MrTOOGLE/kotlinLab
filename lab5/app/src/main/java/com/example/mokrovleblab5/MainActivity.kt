package com.example.mokrovleblab5

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var b1: TextView
    private lateinit var b2: TextView
    private lateinit var b3: TextView
    private lateinit var b5: TextView
    private lateinit var b6: TextView
    private lateinit var b4: TextView
    private lateinit var b7: TextView
    private lateinit var b8: TextView
    private lateinit var b9: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        b1 = findViewById(R.id.textView)
        b2 = findViewById(R.id.textView2)
        b3 = findViewById(R.id.textView3)
        b4 = findViewById(R.id.textView4)
        b5 = findViewById(R.id.textView5)
        b6 = findViewById(R.id.textView6)
        b7 = findViewById(R.id.textView7)
        b8 = findViewById(R.id.textView8)
        b9 = findViewById(R.id.textView9)
    }

    override fun onResume() {
        super.onResume()
        changeColor()
    }

    fun onClick(view: View) {
        changeColor()
    }

    fun changeColor() {
        val color1 = (0..255).random()
        val color2 = (0..255).random()
        b1.setBackgroundColor(Color.rgb(color1, 0, color2))
        b2.setBackgroundColor(Color.rgb(color1, 28, color2))
        b3.setBackgroundColor(Color.rgb(color1, 56, color2))
        b4.setBackgroundColor(Color.rgb(color1, 84, color2))
        b5.setBackgroundColor(Color.rgb(color1, 112, color2))
        b6.setBackgroundColor(Color.rgb(color1, 140, color2))
        b7.setBackgroundColor(Color.rgb(color1, 168, color2))
        b8.setBackgroundColor(Color.rgb(color1, 196, color2))
        b9.setBackgroundColor(Color.rgb(color1, 224, color2))
    }
}