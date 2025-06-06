package com.example.lab21

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var minutesEditText: EditText
    private lateinit var secondsEditText: EditText
    private lateinit var startStopButton: Button
    private lateinit var statusTextView: TextView

    private var timerService: TimerService? = null
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TimerService.LocalBinder
            timerService = binder.getService()
            isBound = true

            // Обновляем UI при подключении к сервису
            timerService?.setUpdateListener { timeLeft, isRunning ->
                runOnUiThread {
                    updateUI(timeLeft, isRunning)
                }
            }

            // Синхронизируем состояние UI с сервисом
            timerService?.let { service ->
                updateUI(service.getCurrentTime(), service.isRunning())
            }
        }

        override fun onServiceDisconnected(className: ComponentName) {
            timerService = null
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Запрашиваем разрешение на уведомления для Android 13+
        requestNotificationPermission()

        initViews()
        setupClickListeners()
        setupEditTextListeners()
    }

    override fun onStart() {
        super.onStart()
        // Сначала запускаем сервис, потом привязываемся
        val intent = Intent(this, TimerService::class.java)
        startService(intent)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        // Отвязываемся от сервиса при остановке активности
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
        }
    }

    private fun initViews() {
        minutesEditText = findViewById(R.id.minutesEditText)
        secondsEditText = findViewById(R.id.secondsEditText)
        startStopButton = findViewById(R.id.startStopButton)
        statusTextView = findViewById(R.id.statusTextView)

        // Устанавливаем начальные значения
        minutesEditText.setText("0")
        secondsEditText.setText("30")
        statusTextView.text = "Таймер готов к запуску"
    }

    private fun setupEditTextListeners() {
        // При фокусе выделяем весь текст для удобного редактирования
        minutesEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                minutesEditText.selectAll()
            }
        }

        secondsEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                secondsEditText.selectAll()
            }
        }

        // При клике также выделяем весь текст
        minutesEditText.setOnClickListener {
            minutesEditText.selectAll()
        }

        secondsEditText.setOnClickListener {
            secondsEditText.selectAll()
        }
    }

    private fun setupClickListeners() {
        startStopButton.setOnClickListener {
            if (isBound && timerService != null) {
                if (timerService!!.isRunning()) {
                    // Останавливаем таймер
                    timerService!!.stopTimer()
                } else {
                    // Запускаем таймер
                    val minutes = minutesEditText.text.toString().toIntOrNull() ?: 0
                    val seconds = secondsEditText.text.toString().toIntOrNull() ?: 0
                    val totalSeconds = minutes * 60 + seconds

                    if (totalSeconds > 0) {
                        timerService!!.startTimer(totalSeconds)
                    } else {
                        Toast.makeText(this, "Введите время больше 0", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Сервис не подключен", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(timeLeft: Int, isRunning: Boolean) {
        if (isRunning) {
            // Во время работы таймера
            minutesEditText.isEnabled = false
            secondsEditText.isEnabled = false
            startStopButton.text = "Стоп"

            val minutes = timeLeft / 60
            val seconds = timeLeft % 60
            statusTextView.text = String.format("%d:%02d", minutes, seconds)
        } else {
            // Таймер остановлен
            minutesEditText.isEnabled = true
            secondsEditText.isEnabled = true
            startStopButton.text = "Пуск"
            statusTextView.text = "Таймер готов к запуску"
        }
    }
}