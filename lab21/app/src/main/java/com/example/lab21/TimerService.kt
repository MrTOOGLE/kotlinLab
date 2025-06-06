package com.example.lab21

import android.app.*
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat

class TimerService : Service() {

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "TimerChannel"
    }

    private val binder = LocalBinder()
    private var countDownTimer: CountDownTimer? = null
    private var timeLeft = 0
    private var isTimerRunning = false
    private var updateListener: ((Int, Boolean) -> Unit)? = null

    inner class LocalBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun createNotificationChannel() {
        // NotificationChannel нужен только для API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Timer Service Channel",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Канал для уведомлений таймера"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun startTimer(totalSeconds: Int) {
        if (isTimerRunning) return

        timeLeft = totalSeconds
        isTimerRunning = true

        // Запускаем сервис переднего плана
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification = createNotification(timeLeft, pendingIntent)

        // Используем ServiceCompat для совместимости
        try {
            ServiceCompat.startForeground(
                this,
                NOTIFICATION_ID,
                notification,
                android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE
            )
        } catch (e: Exception) {
            // Fallback для старых версий Android
            startForeground(NOTIFICATION_ID, notification)
        }

        // Создаем и запускаем CountDownTimer
        countDownTimer = object : CountDownTimer((totalSeconds * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = (millisUntilFinished / 1000).toInt()
                updateNotification(timeLeft)
                updateListener?.invoke(timeLeft, true)
            }

            override fun onFinish() {
                stopTimer()
                updateListener?.invoke(0, false)
            }
        }

        countDownTimer?.start()
        updateListener?.invoke(timeLeft, true)
    }

    fun stopTimer() {
        countDownTimer?.cancel()
        countDownTimer = null
        isTimerRunning = false
        timeLeft = 0

        // Останавливаем сервис переднего плана
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)

        updateListener?.invoke(timeLeft, false)
    }

    fun isRunning(): Boolean = isTimerRunning

    fun getCurrentTime(): Int = timeLeft

    fun setUpdateListener(listener: (Int, Boolean) -> Unit) {
        this.updateListener = listener
    }

    private fun createNotification(timeLeft: Int, pendingIntent: PendingIntent): Notification {
        val minutes = timeLeft / 60
        val seconds = timeLeft % 60
        val timeText = String.format("%d:%02d", minutes, seconds)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Таймер")
            .setContentText(timeText)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification(timeLeft: Int) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification = createNotification(timeLeft, pendingIntent)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}