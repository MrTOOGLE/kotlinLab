package com.example.mokrovlevlab10

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private var redValues = intArrayOf(0, 0, 0)
    private var greenValues = intArrayOf(0, 0, 0)
    private var blueValues = intArrayOf(0, 0, 0)
    private lateinit var btn: Button
    private lateinit var msg: EditText
    private lateinit var btnMsg: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn = findViewById(R.id.button)
        msg = findViewById(R.id.editTextText)
        btnMsg = findViewById(R.id.editTextText2)

        btn.setOnClickListener {
            redValues[0] = findViewById<SeekBar>(R.id.seekBar11).progress
            redValues[1] = findViewById<SeekBar>(R.id.seekBar21).progress
            redValues[2] = findViewById<SeekBar>(R.id.seekBar31).progress

            greenValues[0] = findViewById<SeekBar>(R.id.seekBar12).progress
            greenValues[1] = findViewById<SeekBar>(R.id.seekBar22).progress
            greenValues[2] = findViewById<SeekBar>(R.id.seekBar32).progress

            blueValues[0] = findViewById<SeekBar>(R.id.seekBar13).progress
            blueValues[1] = findViewById<SeekBar>(R.id.seekBar23).progress
            blueValues[2] = findViewById<SeekBar>(R.id.seekBar33).progress

            val view: ConstraintLayout = findViewById(R.id.main)
            val sb: Snackbar = Snackbar.make(view, msg.text.toString(), Snackbar.LENGTH_SHORT)
            sb.setBackgroundTint(Color.rgb(redValues[0], greenValues[0], blueValues[0]))
            sb.setTextColor(Color.rgb(redValues[1], greenValues[1], blueValues[1]))
            sb.setAction(btnMsg.text.toString()) {}
            sb.setActionTextColor(Color.rgb(redValues[2], greenValues[2], blueValues[2]))
            sb.show()
        }
    }
}