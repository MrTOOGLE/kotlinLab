package com.example.mokrovlevlab7

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var img: ImageView
    private lateinit var btnEat: Button
    private lateinit var btnNotEat: Button
    private lateinit var txtR: String
    private lateinit var txtM: String
    private lateinit var textViewR: TextView
    private lateinit var textViewM: TextView
    private var counter: Int = 0
    private var counterR: Int = 0
    private var counterM: Int = 0
    private var randImg: Int = 0
    private var name: String = ""


    private var images = arrayOf(
        R.drawable.food01,
        R.drawable.food02,
        R.drawable.food03,
        R.drawable.food04,
        R.drawable.food05,
        R.drawable.food06,
        R.drawable.food07,
        R.drawable.food08,
        R.drawable.food09,
        R.drawable.food10,
        R.drawable.food11,
        R.drawable.food12,
        R.drawable.food13,
        R.drawable.food14,
        R.drawable.food15,
        R.drawable.food16,
        R.drawable.food17,
        R.drawable.food18,
        R.drawable.food19,
        R.drawable.food20,
        R.drawable.sport01,
        R.drawable.sport02,
        R.drawable.sport03,
        R.drawable.sport04,
        R.drawable.sport05,
        R.drawable.sport06,
        R.drawable.sport07,
        R.drawable.sport08,
        R.drawable.sport09,
        R.drawable.sport10,
        R.drawable.sport11,
        R.drawable.sport12,
        R.drawable.sport13,
        R.drawable.sport14,
        R.drawable.sport15,
        R.drawable.sport16,
        R.drawable.sport17,
        R.drawable.sport18,
        R.drawable.sport19,
        R.drawable.sport20
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        img = findViewById(R.id.imageView)
        var randImg: Int = setImg()
        btnEat = findViewById(R.id.button)
        btnNotEat = findViewById(R.id.button2)
        textViewR = findViewById(R.id.textView)
        textViewM = findViewById(R.id.textView2)

        txtR = getString(R.string.right)
        txtM = getString(R.string.mistake)
        txtR = String.format(txtR, counterR, counter)
        txtM = String.format(txtM, counterM, counter)
        textViewR.text = txtR
        textViewM.text = txtM

        btnEat.setOnClickListener {
            btnHandler("food")
        }

        btnNotEat.setOnClickListener {
            btnHandler("sport")
        }
    }

    fun setImg(): Int {
        val randImg = (images.indices).random()
        img.setImageResource(images[randImg])

        return randImg
    }

    fun btnHandler(type: String) {
        txtR = getString(R.string.right)
        txtM = getString(R.string.mistake)

        name = resources.getResourceEntryName(images[randImg])
        counter++

        if (name.contains(type)) {
            counterR++
        }
        else {
            counterM++
        }

        txtR = String.format(txtR, counterR, counter)
        txtM = String.format(txtM, counterM, counter)
        textViewR.text = txtR
        textViewM.text = txtM

        randImg = setImg()
    }
}