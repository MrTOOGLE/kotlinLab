package com.example.lab20

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.RemoteInput

class NotificationBroadcastReceiver(private val activity: MainActivity) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            MainActivity.ACTION_RESET_COLOR -> {
                activity.resetColor()
            }
            MainActivity.ACTION_SET_COLOR -> {
                val resultsFromIntent = RemoteInput.getResultsFromIntent(intent)
                if (resultsFromIntent != null) {
                    val inputText = resultsFromIntent.getCharSequence(MainActivity.KEY_TEXT)?.toString()
                    if (!inputText.isNullOrBlank()) {
                        // Убираем символ # если пользователь его ввел
                        val cleanHex = inputText.removePrefix("#")
                        activity.setColor(cleanHex)
                    }
                }
            }
        }
    }
}