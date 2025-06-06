package com.example.lab20

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_MSG_ID = "lab20_channel_msg"
        const val NOTIFICATION_ID = 1
        const val KEY_TEXT = "key_text"
        const val ACTION_RESET_COLOR = "com.example.notificationlab.RESET_COLOR"
        const val ACTION_SET_COLOR = "com.example.notificationlab.SET_COLOR"
    }

    private lateinit var showNotificationButton: Button
    private lateinit var statusText: TextView
    private lateinit var broadcastReceiver: NotificationBroadcastReceiver

    // Для запроса разрешений
    private val requestResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                showNotification()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        createNotificationChannel()
        setupBroadcastReceiver()
    }

    private fun initViews() {
        showNotificationButton = findViewById(R.id.showNotificationButton)
        statusText = findViewById(R.id.statusText)

        showNotificationButton.setOnClickListener {
            checkPermissionsAndShowNotification()
        }
    }

    private fun createNotificationChannel() {
        // NotificationChannel доступен только с API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_MSG_ID,
                "Управляющий канал",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Уведомления для управления цветом фона программы"

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setupBroadcastReceiver() {
        broadcastReceiver = NotificationBroadcastReceiver(this)
        val filter = IntentFilter().apply {
            addAction(ACTION_RESET_COLOR)
            addAction(ACTION_SET_COLOR)
        }
        ContextCompat.registerReceiver(
            this,
            broadcastReceiver,
            filter,
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    private fun checkPermissionsAndShowNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestResult.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                showNotification()
            }
        } else {
            showNotification()
        }
    }

    private fun showNotification() {
        // Намерение для открытия приложения при клике на уведомление
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Создание действия "Сбросить цвет"
        val resetColorIntent = Intent(ACTION_RESET_COLOR)
        resetColorIntent.setPackage(packageName)
        val resetPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            resetColorIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Создание действия "Задать цвет" с полем ввода
        val setColorIntent = Intent(ACTION_SET_COLOR)
        setColorIntent.setPackage(packageName)

        val flags = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
                PendingIntent.FLAG_MUTABLE
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ->
                PendingIntent.FLAG_IMMUTABLE
            else ->
                PendingIntent.FLAG_UPDATE_CURRENT
        }

        val setPendingIntent = PendingIntent.getBroadcast(
            this,
            1,
            setColorIntent,
            flags
        )

        // Создание поля ввода для цвета
        val remoteInput = RemoteInput.Builder(KEY_TEXT).run {
            setLabel("Цвет в формате RRGGBB")
            build()
        }

        val setColorAction = NotificationCompat.Action.Builder(
            R.drawable.ic_palette,
            "ЗАДАТЬ ЦВЕТ",
            setPendingIntent
        )
            .addRemoteInput(remoteInput)
            .build()

        // Построение уведомления
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this, CHANNEL_MSG_ID)
        } else {
            // Для старых версий Android используем deprecated конструктор
            @Suppress("DEPRECATION")
            NotificationCompat.Builder(this)
        }

        builder.setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Управляющий")
            .setContentText("Отсюда можно управлять цветом фона программы")
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .addAction(R.drawable.ic_clear, "СБРОСИТЬ ЦВЕТ", resetPendingIntent)
            .addAction(setColorAction)

        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED ||
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
        ) {
            notificationManager.notify(NOTIFICATION_ID, builder.build())
        }
    }

    fun resetColor() {
        window.decorView.setBackgroundColor(Color.WHITE)
        statusText.text = ""
    }

    fun setColor(colorHex: String) {
        try {
            val color = Color.parseColor("#$colorHex")
            window.decorView.setBackgroundColor(color)
            statusText.text = "Выбран цвет: #$colorHex"
        } catch (e: IllegalArgumentException) {
            statusText.text = "Неверный формат цвета: $colorHex"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }
}